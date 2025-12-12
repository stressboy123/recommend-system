package com.gdut.behaviordata.service;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.Result;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface BehaviorService {
    /**
     * 插入单条行为
     */
    Result<Integer> saveBehaviorDetail(BehaviorDetail behavior);

    /**
     * 批量插入行为
     */
    Result<Integer> saveBehaviorDetailBatch(List<BehaviorDetail> behaviors);

    /**
     * 删除单条行为
     */
    Result<Integer> removeBehaviorDetail(String behaviorId);

    /**
     * 批量删除行为
     */
    Result<Integer> removeBehaviorDetailBatch(List<String> behaviorIds);

    /**
     * 更新单条行为
     */
    Result<Integer> updateBehaviorDetail(BehaviorDetail behavior);

    /**
     * 批量更新行为
     */
    Result<Integer> updateBehaviorDetailBatch(List<BehaviorDetail> behaviors);

    /**
     * 查询单条行为
     */
    Result<BehaviorDetail> getBehaviorDetail(String behaviorId);

    /**
     * 批量查询行为
     */
    Result<List<BehaviorDetail>> getBehaviorDetailBatch(List<String> behaviorIds);

    /**
     * 采集信息
     */
    Result<Integer> collect(BehaviorDetail behavior);

    /**
     * 根据用户id查询他的所有行为信息
     */
    Result<List<BehaviorDetail>> getBehaviorsDetailByUserId(String userId);

    /**
     * 根据用户id查询他的最新一条行为信息
     */
    Result<BehaviorDetail> getBehaviorNewOneDetailByUserId(String userId);
    /**
     * 批量插入1000*100条行为
     */
    Result<Integer> saveBehaviorsBatch();

    /**
     * 行为最多的前10个itemId
     */
    Result<List<String>> getTop10TargetIdByBehaviorCount();

    /**
     * 当前用户行为最多的前10个itemId
     */
    Result<List<String>> getTop10TargetIdByBehaviorAndUserId(String userId);
}
