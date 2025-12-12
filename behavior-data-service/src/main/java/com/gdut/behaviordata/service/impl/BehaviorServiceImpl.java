package com.gdut.behaviordata.service.impl;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.enums.ResultCode;
import com.gdut.behaviordata.exception.BusinessException;
import com.gdut.behaviordata.mapper.BehaviorDetailMapper;
import com.gdut.behaviordata.rabbitmq.producer.BehaviorProducer;
import com.gdut.behaviordata.service.BehaviorService;
import com.gdut.behaviordata.util.DataDesensitizeUtils;
import com.gdut.behaviordata.util.RedisLockUtils;
import com.gdut.behaviordata.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class BehaviorServiceImpl implements BehaviorService {
    private final BehaviorDetailMapper behaviorDetailMapper;

    private final RedisLockUtils redisLockUtils;

    private final BehaviorProducer behaviorProducer;

    public BehaviorServiceImpl(BehaviorDetailMapper behaviorDetailMapper, RedisLockUtils redisLockUtils, BehaviorProducer behaviorProducer) {
        this.behaviorDetailMapper = behaviorDetailMapper;
        this.redisLockUtils = redisLockUtils;
        this.behaviorProducer = behaviorProducer;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveBehaviorDetail(BehaviorDetail behavior) {
        if (behavior == null) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        if (behavior.getId() == null) {
            behavior.setId(UUID.randomUUID().toString());
        }
        if (behavior.getCreateTime() == null) {
            behavior.setCreateTime(LocalDateTime.now());
        }
        if (behavior.getIsValid() == null) {
            behavior.setIsValid(1);
        }
        int count = behaviorDetailMapper.insertBehaviorDetailSingle(behavior);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveBehaviorDetailBatch(List<BehaviorDetail> behaviors) {
        if (behaviors == null || behaviors.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        behaviors.forEach(behavior -> {
            if (behavior.getId() == null) {
                behavior.setId(UUID.randomUUID().toString());
            }
            if (behavior.getCreateTime() == null) {
                behavior.setCreateTime(LocalDateTime.now());
            }
            if (behavior.getIsValid() == null) {
                behavior.setIsValid(1);
            }
        });
        int count = behaviorDetailMapper.insertBehaviorDetailMulti(behaviors);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeBehaviorDetail(String behaviorId) {
        if (Objects.isNull(behaviorId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        int count = behaviorDetailMapper.deleteBehaviorDetailSingle(behaviorId);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeBehaviorDetailBatch(List<String> behaviorIds) {
        if (behaviorIds == null || behaviorIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        int count = behaviorDetailMapper.deleteBehaviorDetailMulti(behaviorIds);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateBehaviorDetail(BehaviorDetail behavior) {
        if (behavior == null) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        int count = behaviorDetailMapper.updateBehaviorDetailSingle(behavior);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateBehaviorDetailBatch(List<BehaviorDetail> behaviors) {
        if (behaviors == null || behaviors.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        int count = behaviorDetailMapper.updateBehaviorDetailMulti(behaviors);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<BehaviorDetail> getBehaviorDetail(String behaviorId) {
        if (Objects.isNull(behaviorId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        BehaviorDetail behavior = behaviorDetailMapper.selectBehaviorDetailSingle(behaviorId);
        return behavior != null ? ResultUtils.success(behavior) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<List<BehaviorDetail>> getBehaviorDetailBatch(List<String> behaviorIds) {
        if (behaviorIds == null || behaviorIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        List<BehaviorDetail> behaviors = behaviorDetailMapper.selectBehaviorDetailMulti(behaviorIds);
        return behaviors != null ? ResultUtils.success(behaviors) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<Integer> collect(BehaviorDetail behavior) {
        // 1. 行为去重：获取分布式锁
        String lockKey = "behavior:lock:" + behavior.getUserId() + ":" + behavior.getTargetId();
        boolean lockSuccess = redisLockUtils.tryLock(lockKey, 1, 1, TimeUnit.MINUTES);
        if (!lockSuccess) {
            throw new BusinessException(ResultCode.REPEAT_SUBMIT);
        }
        try {
            // 2. 数据脱敏：设备号、IP信息脱敏
            if (behavior.getDeviceNo() != null) {
                behavior.setDeviceNo(DataDesensitizeUtils.desensitizeDeviceNo(behavior.getDeviceNo()));
            }
            if (behavior.getIpAddr() != null) {
                behavior.setIpAddr(DataDesensitizeUtils.desensitizeIp(behavior.getIpAddr()));
            }

            // 3. 投递RabbitMQ异步处理
            behaviorProducer.sendBehaviorMessage(behavior);
            // TODO AI服务收集当前数据
        } finally {
            // 释放锁
            redisLockUtils.unlock(lockKey);
        }
        return ResultUtils.success();
    }

    @Override
    public Result<List<BehaviorDetail>> getBehaviorsDetailByUserId(String userId) {
        if (Objects.isNull(userId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        List<BehaviorDetail> behaviors = behaviorDetailMapper.selectBehaviorDetailByUserId(userId);
        return behaviors != null ? ResultUtils.success(behaviors) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<BehaviorDetail> getBehaviorNewOneDetailByUserId(String userId) {
        if (Objects.isNull(userId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }
        BehaviorDetail behavior = behaviorDetailMapper.selectBehaviorDetailNewOneByUserId(userId);
        return behavior != null ? ResultUtils.success(behavior) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveBehaviorsBatch() {
        List<BehaviorDetail> behaviorList = new ArrayList<>();
        Map<String, Integer> itemType = new HashMap<>();
        itemType.put("article", 1);
        itemType.put("item", 2);
        itemType.put("image", 3);
        itemType.put("shortvideo", 4);
        itemType.put("video", 5);
        itemType.put("audio", 6);
        itemType.put("recipe", 7);
        String[] type = {"image", "item", "recipe", "shortvideo", "video", "audio", "article"};
        long baseTimeSec = System.currentTimeMillis() / 1000; // 基础时间戳（秒级）
        for (int i = 0; i < 1000; i++) {
            String userId = "user_id" + i; // 关联SysUser的id
            for (int j = 0; j < 100; j++) {
                // 1. 关联Item：targetId = item_id + j
                String targetId = "item_id" + j;
                // 2. 映射item_type到targetType
                String itemTypeStr = type[(i + j) % 7];
                Integer targetType = itemType.get(itemTypeStr);
                // 3. 映射行为类型：click→2，expose→1
                Integer behaviorType = (j % 10 == 0) ? 2 : 1;
                // 4. 时间戳转LocalDateTime
                LocalDateTime createTime = Instant.ofEpochSecond(baseTimeSec + j)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                BehaviorDetail behavior = BehaviorDetail.builder()
                        .id(UUID.randomUUID().toString().replace("-", ""))
                        .userId(userId)
                        .behaviorType(behaviorType)
                        .targetId(targetId)
                        .targetType(targetType)
                        .behaviorContent("")
                        .deviceNo("device_model" + (j % 2))
                        .ipAddr("ip")
                        .createTime(createTime)
                        .isValid(1)
                        .build();
                behaviorList.add(behavior);
            }
        }
        int count = this.batchInsertBehavior(behaviorList);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<List<String>> getTop10TargetIdByBehaviorCount() {
        List<Map<String, Object>> result = behaviorDetailMapper.selectTop10TargetIdByBehaviorCount();
        if (result == null || result.isEmpty()) {
            return ResultUtils.fail(ResultCode.DB_ERROR);
        }
        List<String> targetIds = result.stream().map(map -> map.get("target_id").toString()).collect(Collectors.toList());
        return ResultUtils.success(targetIds);
    }

    @Override
    public Result<List<String>> getTop10TargetIdByBehaviorAndUserId(String userId) {
        List<Map<String, Object>> result = behaviorDetailMapper.selectTop10TargetIdByBehaviorCountAndUserId(userId);
        if (result == null || result.isEmpty()) {
            return ResultUtils.fail(ResultCode.DB_ERROR);
        }
        List<String> targetIds = result.stream().map(map -> map.get("target_id").toString()).collect(Collectors.toList());
        return ResultUtils.success(targetIds);
    }

    public int batchInsertBehavior(List<BehaviorDetail> list) {
        int batchSize = 2000;
        int totalInsert = 0; // 统计实际插入的行数
        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<BehaviorDetail> batchList = list.subList(i, end);
            // 累加每批次插入的行数（需保证mapper返回实际插入的条数）
            int batchCount = behaviorDetailMapper.insertBehaviorDetailMulti(batchList);
            totalInsert += batchCount;
            // 若某批次插入条数为0，直接抛异常触发回滚（可选，根据业务）
            if (batchCount != batchList.size()) {
                throw new RuntimeException("批次插入失败，预期插入" + batchList.size() + "条，实际插入" + batchCount + "条");
            }
        }
        return totalInsert; // 返回实际插入总数
    }
}
