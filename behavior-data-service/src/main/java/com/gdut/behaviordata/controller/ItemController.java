package com.gdut.behaviordata.controller;

import com.gdut.behaviordata.entity.Item;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.service.ItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/9
 */
@RestController
@RequestMapping("/api/behavior/item")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/addBatch")
    public Result<Integer> addItemBatch() {
        return itemService.saveItemDetailBatch();
    }

    @GetMapping("/getById")
    public Result<Item> getItemById(@RequestParam String itemId) {
        return itemService.getItemByItemId(itemId);
    }

    @PostMapping("/getByIds")
    public Result<List<Item>> getItemByIds(@RequestBody List<String> itemIds) {
        return itemService.getItemByIds(itemIds);
    }

    @PostMapping("/selectByTags")
    public Result<List<Item>> selectByTags(@RequestBody List<String> userValidTags) {
        return itemService.selectByTags(userValidTags);
    }

    @GetMapping("/getAll")
    public Result<List<Item>> getAll() {
        return itemService.getAll();
    }

}
