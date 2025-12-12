package com.gdut.behaviordata.rabbitmq.consumer;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.enums.RabbitMQConstants;
import com.gdut.behaviordata.service.BehaviorService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author liujunliang
 * @date 2025/12/6
 */
@Component
public class BehaviorDlxConsumer {

    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;

    @Resource
    private BehaviorService behaviorService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 监听死信队列，重试保存失败的行为数据
     */
    @RabbitListener(queues = RabbitMQConstants.BEHAVIOR_DLX_QUEUE)
    public void consumeDlxBehaviorMessage(BehaviorDetail behavior,
                                          Message message,
                                          Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 1. 获取当前重试次数（从消息头获取，首次进入死信为1，每次重试+1）
        Integer retryCount = message.getMessageProperties().getHeader("x-retry-count");
        if (retryCount == null) {
            retryCount = 1;
        }

        try {
            // 2. 校验是否已超过最大重试次数
            if (retryCount > MAX_RETRY_COUNT) {
                // 超过最大次数：记录日志（标记需人工处理），并ACK（避免死信队列堆积）
                logManualProcess(behavior, retryCount);
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 3. 幂等校验（再次确认，防止重试时已保存）
            Result<BehaviorDetail> existResult = behaviorService.getBehaviorDetail(behavior.getId());
            if (existResult.isSuccess() && existResult.getData() != null) {
                channel.basicAck(deliveryTag, false);
                System.out.println("死信重试：行为数据已存在（幂等），忽略：" + behavior.getId());
                return;
            }

            // 4. 重试保存
            Result<Integer> result = behaviorService.saveBehaviorDetail(behavior);
            if (result.isSuccess()) {
                // 重试成功：ACK确认
                channel.basicAck(deliveryTag, false);
                System.out.println("死信重试成功（第" + retryCount + "次）：" + behavior.getId());
            } else {
                // 重试失败：更新重试次数，重新发送到原队列（或继续入死信）
                retryCount++;
                resendToDlxQueue(behavior, retryCount);
                channel.basicAck(deliveryTag, false); // 先ACK当前死信消息
                System.out.println("死信重试失败（第" + (retryCount - 1) + "次），重试次数+1：" + behavior.getId());
            }
        } catch (Exception e) {
            // 5. 重试异常：更新重试次数，重新入死信
            retryCount++;
            resendToDlxQueue(behavior, retryCount);
            channel.basicAck(deliveryTag, false);
            System.err.println("死信重试异常（第" + (retryCount - 1) + "次）：" + behavior.getId());
        }
    }

    /**
     * 将失败消息重新发送到死信队列（更新重试次数）
     */
    private void resendToDlxQueue(BehaviorDetail behavior, int retryCount) {
        // 构建消息头，记录重试次数
        org.springframework.amqp.core.MessageProperties properties = new org.springframework.amqp.core.MessageProperties();
        properties.setHeader("x-retry-count", retryCount);
        // 重新发送到死信队列（也可发送回原队列，根据业务选择）
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.BEHAVIOR_DLX_EXCHANGE,
                RabbitMQConstants.BEHAVIOR_DLX_ROUTING_KEY,
                behavior,
                msg -> {
                    msg.getMessageProperties().setHeader("x-retry-count", retryCount);
                    return msg;
                }
        );
    }

    /**
     * 记录需人工处理的日志（可写入数据库/告警平台）
     */
    private void logManualProcess(BehaviorDetail behavior, int retryCount) {
        String log = String.format(
                "行为数据超过最大重试次数（%d次），需人工处理！ID：%s，用户ID：%s，行为类型：%d",
                retryCount, behavior.getId(), behavior.getUserId(), behavior.getBehaviorType()
        );
        // 打印错误日志
        System.err.println(log);
    }
}
