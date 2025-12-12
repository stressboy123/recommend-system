package com.gdut.profile.recommend.service;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface AIService {
    /**
     * 推送单个用户的详细信息到 AIRec 的user表，用于初始化或更新单个用户数据。
     */
    void pushOneUser();

    /**
     * 批量推送 1000 个用户的基础信息到 AIRec 的user表，用于生成测试用的用户数据集。
     */
    void pushRecommendUser();

    /**
     * 推送单个内容类物品（如图片、文章）的详细信息到 AIRec 的item表，用于初始化或更新单个内容物品。
     */
    void pushOneContentItem();

    /**
     * 针对内容行业（如图文、视频等），批量推送 1000 条物品数据到 AIRec 的item表，用于测试物品质量分布功能。
     */
    void pusDistributeContent();

    /**
     * 推送单个通用物品（如商品、新闻）的信息到 AIRec 的item表，适用于非特定行业的物品初始化。
     */
    void pushOneGeneralItem();

    /**
     * 针对新闻行业，批量推送 1000 条物品数据到item表，用于测试新闻行业的物品质量分布。
     */
    void pusDistributeItem();

    /**
     * 推送单个新闻物品的详细信息到item表，用于初始化或更新单条新闻数据。
     */
    void pushOneNewsItem();

    /**
     * 批量推送新闻行业的物品数据（共 1000 条）到item表，用于测试新闻行业的物品质量分布。
     */
    void pusDistributeNews();

    /**
     * 批量推送历史用户行为数据（如曝光、点击）到 AIRec 的behavior表，为推荐模型提供用户交互数据。
     */
    void pushRecommendBhv();

    /**
     * 模拟实时用户行为触发，推送特定用户的实时行为数据（如点击、不喜欢）到behavior表，用于测试实时推荐反馈。
     */
    void pushRealTimeRecommendBhv();

    /**
     * 推送特定用户的实时更新数据到user表，用于模拟用户属性变化触发的实时推荐调整。
     */
    void pushRealTimeOneUser();
}
