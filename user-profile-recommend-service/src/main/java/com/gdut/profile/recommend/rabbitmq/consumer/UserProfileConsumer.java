package com.gdut.profile.recommend.rabbitmq.consumer;

import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.enums.RabbitMQConstants;
import com.gdut.profile.recommend.service.UserProfileService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author liujunliang
 * @date 2025/12/6
 */
@Component
public class UserProfileConsumer {
    @Resource
    private UserProfileService userProfileService;

    /**
     * 监听画像数据队列，消费用户id并更新用户画像
     * @param userId 用户id
     */
    @RabbitListener(queues = RabbitMQConstants.PROFILE_QUEUE)
    public void consumeBehaviorMessage(String userId) throws IOException {
        try {
            // 不需要channel.basicAck
            userProfileService.updateUserProfileIncrementally(userId);
        } catch (Exception e) {
            // 5. 异常处理：拒绝消息（进入死信队列），记录日志
            System.err.println("消费信息异常，消息进入死信队列：" + userId);
        }
    }
}
