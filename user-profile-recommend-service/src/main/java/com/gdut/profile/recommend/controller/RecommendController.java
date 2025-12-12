package com.gdut.profile.recommend.controller;

import com.gdut.profile.recommend.entity.RecommendResult;
import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.entity.UserTag;
import com.gdut.profile.recommend.service.RecommendService;
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
@RequestMapping("/api/recommend/recommend") // 接口路径：推荐结果模块（RESTful风格）
public class RecommendController {

    // final 修饰依赖，构造器注入
    private final RecommendService recommendService;

    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

    /**
     * 新增单条推荐结果
     */
    @PostMapping("/add")
    public Result<Integer> addRecommendResult(@Valid @RequestBody RecommendResult result) {
        return recommendService.saveRecommendResult(result);
    }

    /**
     * 批量新增推荐结果
     */
    @PostMapping("/addBatch")
    public Result<Integer> addRecommendResultBatch(@Valid @RequestBody List<RecommendResult> results) {
        return recommendService.saveRecommendResultBatch(results);
    }

    /**
     * 删除单条推荐结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Integer> deleteRecommendResult(@PathVariable("id") String resultId) {
        return recommendService.removeRecommendResult(resultId);
    }

    /**
     * 批量删除推荐结果
     */
    @DeleteMapping("/deleteBatch")
    public Result<Integer> deleteRecommendResultBatch(@RequestBody List<String> resultIds) {
        return recommendService.removeRecommendResultBatch(resultIds);
    }

    /**
     * 更新单条推荐结果（仅支持点击量、过期时间）
     */
    @PutMapping("/update")
    public Result<Integer> updateRecommendResult(@Valid @RequestBody RecommendResult result) {
        return recommendService.updateRecommendResult(result);
    }

    /**
     * 批量更新推荐结果
     */
    @PutMapping("/updateBatch")
    public Result<Integer> updateRecommendResultBatch(@Valid @RequestBody List<RecommendResult> results) {
        return recommendService.updateRecommendResultBatch(results);
    }

    /**
     * 查询单条推荐结果
     */
    @GetMapping("/query/{id}")
    public Result<RecommendResult> getRecommendResult(@PathVariable("id") String resultId) {
        return recommendService.getRecommendResult(resultId);
    }

    /**
     * 批量查询推荐结果
     */
    @PostMapping("/queryBatch")
    public Result<List<RecommendResult>> getRecommendResultBatch(@RequestBody List<String> resultIds) {
        return recommendService.getRecommendResultBatch(resultIds);
    }

    /**
     * 查询个性化推荐结果
     */
    @GetMapping("/queryPersonRecommend/{id}")
    public Result<RecommendResult> getPersonRecommend(@PathVariable("id") String userId) {
        return recommendService.getPersonRecommend(userId);
    }

    /**
     * 查询热门推荐结果
     */
    @GetMapping("/queryHotRecommend/{id}")
    public Result<RecommendResult> getHotRecommend(@PathVariable("id") String userId) {
        return recommendService.getHotRecommend(userId);
    }

    /**
     * 查询相关推荐结果
     */
    @GetMapping("/queryRelatedRecommend/{id}")
    public Result<RecommendResult> getRelatedRecommend(@PathVariable("id") String userId) {
        return recommendService.getRelatedRecommend(userId);
    }
}
