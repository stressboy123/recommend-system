package com.gdut.behaviordata.mapper;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.Item;
import com.gdut.behaviordata.entity.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/9
 */
@Mapper
public interface ItemMapper {
    /**
     * 插入多条物品
     * @param items 多条物品详细信息
     * @return 成功次数
     */
    int insertItemBatch(@Param("items") List<Item> items);

    /**
     * 根据 itemId 查询物品详情
     * @param itemId 物品ID
     * @return 物品详情结果
     */
    Item selectByItemId(@Param("itemId") String itemId);

    /**
     * 根据 itemId 批量查询物品详情
     * @param itemIds 物品ID列表
     * @return 物品详情结果列表
     */
    List<Item> selectByItemIds(@Param("itemIds") List<String> itemIds);

    /**
     * 根据 tags 批量查询物品详情
     * @param userValidTags 用户有效标签列表
     * @return 物品详情结果列表
     */
    List<Item> selectByTags(@Param("tags") List<String> userValidTags);

    /**
     * 查询所有物品详情
     * @return 所有物品详情结果列表
     */
    List<Item> selectAll();
}
