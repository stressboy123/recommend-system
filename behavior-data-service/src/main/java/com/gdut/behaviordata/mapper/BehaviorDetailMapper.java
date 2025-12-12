package com.gdut.behaviordata.mapper;

import com.gdut.behaviordata.entity.BehaviorDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface BehaviorDetailMapper {
    /**
     * 插入单条行为
     * @param behavior 行为详细信息
     * @return 成功次数
     */
    int insertBehaviorDetailSingle(BehaviorDetail behavior);
    /**
     * 插入多条行为
     * @param behaviors 多条行为详细信息
     * @return 成功次数
     */
    int insertBehaviorDetailMulti(@Param("behaviors") List<BehaviorDetail> behaviors);
    /**
     * 删除单条行为
     * @param behaviorId 行为信息ID
     * @return 成功次数
     */
    int deleteBehaviorDetailSingle(@Param("behaviorId") String behaviorId);
    /**
     * 删除多条行为
     * @param behaviorIds 多条行为信息ID
     * @return 成功次数
     */
    int deleteBehaviorDetailMulti(@Param("behaviorIds") List<String> behaviorIds);
    /**
     * 更新单条行为
     * @param behavior 行为详细信息
     * @return 成功次数
     */
    int updateBehaviorDetailSingle(BehaviorDetail behavior);
    /**
     * 更新多条行为
     * @param behaviors 多条行为详细信息
     * @return 成功次数
     */
    int updateBehaviorDetailMulti(@Param("behaviors") List<BehaviorDetail> behaviors);
    /**
     * 查询单条行为
     * @param behaviorId 行为信息ID
     * @return 单条行为详细信息
     */
    BehaviorDetail selectBehaviorDetailSingle(@Param("behaviorId") String behaviorId);
    /**
     * 查询多条行为
     * @param behaviorIds 多条行为信息ID
     * @return 多条行为详细信息
     */
    List<BehaviorDetail> selectBehaviorDetailMulti(@Param("behaviorIds") List<String> behaviorIds);

    /**
     * 根据用户id查询他的所有行为信息
     * @param userId 用户id
     * @return 用户行为列表
     */
    List<BehaviorDetail> selectBehaviorDetailByUserId(@Param("userId") String userId);

    /**
     * 根据用户id查询他的最新一条行为信息
     * @param userId 用户id
     * @return 用户行为
     */
    BehaviorDetail selectBehaviorDetailNewOneByUserId(@Param("userId") String userId);

    /**
     * 查询所有行为信息
     * @return 所有行为列表
     */
    List<Map<String, Object>> selectTop10TargetIdByBehaviorCount();

    /**
     * 根据用户id查询所有行为信息
     * @param userId 用户id
     * @return 所有行为列表
     */
    List<Map<String, Object>> selectTop10TargetIdByBehaviorCountAndUserId(@Param("userId") String userId);
}
