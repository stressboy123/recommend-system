package com.gdut.behaviordata.rabbitmq.consumer;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.enums.RabbitMQConstants;
import com.gdut.behaviordata.rabbitmq.producer.UserProfileProducer;
import com.gdut.behaviordata.service.BehaviorService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Component
public class BehaviorConsumer {

    @Resource
    private BehaviorService behaviorService;

    @Resource
    private UserProfileProducer userProfileProducer;

    /**
     * 监听行为数据队列，消费消息并保存到数据库
     * @param behavior 消息体（自动反序列化为BehaviorDetail）
     * @param message 原生消息（用于ACK）
     * @param channel 信道（用于手动ACK）
     */
    @RabbitListener(queues = RabbitMQConstants.BEHAVIOR_QUEUE)
    public void consumeBehaviorMessage(BehaviorDetail behavior,
                                       Message message,
                                       Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // 1. 幂等校验（防止重复消费）：先查询是否已存在该ID的数据
            Result<BehaviorDetail> queryBehavior = behaviorService.getBehaviorDetail(behavior.getId());
            if (queryBehavior.isSuccess() && queryBehavior.getData() != null) {
                // 已存在，直接确认消息（ACK）
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 2. 调用保存方法（带事务）
            Result<Integer> result = behaviorService.saveBehaviorDetail(behavior);

            // 3. 保存成功：手动ACK（确认消息已消费）
            if (result.isSuccess()) {
                // TODO 3.1 更新hot_behavior表
                try {
                    userProfileProducer.sendBehaviorMessage(behavior.getUserId());
                } catch (Exception e) {
                    System.err.println("发送画像更新消息失败: " + e);
                }
                channel.basicAck(deliveryTag, false); // false：不批量确认
            } else {
                // 4. 保存失败：拒绝消息（不重新入队，进入死信队列）
                channel.basicNack(deliveryTag, false, false); // 第三个false：不重新入队
                System.err.println("保存行为数据失败，消息进入死信队列：" + behavior.getId());
            }
        } catch (Exception e) {
            // 5. 异常处理：拒绝消息（进入死信队列），记录日志
            channel.basicNack(deliveryTag, false, false);
            System.err.println("消费行为数据异常，消息进入死信队列：" + behavior.getId());
        }
    }
}