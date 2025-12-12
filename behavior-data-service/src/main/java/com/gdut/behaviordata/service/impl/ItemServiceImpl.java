package com.gdut.behaviordata.service.impl;

import com.gdut.behaviordata.entity.Item;
import com.gdut.behaviordata.entity.Result;
import com.gdut.behaviordata.enums.ResultCode;
import com.gdut.behaviordata.mapper.ItemMapper;
import com.gdut.behaviordata.service.ItemService;
import com.gdut.behaviordata.util.ResultUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/9
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @Override
    public Result<Integer> saveItemDetailBatch() {
        List<Item> items = new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            Item item = new Item();
            String itemId = "item_id" + j;
            String tags = "tag" + j % 10;
            String categoryPath = "0_" + j % 10 + "_" + j % 10;
            int itemType = (j % 7) + 1;
            item.setId(itemId);
            item.setTags(tags);
            item.setCategoryPath(categoryPath);
            item.setItemType(itemType);
            items.add(item);
        }
        int count = itemMapper.insertItemBatch(items);
        return count > 0 ? ResultUtils.success() : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<Item> getItemByItemId(String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_ERROR);
        }

        Item item = itemMapper.selectByItemId(itemId);
        if (item != null) {
            return ResultUtils.success(item);
        } else {
            return ResultUtils.fail(ResultCode.DB_ERROR);
        }
    }

    @Override
    public Result<List<Item>> getItemByIds(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_ERROR);
        }

        List<Item> items = itemMapper.selectByItemIds(itemIds);
        return items != null ? ResultUtils.success(items) : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<List<Item>> selectByTags(List<String> userValidTags) {
        if (userValidTags == null || userValidTags.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_ERROR);
        }
        List<Item> items = itemMapper.selectByTags(userValidTags);
        return items != null ? ResultUtils.success(items) : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    public Result<List<Item>> getAll() {
        List<Item> items = itemMapper.selectAll();
        return items != null ? ResultUtils.success(items) : ResultUtils.fail(ResultCode.DB_ERROR);
    }
}
