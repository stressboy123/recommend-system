package com.gdut.profile.recommend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.airec.model.v20181012.RecommendRequest;
import com.aliyuncs.airec.model.v20181012.RecommendResponse;
import com.aliyuncs.http.FormatType;
import com.gdut.profile.recommend.aiclient.AIClient;
import com.gdut.profile.recommend.entity.Item;
import com.gdut.profile.recommend.entity.RecommendResult;
import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.entity.UserTag;
import com.gdut.profile.recommend.enums.ResultCode;
import com.gdut.profile.recommend.exception.BusinessException;
import com.gdut.profile.recommend.fegin.BehaviorFeignClient;
import com.gdut.profile.recommend.fegin.ItemFeignClient;
import com.gdut.profile.recommend.mapper.RecommendResultMapper;
import com.gdut.profile.recommend.service.RecommendService;
import com.gdut.profile.recommend.service.UserProfileService;
import com.gdut.profile.recommend.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class RecommendServiceImpl implements RecommendService {
//    // 允许的推荐场景（1-首页，2-详情页，3-搜索页，4-个人中心，5-活动页，可按需扩展）
//    private static final List<Integer> ALLOWED_SCENES = List.of(1, 2, 3, 4, 5);

    // final 修饰依赖，构造器注入
    private final RecommendResultMapper recommendResultMapper;

    private final UserProfileService userProfileService;

    private final BehaviorFeignClient behaviorFeignClient;

    private final ItemFeignClient itemFeignClient;

    public RecommendServiceImpl(RecommendResultMapper recommendResultMapper, UserProfileService userProfileService, BehaviorFeignClient behaviorFeignClient, ItemFeignClient itemFeignClient) {
        this.recommendResultMapper = recommendResultMapper;
        this.userProfileService = userProfileService;
        this.behaviorFeignClient = behaviorFeignClient;
        this.itemFeignClient = itemFeignClient;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveRecommendResult(RecommendResult result) {
        // 1. 核心参数校验
        if (Objects.isNull(result.getUserId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "用户ID（userId）不能为空");
        }
        if (Objects.isNull(result.getScene())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "推荐场景（scene）不能为空");
        }
        if (Objects.isNull(result.getResultJson())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "推荐结果JSON（resultJson）不能为空");
        }

        // 2. 业务规则校验
        // 2.1 场景合法性
//        if (!ALLOWED_SCENES.contains(result.getScene())) {
//            throw new BusinessException(ResultCode.PARAM_ERROR,
//                    "推荐场景不合法，允许值：" + String.join(",", ALLOWED_SCENES.stream().map(String::valueOf).toArray(String[]::new)));
//        }
        // 2.2 JSON格式校验
        validateJsonField(result.getResultJson(), "推荐结果JSON（resultJson）");
        // 2.3 过期时间合理性（不能早于当前时间）
        if (Objects.nonNull(result.getExpireTime()) && result.getExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "过期时间不能早于当前时间");
        }

        // 3. 设置默认值
        result.setCreateTime(LocalDateTime.now()); // 自动填充创建时间
        result.setClickCount(Objects.isNull(result.getClickCount()) ? 0 : result.getClickCount()); // 点击量默认0
        // 未指定过期时间时，默认24小时过期
        if (Objects.isNull(result.getExpireTime())) {
            result.setExpireTime(LocalDateTime.now().plusHours(24));
        }

        int count = recommendResultMapper.insertRecommendResultSingle(result);
        return count > 0 ? ResultUtils.success(count) : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveRecommendResultBatch(List<RecommendResult> results) {
        if (results == null || results.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        results.forEach(result -> {
            // 批量参数校验
            if (Objects.isNull(result.getUserId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量推荐结果中存在用户ID为空的记录");
            }
            if (Objects.isNull(result.getScene())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "用户【" + result.getUserId() + "】的推荐场景不合法");
            }
            if (Objects.isNull(result.getResultJson())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "用户【" + result.getUserId() + "】的推荐结果JSON不能为空");
            }
            // JSON格式校验
            validateJsonField(result.getResultJson(), "用户【" + result.getUserId() + "】的推荐结果JSON");
            // 过期时间校验
            if (Objects.nonNull(result.getExpireTime()) && result.getExpireTime().isBefore(LocalDateTime.now())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "用户【" + result.getUserId() + "】的推荐结果过期时间不合法");
            }
            // 默认值设置
            result.setCreateTime(LocalDateTime.now());
            result.setClickCount(Objects.isNull(result.getClickCount()) ? 0 : result.getClickCount());
            if (Objects.isNull(result.getExpireTime())) {
                result.setExpireTime(LocalDateTime.now().plusHours(24));
            }
        });

        int count = recommendResultMapper.insertRecommendResultMulti(results);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeRecommendResult(String resultId) {
        if (Objects.isNull(resultId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        RecommendResult result = recommendResultMapper.selectRecommendResultSingle(resultId);
        if (result == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "推荐结果不存在，删除失败");
        }

        int count = recommendResultMapper.deleteRecommendResultSingle(resultId);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeRecommendResultBatch(List<String> resultIds) {
        if (resultIds == null || resultIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        int count = recommendResultMapper.deleteRecommendResultMulti(resultIds);
        return count > 0 ? ResultUtils.success(count) : ResultUtils.failWithCustomMsg(ResultCode.DATA_NOT_FOUND, "无有效推荐结果可删除");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateRecommendResult(RecommendResult result) {
        // 1. ID校验
        if (Objects.isNull(result.getId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "推荐结果ID不能为空");
        }

        // 2. 结果存在性校验
        RecommendResult existing = recommendResultMapper.selectRecommendResultSingle(result.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "推荐结果不存在，更新失败");
        }

        // 3. 业务规则校验
        // 3.1 仅允许更新点击量、过期时间（推荐结果内容不允许修改）
        RecommendResult updateResult = RecommendResult.builder()
                .id(result.getId())
                .clickCount(result.getClickCount())
                .expireTime(result.getExpireTime())
                .build();
        // 3.2 过期时间合理性
        if (Objects.nonNull(updateResult.getExpireTime()) && updateResult.getExpireTime().isBefore(existing.getCreateTime())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "过期时间不能早于创建时间");
        }
        // 3.3 点击量非负
        if (Objects.nonNull(updateResult.getClickCount()) && updateResult.getClickCount() < 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "点击量不能为负数");
        }

        int count = recommendResultMapper.updateRecommendResultSingle(updateResult);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateRecommendResultBatch(List<RecommendResult> results) {
        if (results == null || results.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        results.forEach(result -> {
            if (Objects.isNull(result.getId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量更新中存在推荐结果ID为空的记录");
            }
            if (recommendResultMapper.selectRecommendResultSingle(result.getId()) == null) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND, "推荐结果ID【" + result.getId() + "】不存在");
            }
            // 点击量非负校验
            if (Objects.nonNull(result.getClickCount()) && result.getClickCount() < 0) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "推荐结果ID【" + result.getId() + "】的点击量不能为负数");
            }
            // 过期时间合理性
            if (Objects.nonNull(result.getExpireTime())) {
                RecommendResult existing = recommendResultMapper.selectRecommendResultSingle(result.getId());
                if (result.getExpireTime().isBefore(existing.getCreateTime())) {
                    throw new BusinessException(ResultCode.PARAM_ERROR, "推荐结果ID【" + result.getId() + "】的过期时间不合法");
                }
            }
        });

        int count = recommendResultMapper.updateRecommendResultMulti(results);
        return ResultUtils.success(count);
    }

    @Override
    public Result<RecommendResult> getRecommendResult(String resultId) {
        if (Objects.isNull(resultId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        RecommendResult result = recommendResultMapper.selectRecommendResultSingle(resultId);
        return result != null ? ResultUtils.success(result) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<List<RecommendResult>> getRecommendResultBatch(List<String> resultIds) {
        if (resultIds == null || resultIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        List<RecommendResult> results = recommendResultMapper.selectRecommendResultMulti(resultIds);

        return ResultUtils.success(results);
    }

    @Override
    public Result<RecommendResult> getPersonRecommend(String userId) {
        // TODO 1.获取规则
        // 2.调用AI服务（如果调不了降级查数据库）
        Result<RecommendResult> aiResult = doAIRecommendService(userId, 10, 1);
        if (aiResult.isSuccess()) {
            return aiResult;
        }
        // 1. 获取当前用户标签
        Result<UserTag> userTagResult = userProfileService.getUserTagByUserId(userId);
        if (!userTagResult.isSuccess()) {
            return ResultUtils.failWithOnlyMsg(userTagResult.getMsg());
        }
        UserTag userTag = userTagResult.getData();
        // 2. 解析用户标签和权重
        JSONArray userTagList = JSONArray.parseArray(userTag.getTagJson());
        JSONObject userTagWeightMap = JSONObject.parseObject(userTag.getTagWeightJson());
        // 过滤掉权重≤0的标签（负向行为标签，不参与推荐）
        Map<String, Double> validTagWeightMap = new HashMap<>();
        for (Object tagObj : userTagList) {
            String tag = tagObj.toString().trim();
            double weight = userTagWeightMap.getDoubleValue(tag);
            if (weight > 0) { // 仅保留正向权重标签
                validTagWeightMap.put(tag, weight);
            }
        }
        if (validTagWeightMap.isEmpty()) {
            // 无有效正向标签，返回默认推荐
            return this.getHotRecommend(userId);
        }
        // 3. 查询所有匹配用户标签的Item
        List<String> userValidTags = new ArrayList<>(validTagWeightMap.keySet());
        Result<List<Item>> itemResult = itemFeignClient.selectByTags(userValidTags);
        if (!itemResult.isSuccess() && itemResult.getData().isEmpty()) {
            // 无匹配Item，返回默认推荐
            return this.getHotRecommend(userId);
        }
        // 4. 计算每个Item的推荐权重
        List<Item> matchItems = itemResult.getData();
        Map<Item, Double> itemWeightMap = new HashMap<>();
        for (Item item : matchItems) {
            String itemTagsStr = item.getTags();
            String categoryPath = item.getCategoryPath();

            if (!StringUtils.hasText(itemTagsStr)) {
                continue;
            }

            // 累加该Item所有标签的推荐权重
            double totalRecommendWeight = 0.0;
            String[] itemTags = itemTagsStr.split(",");
            for (String itemTag : itemTags) {
                itemTag = itemTag.trim();
                if (!validTagWeightMap.containsKey(itemTag)) {
                    continue;
                }

                // 推荐权重 = 用户标签权重 × 关联度系数
                double userTagWeight = validTagWeightMap.get(itemTag);
                double relevance = getRelevanceCoefficient(itemTag, categoryPath, userTagList);
                totalRecommendWeight += userTagWeight * relevance;
            }

            if (totalRecommendWeight > 0) {
                itemWeightMap.put(item, totalRecommendWeight); // 直接关联Item对象
            }
        }
        // 5. 按权重降序排序，取前10条
        List<Item> recommendItems = itemWeightMap.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue())) // 降序
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        RecommendResult result = RecommendResult.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .userId(userId)
                .scene(1)
                .resultJson(JSON.toJSONString(recommendItems))
                .expireTime(LocalDateTime.now().plusDays(7))
                .createTime(LocalDateTime.now())
                .build();
        return ResultUtils.success(result);
    }

    @Override
    public Result<RecommendResult> getHotRecommend(String userId) {
        // TODO 1.获取规则
        // 2.调用AI服务（如果调不了降级查数据库）
        Result<RecommendResult> aiResult = doAIRecommendService(userId, 10, 2);
        if (aiResult.isSuccess()) {
            return aiResult;
        }
        // 远程查询行为服务，统计行为最多的前10个itemId（总行为次数）
        Result<List<String>> behaviorResult = behaviorFeignClient.getTop10TargetIdByBehaviorCount();
        if (!behaviorResult.isSuccess()) {
            return ResultUtils.failWithOnlyMsg(behaviorResult.getMsg());
        }
        List<String> itemIds = behaviorResult.getData();
        // 远程查询物品服务，查询物品标签
        Result<List<Item>> itemResult = itemFeignClient.getItemByIds(itemIds);
        if (!itemResult.isSuccess()) {
            return ResultUtils.failWithOnlyMsg(itemResult.getMsg());
        }
        List<Item> items = itemResult.getData();
        RecommendResult result = RecommendResult.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .userId(userId)
                .scene(2)
                .resultJson(JSON.toJSONString(items))
                .expireTime(LocalDateTime.now().plusDays(7))
                .createTime(LocalDateTime.now())
                .build();

        return ResultUtils.success(result);
    }

    @Override
    public Result<RecommendResult> getRelatedRecommend(String userId) {
        // TODO 1.获取规则
        // 2.调用AI服务（如果调不了降级查数据库）
        Result<RecommendResult> aiResult = doAIRecommendService(userId, 10, 3);
        if (aiResult.isSuccess()) {
            return aiResult;
        }
        // 统计当前用户所有行为信息，选取其中行为最多的前10条targetId
        Result<List<String>> behaviorResult = behaviorFeignClient.getTop10TargetIdByBehaviorAndUserId(userId);
        if (!behaviorResult.isSuccess()) {
            return ResultUtils.failWithOnlyMsg(behaviorResult.getMsg());
        }
        List<String> itemIds = behaviorResult.getData();
        // 远程查询物品服务，查询物品标签
        Result<List<Item>> itemResult = itemFeignClient.getItemByIds(itemIds);
        if (!itemResult.isSuccess()) {
            return ResultUtils.failWithOnlyMsg(itemResult.getMsg());
        }
        List<Item> items = itemResult.getData();
        RecommendResult result = RecommendResult.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .userId(userId)
                .scene(3)
                .resultJson(JSON.toJSONString(items))
                .expireTime(LocalDateTime.now().plusDays(7))
                .createTime(LocalDateTime.now())
                .build();

        return ResultUtils.success(result);
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

    public Result<RecommendResult> doAIRecommendService(String userId, int returnCount, int scene) {
        Map<String, Integer> itemType = new HashMap<>();
        itemType.put("article", 1);
        itemType.put("item", 2);
        itemType.put("image", 3);
        itemType.put("shortvideo", 4);
        itemType.put("video", 5);
        itemType.put("audio", 6);
        itemType.put("recipe", 7);
        DefaultAcsClient client = AIClient.getAcsClient();
        RecommendRequest request = new RecommendRequest();
        request.setInstanceId(AIClient.getInstanceId());
        request.setUserId(userId);
        request.setReturnCount(returnCount);
        request.setSceneId(String.valueOf(scene));
        request.setAcceptFormat(FormatType.JSON);
        try {
            RecommendResponse response = client.getAcsResponse(request);
            List<Item> items = new ArrayList<>();
            for (RecommendResponse.ResultItem resultItem : response.getResult()) {
                Item item = new Item();
                item.setId(resultItem.getItemId());
                item.setItemType(itemType.get(resultItem.getItemType()));
                item.setTags(resultItem.getMatchInfo());
                item.setCategoryPath(resultItem.getMatchInfo());
                items.add(item);
            }
            return ResultUtils.success(RecommendResult.builder()
                    .id(UUID.randomUUID().toString().replace("-", ""))
                    .userId(userId)
                    .scene(scene)
                    .resultJson(JSON.toJSONString(items))
                    .expireTime(LocalDateTime.now().plusDays(7))
                    .createTime(LocalDateTime.now())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.failWithOnlyMsg(e.getMessage());
        }
    }


    /**
     * 辅助方法：校验JSON字符串格式
     */
    private void validateJsonField(String jsonStr, String fieldDesc) {
        try {
            JSON.parse(jsonStr);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.PARAM_ERROR, fieldDesc + "格式不合法");
        }
    }
}
