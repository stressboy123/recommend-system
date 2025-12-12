package com.gdut.behaviordata.controller;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.service.BehaviorService;
import com.gdut.behaviordata.util.ResultUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@RestController
@RequestMapping("/api/behavior/detail")
public class BehaviorController {
    private final BehaviorService behaviorService;

    public BehaviorController(BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
    }

    @PostMapping("/add")
    public Result<Integer> addBehavior(@Valid @RequestBody BehaviorDetail behavior) {
        return behaviorService.saveBehaviorDetail(behavior);
    }

    @PostMapping("/addBatch")
    public Result<Integer> addBehaviorBatch(@Valid @RequestBody List<BehaviorDetail> behaviors) {
        return behaviorService.saveBehaviorDetailBatch(behaviors);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Integer> deleteBehavior(@PathVariable("id") String behaviorId) {
        return behaviorService.removeBehaviorDetail(behaviorId);
    }

    @DeleteMapping("/deleteBatch")
    public Result<Integer> deleteBehaviorBatch(@RequestBody List<String> behaviorIds) {
        return behaviorService.removeBehaviorDetailBatch(behaviorIds);
    }

    @PutMapping("/update")
    public Result<Integer> updateBehavior(@Valid @RequestBody BehaviorDetail behavior) {
        return behaviorService.updateBehaviorDetail(behavior);
    }

    @PutMapping("/updateBatch")
    public Result<Integer> updateBehaviorBatch(@Valid @RequestBody List<BehaviorDetail> behaviors) {
        return behaviorService.updateBehaviorDetailBatch(behaviors);
    }

    @GetMapping("/query/{id}")
    public Result<BehaviorDetail> getBehavior(@PathVariable("id") String behaviorId) {
        return behaviorService.getBehaviorDetail(behaviorId);
    }

    @PostMapping("/queryBatch")
    public Result<List<BehaviorDetail>> getBehaviorBatch(@RequestBody List<String> behaviorIds) {
        return behaviorService.getBehaviorDetailBatch(behaviorIds);
    }

    @PostMapping("/collect")
    public Result<BehaviorDetail> collect(@Valid @RequestBody BehaviorDetail behavior) {
        behaviorService.collect(behavior);
        return ResultUtils.successWithCustomMsg("消息采集成功");
    }

    @GetMapping("/queryAllByUserId/{userId}")
    Result<List<BehaviorDetail>> getBehaviorsByUserId(@PathVariable("userId") String userId) {
        return behaviorService.getBehaviorsDetailByUserId(userId);
    }

    @GetMapping("/queryNewOneByUserId/{userId}")
    Result<BehaviorDetail> getBehaviorNewOneByUserId(@PathVariable("userId") String userId) {
        return behaviorService.getBehaviorNewOneDetailByUserId(userId);
    }

    @PostMapping("/addBehaviorsBatch")
    public Result<Integer> addBehaviorsBatch() {
        return behaviorService.saveBehaviorsBatch();
    }

    /**
     * 行为最多的前10个itemId
     * @return 用户行为列表
     */
    @GetMapping("/queryHotTargetId")
    public Result<List<String>> getTop10TargetIdByBehaviorCount() {
        return behaviorService.getTop10TargetIdByBehaviorCount();
    }
    /**
     * 当前用户行为最多的前10个itemId
     * @return 用户行为列表
     */
    @GetMapping("/queryRelatedTargetId/{userId}")
    public Result<List<String>> getTop10TargetIdByBehaviorAndUserId(@PathVariable("userId") String userId) {
        return behaviorService.getTop10TargetIdByBehaviorAndUserId(userId);
    }
}
