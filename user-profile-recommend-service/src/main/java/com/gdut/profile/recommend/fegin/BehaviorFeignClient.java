package com.gdut.profile.recommend.fegin;

import com.gdut.profile.recommend.entity.BehaviorDetail;
import com.gdut.profile.recommend.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@FeignClient(value = "behavior-data-service", contextId = "behavior-detail", path = "/api/behavior/detail")//可加fallback降级处理
public interface BehaviorFeignClient {
    /**
     * 根据用户id查询他的最新一条行为信息
     * @param userId 用户id
     * @return 用户行为列表
     */
    @GetMapping("/queryNewOneByUserId/{userId}")
    Result<BehaviorDetail> getBehaviorNewOneByUserId(@PathVariable("userId") String userId);

    /**
     * 根据用户id查询他的所有行为信息
     * @param userId 用户id
     * @return 用户行为列表
     */
    @GetMapping("/queryAllByUserId/{userId}")
    Result<List<BehaviorDetail>> getBehaviorsByUserId(@PathVariable("userId") String userId);

    /**
     * 行为最多的前10个itemId
     * @return 用户行为列表
     */
    @GetMapping("/queryHotTargetId")
    Result<List<String>> getTop10TargetIdByBehaviorCount();
    /**
     * 当前用户行为最多的前10个itemId
     * @return 用户行为列表
     */
    @GetMapping("/queryRelatedTargetId/{userId}")
    Result<List<String>> getTop10TargetIdByBehaviorAndUserId(@PathVariable("userId") String userId);
}
