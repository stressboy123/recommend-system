package com.gdut.profile.recommend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gdut.profile.recommend.enums.RabbitMQConstants;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liujunliang
 * @date 2025/12/6
 */
@Configuration
public class RabbitMQConfig {
    // ========== 1. 声明普通队列+交换机+绑定（带死信配置） ==========
    @Bean
    public Queue profileQueue() {
        return QueueBuilder.durable(RabbitMQConstants.PROFILE_QUEUE) // 持久化队列（保证MQ重启后队列不丢失）
                .deadLetterExchange(RabbitMQConstants.PROFILE_DLX_EXCHANGE) // 绑定死信交换机
                .deadLetterRoutingKey(RabbitMQConstants.PROFILE_DLX_ROUTING_KEY) // 死信路由键
                .maxLength(10000) // 队列最大长度（可选，防止消息堆积）
                .build();
    }

    @Bean
    public DirectExchange profileExchange() {
        return ExchangeBuilder.directExchange(RabbitMQConstants.PROFILE_EXCHANGE)
                .durable(true) // 持久化交换机
                .build();
    }

    @Bean
    public Binding profileBinding() {
        return BindingBuilder.bind(profileQueue())
                .to(profileExchange())
                .with(RabbitMQConstants.PROFILE_ROUTING_KEY);
    }

    // ========== 2. 声明死信队列+交换机+绑定（兜底失败消息） ==========
    @Bean
    public Queue profileDlxQueue() {
        return QueueBuilder.durable(RabbitMQConstants.PROFILE_DLX_QUEUE).build();
    }

    @Bean
    public DirectExchange profileDlxExchange() {
        return ExchangeBuilder.directExchange(RabbitMQConstants.PROFILE_DLX_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding profileDlxBinding() {
        return BindingBuilder.bind(profileDlxQueue())
                .to(profileDlxExchange())
                .with(RabbitMQConstants.PROFILE_DLX_ROUTING_KEY);
    }

    // ========== 3. 配置JSON消息序列化器（解决LocalDateTime序列化） ==========
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 支持LocalDateTime序列化
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    // ========== 4. 配置RabbitTemplate（生产者序列化） ==========
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        // 开启消息发送确认（保证消息到达交换机）
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                // 消息未到达交换机，可记录日志/告警/重试
                System.err.println("消息发送失败：" + cause);
            }
        });
        // 开启消息返回（保证消息到达队列）
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            // 消息未到达队列，可记录日志/告警/重试
            System.err.println("消息未到达队列：" + returnedMessage.getReplyText());
        });
        return rabbitTemplate;
    }

    // ========== 5. 配置消费者容器（并发数） ==========
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrentConsumers(1); // 消费者并发数（根据业务调整）
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false); // 拒绝的消息不重新入队（交给死信）
        return factory;
    }
}
