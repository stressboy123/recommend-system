package com.gdut.system.management.service;

import com.gdut.system.management.entity.RecommendRule;
import com.gdut.system.management.entity.Result;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface RecommendRuleService {
    /**
     * 新增单条推荐规则
     */
    Result<Integer> saveRecommendRule(RecommendRule rule);

    /**
     * 批量新增推荐规则
     */
    Result<Integer> saveRecommendRuleBatch(List<RecommendRule> rules);

    /**
     * 删除单条推荐规则
     */
    Result<Integer> removeRecommendRule(String ruleId);

    /**
     * 批量删除推荐规则
     */
    Result<Integer> removeRecommendRuleBatch(List<String> ruleIds);

    /**
     * 更新单条推荐规则
     */
    Result<Integer> updateRecommendRule(RecommendRule rule);

    /**
     * 批量更新推荐规则
     */
    Result<Integer> updateRecommendRuleBatch(List<RecommendRule> rules);

    /**
     * 查询单条推荐规则
     */
    Result<RecommendRule> getRecommendRule(String ruleId);

    /**
     * 批量查询推荐规则
     */
    Result<List<RecommendRule>> getRecommendRuleBatch(List<String> ruleIds);
}
