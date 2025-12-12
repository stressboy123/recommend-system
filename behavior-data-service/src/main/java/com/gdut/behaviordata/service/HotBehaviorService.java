package com.gdut.behaviordata.service;

import com.gdut.behaviordata.entity.HotBehavior;
import com.gdut.behaviordata.entity.Result;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface HotBehaviorService {
    /**
     * 插入单条热门行为
     */
    Result<Integer> saveHotBehavior(HotBehavior behavior);

    /**
     * 批量插入热门行为
     */
    Result<Integer> saveHotBehaviorBatch(List<HotBehavior> behaviors);

    /**
     * 删除单条热门行为
     */
    Result<Integer> removeHotBehavior(String behaviorId);

    /**
     * 批量删除热门行为
     */
    Result<Integer> removeHotBehaviorBatch(List<String> behaviorIds);

    /**
     * 更新单条热门行为
     */
    Result<Integer> updateHotBehavior(HotBehavior behavior);

    /**
     * 批量更新热门行为
     */
    Result<Integer> updateHotBehaviorBatch(List<HotBehavior> behaviors);

    /**
     * 查询单条热门行为
     */
    Result<HotBehavior> getHotBehavior(String behaviorId);

    /**
     * 批量查询热门行为
     */
    Result<List<HotBehavior>> getHotBehaviorBatch(List<String> behaviorIds);
}
