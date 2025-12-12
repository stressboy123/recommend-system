package com.gdut.behaviordata.rabbitmq.producer;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.enums.RabbitMQConstants;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Component
public class BehaviorProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送行为数据消息到MQ
     * @param behavior 行为数据实体
     */
    public void sendBehaviorMessage(BehaviorDetail behavior) {
        // 1. 生成唯一消息ID（用于幂等/追踪）
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);

        // 2. 给BehaviorDetail补全必要字段
        if (behavior.getId() == null) {
            behavior.setId(messageId);
        }
        if (behavior.getCreateTime() == null) {
            behavior.setCreateTime(LocalDateTime.now());
        }
        if (behavior.getIsValid() == null) {
            behavior.setIsValid(1);
        }

        // 3. 发送消息
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.BEHAVIOR_EXCHANGE,
                RabbitMQConstants.BEHAVIOR_ROUTING_KEY,
                behavior,
                correlationData
        );
    }
}
