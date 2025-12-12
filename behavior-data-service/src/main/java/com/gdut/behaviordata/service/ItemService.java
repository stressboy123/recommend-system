package com.gdut.behaviordata.service;

import com.gdut.behaviordata.entity.Item;
import com.gdut.behaviordata.entity.Result;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/9
 */
public interface ItemService {
    Result<Integer> saveItemDetailBatch();

    /**
     * 根据itemId查询item信息
     * @param itemId itemId
     * @return item信息
     */
    Result<Item> getItemByItemId(String itemId);

    /**
     * 根据itemId列表查询item信息
     * @param itemIds itemId列表
     * @return item信息列表
     */
    Result<List<Item>> getItemByIds(List<String> itemIds);

    /**
     * 根据用户标签列表查询item信息
     * @param userValidTags 用户标签列表
     * @return item信息列表
     */
    Result<List<Item>> selectByTags(List<String> userValidTags);

    /**
     * 获取所有item信息
     * @return item信息列表
     */
    Result<List<Item>> getAll();
}
