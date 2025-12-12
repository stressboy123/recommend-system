package com.gdut.system.management.mapper;

import com.gdut.system.management.entity.RecommendRule;
import com.gdut.system.management.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface RecommendRuleMapper {
    /**
     * 插入单条推荐规则
     * @param rule 推荐规则详细信息
     * @return 成功次数
     */
    int insertRecommendRuleSingle(RecommendRule rule);
    /**
     * 插入多条推荐规则
     * @param rules 推荐规则详细信息
     * @return 成功次数
     */
    int insertRecommendRuleMulti(List<RecommendRule> rules);
    /**
     * 删除单条推荐规则
     * @param ruleId 推荐规则ID
     * @return 成功次数
     */
    int deleteRecommendRuleSingle(String ruleId);
    /**
     * 删除多条推荐规则
     * @param ruleIds 多条推荐规则ID
     * @return 成功次数
     */
    int deleteRecommendRuleMulti(List<String> ruleIds);
    /**
     * 更新单条推荐规则
     * @param rule 推荐规则详细信息
     * @return 成功次数
     */
    int updateRecommendRuleSingle(RecommendRule rule);
    /**
     * 更新多条推荐规则
     * @param rules 多条推荐规则详细信息
     * @return 成功次数
     */
    int updateRecommendRuleMulti(List<RecommendRule> rules);
    /**
     * 查询单条推荐规则
     * @param ruleId 推荐规则ID
     * @return 单条推荐规则详细信息
     */
    RecommendRule selectRecommendRuleSingle(String ruleId);
    /**
     * 查询多条推荐规则
     * @param ruleIds 多条推荐规则ID
     * @return 多条推荐规则详细信息
     */
    List<RecommendRule> selectRecommendRuleMulti(List<String> ruleIds);
}
