package com.gdut.behaviordata.enums;

/**
 * @author liujunliang
 * @date 2025/12/6
 */
public class RabbitMQConstants {
    // 行为数据交换机（直连型）
    public static final String BEHAVIOR_EXCHANGE = "behavior.exchange";
    // 行为数据队列
    public static final String BEHAVIOR_QUEUE = "behavior.queue";
    // 行为数据路由键
    public static final String BEHAVIOR_ROUTING_KEY = "behavior.routing.key";

    // 死信交换机（处理消费失败的消息）
    public static final String BEHAVIOR_DLX_EXCHANGE = "behavior.dlx.exchange";
    // 死信队列
    public static final String BEHAVIOR_DLX_QUEUE = "behavior.dlx.queue";
    // 死信路由键
    public static final String BEHAVIOR_DLX_ROUTING_KEY = "behavior.dlx.routing.key";

    // 画像数据交换机（直连型）
    public static final String PROFILE_EXCHANGE = "profile.exchange";
    // 画像数据路由键
    public static final String PROFILE_ROUTING_KEY = "profile.routing.key";
}
