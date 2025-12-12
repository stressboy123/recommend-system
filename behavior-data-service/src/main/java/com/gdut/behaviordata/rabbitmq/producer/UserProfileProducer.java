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
 * @date 2025/12/6
 */
@Component
public class UserProfileProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送需要更新的用户id到画像服务
     * @param userId 用户id
     */
    public void sendBehaviorMessage(String userId) {
        // 1. 生成唯一消息ID（用于幂等/追踪）
        String messageId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(messageId);

        // 3. 发送消息
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.PROFILE_EXCHANGE,
                RabbitMQConstants.PROFILE_ROUTING_KEY,
                userId,
                correlationData
        );
    }
}
