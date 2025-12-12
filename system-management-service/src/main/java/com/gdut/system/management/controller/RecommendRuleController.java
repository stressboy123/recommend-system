package com.gdut.system.management.controller;

import com.gdut.system.management.entity.RecommendRule;
import com.gdut.system.management.entity.Result;
import com.gdut.system.management.service.RecommendRuleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@RestController
@RequestMapping("/api/system/rule") // 接口路径：推荐规则模块
public class RecommendRuleController {

    // final 修饰依赖，构造器注入
    private final RecommendRuleService recommendRuleService;

    public RecommendRuleController(RecommendRuleService recommendRuleService) {
        this.recommendRuleService = recommendRuleService;
    }

    /**
     * 新增单条推荐规则
     */
    @PostMapping
    public Result<Integer> addRecommendRule(@Valid @RequestBody RecommendRule rule) {
        return recommendRuleService.saveRecommendRule(rule);
    }

    /**
     * 批量新增推荐规则
     */
    @PostMapping("/batch")
    public Result<Integer> addRecommendRuleBatch(@Valid @RequestBody List<RecommendRule> rules) {
        return recommendRuleService.saveRecommendRuleBatch(rules);
    }

    /**
     * 删除单条推荐规则
     */
    @DeleteMapping("/{id}")
    public Result<Integer> deleteRecommendRule(@PathVariable("id") String ruleId) {
        return recommendRuleService.removeRecommendRule(ruleId);
    }

    /**
     * 批量删除推荐规则
     */
    @DeleteMapping("/batch")
    public Result<Integer> deleteRecommendRuleBatch(@RequestBody List<String> ruleIds) {
        return recommendRuleService.removeRecommendRuleBatch(ruleIds);
    }

    /**
     * 更新单条推荐规则
     */
    @PutMapping
    public Result<Integer> updateRecommendRule(@Valid @RequestBody RecommendRule rule) {
        return recommendRuleService.updateRecommendRule(rule);
    }

    /**
     * 批量更新推荐规则
     */
    @PutMapping("/batch")
    public Result<Integer> updateRecommendRuleBatch(@Valid @RequestBody List<RecommendRule> rules) {
        return recommendRuleService.updateRecommendRuleBatch(rules);
    }

    /**
     * 查询单条推荐规则
     */
    @GetMapping("/{id}")
    public Result<RecommendRule> getRecommendRule(@PathVariable("id") String ruleId) {
        return recommendRuleService.getRecommendRule(ruleId);
    }

    /**
     * 批量查询推荐规则
     */
    @GetMapping("/batch")
    public Result<List<RecommendRule>> getRecommendRuleBatch(@RequestParam("ids") List<String> ruleIds) {
        return recommendRuleService.getRecommendRuleBatch(ruleIds);
    }
}
