package com.gdut.profile.recommend.fegin;

import com.gdut.profile.recommend.entity.Item;
import com.gdut.profile.recommend.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/9
 */
@FeignClient(value = "behavior-data-service", contextId = "behavior-item", path = "/api/behavior/item")//可加fallback降级处理
public interface ItemFeignClient {
    /**
     * 根据itemId查询item信息
     * @param itemId itemId
     * @return item信息
     */
    @GetMapping("/getById")
    Result<Item> getItemById(@RequestParam("itemId") String itemId);

    /**
     * 根据itemId查询item信息
     * @param itemIds itemId
     * @return item信息
     */
    @PostMapping("/getByIds")
    Result<List<Item>> getItemByIds(@RequestBody List<String> itemIds);

    /**
     * 根据标签查询item信息
     * @param userValidTags 用户有效标签
     * @return item信息
     */
    @PostMapping("/selectByTags")
    Result<List<Item>> selectByTags(@RequestBody List<String> userValidTags);
}
