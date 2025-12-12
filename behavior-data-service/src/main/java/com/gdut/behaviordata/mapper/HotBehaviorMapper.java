package com.gdut.behaviordata.mapper;

import com.gdut.behaviordata.entity.BehaviorDetail;
import com.gdut.behaviordata.entity.HotBehavior;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface HotBehaviorMapper {
    /**
     * 插入单条热门行为
     * @param behavior 热门行为详细信息
     * @return 成功次数
     */
    int insertHotBehaviorDetailSingle(HotBehavior behavior);
    /**
     * 插入多条热门行为
     * @param behaviors 多条热门行为详细信息
     * @return 成功次数
     */
    int insertHotBehaviorDetailMulti(List<HotBehavior> behaviors);
    /**
     * 删除单条热门行为
     * @param behaviorId 热门行为信息ID
     * @return 成功次数
     */
    int deleteHotBehaviorDetailSingle(String behaviorId);
    /**
     * 删除多条热门行为
     * @param behaviorIds 多条热门行为信息ID
     * @return 成功次数
     */
    int deleteHotBehaviorDetailMulti(List<String> behaviorIds);
    /**
     * 更新单条热门行为
     * @param behavior 热门行为详细信息
     * @return 成功次数
     */
    int updateHotBehaviorDetailSingle(HotBehavior behavior);
    /**
     * 更新多条热门行为
     * @param behaviors 热门多条行为详细信息
     * @return 成功次数
     */
    int updateHotBehaviorDetailMulti(List<HotBehavior> behaviors);
    /**
     * 查询单条热门行为
     * @param behaviorId 热门行为信息ID
     * @return 单条热门行为详细信息
     */
    HotBehavior selectHotBehaviorDetailSingle(String behaviorId);
    /**
     * 查询多条热门行为
     * @param behaviorIds 多条热门行为信息ID
     * @return 多条热门行为详细信息
     */
    List<HotBehavior> selectHotBehaviorDetailMulti(List<String> behaviorIds);
}
