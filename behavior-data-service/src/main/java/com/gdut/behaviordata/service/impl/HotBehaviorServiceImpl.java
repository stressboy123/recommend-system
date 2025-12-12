package com.gdut.behaviordata.service.impl;

import com.gdut.behaviordata.entity.HotBehavior;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.enums.ResultCode;
import com.gdut.behaviordata.mapper.HotBehaviorMapper;
import com.gdut.behaviordata.service.HotBehaviorService;
import com.gdut.behaviordata.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class HotBehaviorServiceImpl implements HotBehaviorService {
    private final HotBehaviorMapper hotBehaviorMapper;

    public HotBehaviorServiceImpl(HotBehaviorMapper hotBehaviorMapper) {
        this.hotBehaviorMapper = hotBehaviorMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveHotBehavior(HotBehavior behavior) {
        // 补充默认值：更新时间（新增时默认当前时间）
        behavior.setUpdateTime(LocalDateTime.now());
        // 计数字段默认值（避免null）
        if (behavior.getBehaviorCount() == null) {
            behavior.setBehaviorCount(0);
        }
        if (behavior.getBrowseCount() == null) {
            behavior.setBrowseCount(0);
        }
        if (behavior.getClickCount() == null) {
            behavior.setClickCount(0);
        }
        if (behavior.getCollectCount() == null) {
            behavior.setCollectCount(0);
        }

        int count = hotBehaviorMapper.insertHotBehaviorDetailSingle(behavior);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveHotBehaviorBatch(List<HotBehavior> behaviors) {
        if (behaviors == null || behaviors.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        // 批量设置默认值
        behaviors.forEach(behavior -> {
            behavior.setUpdateTime(LocalDateTime.now());
            if (behavior.getBehaviorCount() == null) {
                behavior.setBehaviorCount(0);
            }
            if (behavior.getBrowseCount() == null) {
                behavior.setBrowseCount(0);
            }
            if (behavior.getClickCount() == null) {
                behavior.setClickCount(0);
            }
            if (behavior.getCollectCount() == null) {
                behavior.setCollectCount(0);
            }
        });

        int count = hotBehaviorMapper.insertHotBehaviorDetailMulti(behaviors);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeHotBehavior(String behaviorId) {
        if (Objects.isNull(behaviorId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        int count = hotBehaviorMapper.deleteHotBehaviorDetailSingle(behaviorId);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeHotBehaviorBatch(List<String> behaviorIds) {
        if (behaviorIds == null || behaviorIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        int count = hotBehaviorMapper.deleteHotBehaviorDetailMulti(behaviorIds);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateHotBehavior(HotBehavior behavior) {
        // 校验ID必填
        if (behavior.getId() == null) {
            return ResultUtils.failWithCustomMsg(ResultCode.PARAM_MISS, "更新ID不能为空");
        }
        // 更新时间强制刷新为当前时间
        behavior.setUpdateTime(LocalDateTime.now());

        int count = hotBehaviorMapper.updateHotBehaviorDetailSingle(behavior);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateHotBehaviorBatch(List<HotBehavior> behaviors) {
        if (behaviors == null || behaviors.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        // 校验批量更新的ID是否为空
        boolean hasInvalidId = behaviors.stream()
                .anyMatch(behavior -> behavior.getId() == null);
        if (hasInvalidId) {
            return ResultUtils.failWithCustomMsg(ResultCode.PARAM_ERROR, "更新数据中存在ID为空的记录");
        }

        // 批量刷新更新时间
        behaviors.forEach(behavior -> behavior.setUpdateTime(LocalDateTime.now()));

        int count = hotBehaviorMapper.updateHotBehaviorDetailMulti(behaviors);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<HotBehavior> getHotBehavior(String behaviorId) {
        if (Objects.isNull(behaviorId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        HotBehavior behavior = hotBehaviorMapper.selectHotBehaviorDetailSingle(behaviorId);
        return behavior != null ? ResultUtils.success(behavior) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<List<HotBehavior>> getHotBehaviorBatch(List<String> behaviorIds) {
        if (behaviorIds == null || behaviorIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        List<HotBehavior> behaviors = hotBehaviorMapper.selectHotBehaviorDetailMulti(behaviorIds);
        return ResultUtils.success(behaviors);
    }
}
