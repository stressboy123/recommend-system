package com.gdut.behaviordata.controller;

import com.gdut.behaviordata.entity.HotBehavior;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.service.HotBehaviorService;
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
@RequestMapping("/api/behavior/hot") // 接口路径区分热门行为与普通行为（缓存热点行为）
public class HotBehaviorController {

    // final 修饰依赖，构造器注入
    private final HotBehaviorService hotBehaviorService;

    public HotBehaviorController(HotBehaviorService hotBehaviorService) {
        this.hotBehaviorService = hotBehaviorService;
    }

    /**
     * 新增单条热门行为
     */
    @PostMapping
    public Result<Integer> addHotBehavior(@Valid @RequestBody HotBehavior behavior) {
        return hotBehaviorService.saveHotBehavior(behavior);
    }

    /**
     * 批量新增热门行为
     */
    @PostMapping("/batch")
    public Result<Integer> addHotBehaviorBatch(@Valid @RequestBody List<HotBehavior> behaviors) {
        return hotBehaviorService.saveHotBehaviorBatch(behaviors);
    }

    /**
     * 删除单条热门行为
     */
    @DeleteMapping("/{id}")
    public Result<Integer> deleteHotBehavior(@PathVariable("id") String behaviorId) {
        return hotBehaviorService.removeHotBehavior(behaviorId);
    }

    /**
     * 批量删除热门行为
     */
    @DeleteMapping("/batch")
    public Result<Integer> deleteHotBehaviorBatch(@RequestBody List<String> behaviorIds) {
        return hotBehaviorService.removeHotBehaviorBatch(behaviorIds);
    }

    /**
     * 更新单条热门行为
     */
    @PutMapping
    public Result<Integer> updateHotBehavior(@Valid @RequestBody HotBehavior behavior) {
        return hotBehaviorService.updateHotBehavior(behavior);
    }

    /**
     * 批量更新热门行为
     */
    @PutMapping("/batch")
    public Result<Integer> updateHotBehaviorBatch(@Valid @RequestBody List<HotBehavior> behaviors) {
        return hotBehaviorService.updateHotBehaviorBatch(behaviors);
    }

    /**
     * 查询单条热门行为
     */
    @GetMapping("/{id}")
    public Result<HotBehavior> getHotBehavior(@PathVariable("id") String behaviorId) {
        return hotBehaviorService.getHotBehavior(behaviorId);
    }

    /**
     * 批量查询热门行为
     */
    @GetMapping("/batch")
    public Result<List<HotBehavior>> getHotBehaviorBatch(@RequestParam("ids") List<String> behaviorIds) {
        return hotBehaviorService.getHotBehaviorBatch(behaviorIds);
    }
}
