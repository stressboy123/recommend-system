package com.gdut.profile.recommend.service;

import com.gdut.profile.recommend.entity.RecommendResult;
import com.gdut.profile.recommend.entity.Result;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface RecommendService {
    /**
     * 新增单条推荐结果
     */
    Result<Integer> saveRecommendResult(RecommendResult result);

    /**
     * 批量新增推荐结果（推荐场景高频操作，优化性能）
     */
    Result<Integer> saveRecommendResultBatch(List<RecommendResult> results);

    /**
     * 删除单条推荐结果
     */
    Result<Integer> removeRecommendResult(String resultId);

    /**
     * 批量删除推荐结果
     */
    Result<Integer> removeRecommendResultBatch(List<String> resultIds);

    /**
     * 更新单条推荐结果（主要更新点击量、过期时间）
     */
    Result<Integer> updateRecommendResult(RecommendResult result);

    /**
     * 批量更新推荐结果（批量更新点击量等）
     */
    Result<Integer> updateRecommendResultBatch(List<RecommendResult> results);

    /**
     * 查询单条推荐结果
     */
    Result<RecommendResult> getRecommendResult(String resultId);

    /**
     * 批量查询推荐结果
     */
    Result<List<RecommendResult>> getRecommendResultBatch(List<String> resultIds);

    /**
     * 查询个性化推荐结果
     */
    Result<RecommendResult> getPersonRecommend(String userId);

    /**
     * 查询热门推荐结果
     */
    Result<RecommendResult> getHotRecommend(String userId);

    /**
     * 查询相关推荐结果
     */
    Result<RecommendResult> getRelatedRecommend(String userId);
}
