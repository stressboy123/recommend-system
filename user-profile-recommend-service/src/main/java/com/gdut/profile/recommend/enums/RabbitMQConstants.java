package com.gdut.profile.recommend.enums;

/**
 * @author liujunliang
 * @date 2025/12/6
 */
public class RabbitMQConstants {
    // 画像数据交换机（直连型）
    public static final String PROFILE_EXCHANGE = "profile.exchange";
    // 画像数据队列
    public static final String PROFILE_QUEUE = "profile.queue";
    // 画像数据路由键
    public static final String PROFILE_ROUTING_KEY = "profile.routing.key";

    // 死信交换机（处理消费失败的消息）
    public static final String PROFILE_DLX_EXCHANGE = "profile.dlx.exchange";
    // 死信队列
    public static final String PROFILE_DLX_QUEUE = "profile.dlx.queue";
    // 死信路由键
    public static final String PROFILE_DLX_ROUTING_KEY = "profile.dlx.routing.key";
}
