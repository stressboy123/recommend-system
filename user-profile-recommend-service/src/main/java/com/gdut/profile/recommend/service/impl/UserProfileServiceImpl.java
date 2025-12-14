package com.gdut.profile.recommend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gdut.profile.recommend.entity.BehaviorDetail;
import com.gdut.profile.recommend.entity.Item;
import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.entity.UserTag;
import com.gdut.profile.recommend.enums.ResultCode;
import com.gdut.profile.recommend.exception.BusinessException;
import com.gdut.profile.recommend.fegin.BehaviorFeignClient;
import com.gdut.profile.recommend.fegin.ItemFeignClient;
import com.gdut.profile.recommend.fegin.SystemFeignClient;
import com.gdut.profile.recommend.mapper.UserTagMapper;
import com.gdut.profile.recommend.service.UserProfileService;
import com.gdut.profile.recommend.util.ResultUtils;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserTagMapper userTagMapper;

    private final BehaviorFeignClient behaviorFeignClient;

    private final SystemFeignClient systemFeignClient;

    private final ItemFeignClient itemFeignClient;

    public UserProfileServiceImpl(UserTagMapper userTagMapper, BehaviorFeignClient behaviorFeignClient, SystemFeignClient systemFeignClient, ItemFeignClient itemFeignClient) {
        this.userTagMapper = userTagMapper;
        this.behaviorFeignClient = behaviorFeignClient;
        this.systemFeignClient = systemFeignClient;
        this.itemFeignClient = itemFeignClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveUserTag(UserTag tag) {
        if (tag == null) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        // 1. 业务规则校验
        validateJsonField(tag.getTagJson(), "标签JSON（tagJson）");
        validateJsonField(tag.getTagWeightJson(), "标签权重JSON（tagWeightJson）");

        // 2. 设置默认值
        if (tag.getId() == null) {
            tag.setId(UUID.randomUUID().toString());
        }
        if (tag.getUpdateTime() == null) {
            tag.setUpdateTime(LocalDateTime.now());
        }
        if (tag.getUpdateType() == null) {
            tag.setUpdateType(1);
        }

        int count = userTagMapper.insertUserTagSingle(tag);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveUserTagBatch(List<UserTag> tags) {
        if (tags == null || tags.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        tags.forEach(tag -> {
            // 1. 业务规则校验
            validateJsonField(tag.getTagJson(), "标签JSON（tagJson）");
            validateJsonField(tag.getTagWeightJson(), "标签权重JSON（tagWeightJson）");

            // 2. 设置默认值
            if (tag.getId() == null) {
                tag.setId(UUID.randomUUID().toString());
            }
            if (tag.getUpdateTime() == null) {
                tag.setUpdateTime(LocalDateTime.now());
            }
            if (tag.getUpdateType() == null) {
                tag.setUpdateType(1);
            }
        });

        int count = userTagMapper.insertUserTagMulti(tags);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeUserTag(String tagId) {
        if (Objects.isNull(tagId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        int count = userTagMapper.deleteUserTagSingle(tagId);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeUserTagBatch(List<String> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        int count = userTagMapper.deleteUserTagMulti(tagIds);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateUserTag(UserTag tag) {
        if (tag == null) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        // 1. 业务规则校验
        validateJsonField(tag.getTagJson(), "标签JSON（tagJson）");
        validateJsonField(tag.getTagWeightJson(), "标签权重JSON（tagWeightJson）");

        // 2. 设置默认值
        if (tag.getId() == null) {
            tag.setId(UUID.randomUUID().toString());
        }
        if (tag.getUpdateType() == null) {
            tag.setUpdateType(1);
        }
        tag.setUpdateTime(LocalDateTime.now());

        int count = userTagMapper.updateUserTagSingle(tag);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateUserTagBatch(List<UserTag> tags) {
        if (tags == null || tags.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        tags.forEach(tag -> {
            // 1. 业务规则校验
            validateJsonField(tag.getTagJson(), "标签JSON（tagJson）");
            validateJsonField(tag.getTagWeightJson(), "标签权重JSON（tagWeightJson）");

            // 2. 设置默认值
            if (tag.getId() == null) {
                tag.setId(UUID.randomUUID().toString());
            }
            if (tag.getUpdateTime() == null) {
                tag.setUpdateTime(LocalDateTime.now());
            }
            if (tag.getUpdateType() == null) {
                tag.setUpdateType(1);
            }
        });

        int count = userTagMapper.updateUserTagMulti(tags);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<UserTag> getUserTag(String tagId) {
        if (Objects.isNull(tagId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        UserTag tag = userTagMapper.selectUserTagSingle(tagId);
        return tag != null ? ResultUtils.success(tag) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<List<UserTag>> getUserTagBatch(List<String> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        List<UserTag> tags = userTagMapper.selectUserTagMulti(tagIds);
        return tags != null ? ResultUtils.success(tags) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<Integer> updateUserProfileFull(String userId) {
        // 1. 全量行为拉取（近30天有效行为）
        Result<List<BehaviorDetail>> result = behaviorFeignClient.getBehaviorsByUserId(userId);
        if (!result.isSuccess()) {
            return ResultUtils.failWithOnlyMsg("当前用户没有最新行为信息");
        }
        List<BehaviorDetail> behaviorDetails = result.getData();
        if (behaviorDetails.isEmpty()) {
            return ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
        }

        // 2. 遍历有效行为，逐行为累积计算标签权重（全量更新从空开始累积）
        String emptyTagJson = null;
        String emptyWeightJson = null;
        for (BehaviorDetail behaviorDetail : behaviorDetails) {
            // 每次基于当前累积的标签/权重更新
            Map<String, Object> calculateResult = calculateNewTags(
                    emptyTagJson,    // 传入当前累积的标签JSON
                    emptyWeightJson, // 传入当前累积的权重JSON
                    behaviorDetail
            );
            // 更新累积的标签和权重
            emptyTagJson = (String) calculateResult.get("tagJson");
            emptyWeightJson = (String) calculateResult.get("weightJson");
        }

        // 3. 画像存储（新增/覆盖用户标签）
        UserTag userTag = userTagMapper.selectUserTagByUserId(userId);
        if (userTag == null) {
            // 无历史标签，新增
            userTag = new UserTag();
            userTag.setId(UUID.randomUUID().toString());
            userTag.setUserId(userId);
            userTag.setTagJson(emptyTagJson);
            userTag.setTagWeightJson(emptyWeightJson);
            userTag.setUpdateTime(LocalDateTime.now());
            userTag.setUpdateType(0);
            userTagMapper.insertUserTagSingle(userTag);
        } else {
            // 有历史标签，更新
            userTag.setTagJson(emptyTagJson);
            userTag.setTagWeightJson(emptyWeightJson);
            userTag.setUpdateTime(LocalDateTime.now());
            userTag.setUpdateType(0);
            userTagMapper.updateUserTagSingle(userTag);
        }
        // TODO 5. 缓存更新
        return ResultUtils.success();
    }

    @Override
    public Result<Integer> updateUserProfileIncrementally(String userId) {
        // 1. 查询历史标签
        UserTag userTag = userTagMapper.selectUserTagByUserId(userId);
        if (userTag == null) {
            return this.updateUserProfileFull(userId);
        }
        // 2. 行为数据获取最新一条
        Result<BehaviorDetail> result = behaviorFeignClient.getBehaviorNewOneByUserId(userId);
        if (!result.isSuccess()) {
            return ResultUtils.failWithOnlyMsg("当前用户没有最新行为信息");
        }
        // 3. 标签计算
        BehaviorDetail latestBehavior = result.getData();
        String oldTagJson = userTag.getTagJson();
        String oldWeightJson = userTag.getTagWeightJson();
        // 计算新标签和权重
        Map<String, Object> calculateResult = calculateNewTags(
                oldTagJson,
                oldWeightJson,
                latestBehavior
        );
        String newTagJson = (String) calculateResult.get("tagJson");
        String newWeightJson = (String) calculateResult.get("weightJson");
        // 4. 画像存储
        UserTag updateTag = new UserTag();
        updateTag.setId(UUID.randomUUID().toString());
        updateTag.setUserId(userId);
        updateTag.setTagJson(newTagJson);
        updateTag.setTagWeightJson(newWeightJson);
        updateTag.setUpdateTime(LocalDateTime.now());
        updateTag.setUpdateType(1);
        userTagMapper.updateUserTagSingle(updateTag);
        // TODO 5. 缓存更新
        return ResultUtils.success();
    }

    @Async("userProfileExecutor") // 绑定自定义线程池
    @Override
    public void updateUserProfileFullAsync(String userId) {
        try {
            updateUserProfileFull(userId);
            XxlJobHelper.log("用户{}全量更新完成", userId);
        } catch (Exception e) {
            XxlJobHelper.log("用户{}全量更新失败：{}", userId, e.getMessage());
            // 抛出异常，让上层感知失败
            throw new RuntimeException("用户" + userId + "更新失败", e);
        }
    }

    @Override
    public Result<List<String>> listNeedFullUpdateUserIds() {
        // 1. 查询所有用户id
        Result<List<String>> result = systemFeignClient.getAllSysUserIds();
        if (!result.isSuccess() || result.getData() == null) {
            return ResultUtils.failWithOnlyMsg("当前没有用户");
        }
        List<String> allUserId = result.getData();
        // 2. 查询所有用户标签
        List<UserTag> userTags = userTagMapper.selectAllUserTag();
        if (userTags == null) {
            return ResultUtils.success(allUserId);
        }
        // 3. 初始化需要更新的用户ID集合
        List<String> updateIds = new ArrayList<>();
        Set<String> taggedUserIdSet = new HashSet<>();
        // 计算一天前的时间（用于判断标签是否超过1天未更新）
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        // 4. 遍历用户标签，筛选出更新时间超过1天的用户
        for (UserTag tag : userTags) {
            String userId = tag.getUserId();
            taggedUserIdSet.add(userId);
            LocalDateTime updateTime = tag.getUpdateTime();
            // 无更新时间/更新时间超过1天
            if (updateTime == null || updateTime.isBefore(oneDayAgo)) {
                updateIds.add(userId);
            }
        }
        // 5. 筛选出无用户标签的用户（所有用户 - 有标签的用户）
        for (String userId : allUserId) {
            if (!taggedUserIdSet.contains(userId)) {
                updateIds.add(userId);
            }
        }
        // 6. 返回去重后的结果（避免同一用户既无标签又超时的重复添加）
        List<String> finalUpdateIds = new ArrayList<>(new HashSet<>(updateIds));
        return ResultUtils.success(finalUpdateIds);
    }

    @Override
    public Result<Integer> saveTagBatch() {
        List<UserTag> userTagList = new ArrayList<>();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        // 遍历1000个用户（user_id0 ~ user_id999）
        for (int i = 0; i < 1000; i++) {
            String userId = "user_id" + i; // 关联SysUser的id
            JSONArray tagList = new JSONArray(); // 用户标签集合
            JSONObject weightMap = new JSONObject(); // 标签权重映射

            // 每个用户随机关联3~6个Item标签（tag0~tag9）
            int tagCount = 3 + random.nextInt(4); // 每个用户3~6个标签
            for (int t = 0; t < tagCount; t++) {
                String itemTag = "tag" + (i + t) % 10; // 标签与Item的tags（tag0~tag9）对齐
                double baseWeight = 0.0;

                // 模拟用户对该标签对应Item的行为，计算基础权重
                int behaviorType = 1 + random.nextInt(7); // 随机行为类型1~7
                switch (behaviorType) {
                    case 1: // 浏览 → 基础权重1.0
                        baseWeight = 1.0;
                        break;
                    case 2: // 点击 → 基础权重5.0
                        baseWeight = 5.0;
                        break;
                    case 3: // 收藏 → 基础权重10.0
                        baseWeight = 10.0;
                        break;
                    case 4: // 分享 → 基础权重12.0
                        baseWeight = 12.0;
                        break;
                    case 5: // 取消收藏 → 负向权重-10.0
                        baseWeight = -10.0;
                        break;
                    case 6: // 停留（模拟停留30秒）→ 2 + 30/60 = 2.5
                        baseWeight = 2.5;
                        break;
                    case 7: // 不喜欢 → 负向权重-8.0
                        baseWeight = -8.0;
                        break;
                }

                // 计算关联度系数（贴合getRelevanceCoefficient）
                double relevance = tagList.contains(itemTag) ? 1.5 : 0.5;
                double finalWeight = baseWeight * relevance;

                // 累加权重（若标签已存在，叠加权重）
                if (weightMap.containsKey(itemTag)) {
                    finalWeight += weightMap.getDoubleValue(itemTag);
                }
                weightMap.put(itemTag, finalWeight);

                // 标签去重加入集合
                if (!tagList.contains(itemTag)) {
                    tagList.add(itemTag);
                }
            }

            // 组装UserTag对象
            UserTag userTag = UserTag.builder()
                    .id(UUID.randomUUID().toString().replace("-", "")) // 主键
                    .userId(userId) // 关联用户ID
                    .tagJson(tagList.toJSONString()) // 标签JSON（["tag0","tag1"...]）
                    .tagWeightJson(weightMap.toJSONString()) // 权重JSON（{"tag0":5.0,"tag1":1.0...}）
                    .updateTime(now) // 更新时间
                    .updateType(random.nextInt(2)) // 0=定时更新/1=触发更新，随机
                    .build();

            userTagList.add(userTag);
        }
        int count = userTagMapper.insertUserTagMulti(userTagList);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<UserTag> getUserTagByUserId(String userId) {
        UserTag userTag = userTagMapper.selectUserTagByUserId(userId);
        return userTag != null ? ResultUtils.success(userTag) : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    /**
     * 标签计算核心方法
     * @param oldTagJson 旧标签JSON（格式：{"tag1":1, "tag2":1}）
     * @param oldWeightJson 旧标签权重JSON（格式：{"tag1":5.0, "tag2":3.0}）
     * @param behavior 行为详情
     * @return 新标签和权重JSON
     */
    private Map<String, Object> calculateNewTags(String oldTagJson, String oldWeightJson, BehaviorDetail behavior) {
        // 1. 初始化标签集合（JSONArray）和权重映射（JSONObject）
        JSONArray tagList = new JSONArray();
        if (oldTagJson != null && !oldTagJson.trim().isEmpty()) {
            tagList = JSONArray.parseArray(oldTagJson);
        }

        JSONObject weightMap = new JSONObject();
        if (oldWeightJson != null && !oldWeightJson.trim().isEmpty()) {
            weightMap = JSONObject.parseObject(oldWeightJson);
        }

        // 解析行为数据
        Integer behaviorType = behavior.getBehaviorType(); // 业务行为类型（1-浏览、2-点击等）
        String targetId = behavior.getTargetId();          // 物品ID（用于查询物品标签）

        // 2. 获取行为对应的基础权重
        double baseWeight = getBaseWeightByBehaviorType(behaviorType, behavior);
        if (baseWeight == 0) {
            // 未知行为类型，不更新标签
            Map<String, Object> result = new HashMap<>();
            result.put("tagJson", tagList.toJSONString());
            result.put("weightJson", weightMap.toJSONString());
            return result;
        }

        // 3. 通过StaticItemData获取物品标签和分类路径
        // 远程获取物品信息
        Result<Item> resultClient = itemFeignClient.getItemById(targetId);
        if (!resultClient.isSuccess() || resultClient.getData() == null) {
            // 数据中无该物品，不更新标签
            Map<String, Object> result = new HashMap<>();
            result.put("tagJson", tagList.toJSONString());
            result.put("weightJson", weightMap.toJSONString());
            return result;
        }

        // 提取物品标签和分类路径（对应demo中PushItemDistribute等类的字段）
        Item data = resultClient.getData();
        String itemTagsStr = data.getTags();
        String categoryPath = data.getCategoryPath();

        // 校验标签是否有效
        if (itemTagsStr == null || itemTagsStr.trim().isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("tagJson", tagList.toJSONString());
            result.put("weightJson", weightMap.toJSONString());
            return result;
        }
        String[] itemTags = itemTagsStr.split(","); // 拆分标签数组（兼容多标签格式）

        // 4. 计算每个物品标签对用户标签的影响
        for (String itemTag : itemTags) {
            itemTag = itemTag.trim();
            if (itemTag.isEmpty()) {
                continue;
            }

            // 计算关联度系数（基于用户现有标签和物品分类）
            double relevance = getRelevanceCoefficient(itemTag, categoryPath, tagList);

            // 计算权重变化值
            double weightChange = baseWeight * relevance;

            // 更新权重映射（首次出现则初始化为0后累加）
            double currentWeight = weightMap.getDoubleValue(itemTag);
            double newWeight = currentWeight + weightChange;
            weightMap.put(itemTag, newWeight);

            // 将标签加入用户标签集合（去重）
            if (!tagList.contains(itemTag)) {
                tagList.add(itemTag);
            }
        }

        // 5. 组装返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("tagJson", tagList.toJSONString());
        result.put("weightJson", weightMap.toJSONString());
        return result;
    }

    /**
     * 获取行为类型对应的基础权重系数
     */
    private double getBaseWeightByBehaviorType(Integer behaviorType, BehaviorDetail behavior) {
        if (behaviorType == null) {
            return 0;
        }
        switch (behaviorType) {
            case 1: // 浏览（对应demo的expose）
                return 1.0;
            case 2: // 点击（对应demo的click）
                return 5.0;
            case 3: // 收藏（扩展行为）
                return 10.0;
            case 4: // 分享（扩展行为）
                return 12.0;
            case 5: // 取消收藏（负向行为）
                return -10.0;
            case 6: // 停留（扩展行为，需从behaviorContent取时长）
                // 假设behaviorContent存储停留秒数（如"60"）
                String stayTimeStr = behavior.getBehaviorContent();
                int staySeconds = 0;
                if (stayTimeStr != null && !stayTimeStr.trim().isEmpty()) {
                    try {
                        staySeconds = Integer.parseInt(stayTimeStr.trim());
                    } catch (NumberFormatException e) {
                        staySeconds = 0;
                    }
                }
                return 2.0 + (staySeconds / 60.0); // 基础2分 + 每60秒加1分
            case 7: // 不喜欢（对应demo的dislike）
                return -8.0;
            default: // 未知行为
                return 0;
        }
    }

    /**
     * 计算物品标签与用户标签的关联度系数
     */
    private double getRelevanceCoefficient(String itemTag, String categoryPath, JSONArray userTags) {
        // 1. 若用户已有该标签，强关联（系数1.5）
        if (userTags.contains(itemTag)) {
            return 1.5;
        }

        // 2. 若物品分类路径包含用户已有标签，中关联（系数1.2）
        if (categoryPath != null && !categoryPath.trim().isEmpty()) {
            String[] categories = categoryPath.split("_");
            for (String category : categories) {
                if (userTags.contains(category.trim())) {
                    return 1.2;
                }
            }
        }

        // 3. 无匹配，弱关联（系数0.5）
        return 0.5;
    }

    /**
     * 辅助方法：校验JSON字符串格式
     */
    private void validateJsonField(String jsonStr, String fieldDesc) {
        if (Objects.isNull(jsonStr)) {
            return; // 允许为空，如需必填可抛异常
        }
        try {
            JSON.parse(jsonStr); // FastJSON解析校验
        } catch (Exception e) {
            throw new BusinessException(ResultCode.PARAM_ERROR, fieldDesc + "格式不合法");
        }
    }
}
