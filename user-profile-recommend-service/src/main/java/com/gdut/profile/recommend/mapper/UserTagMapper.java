package com.gdut.profile.recommend.mapper;

import com.gdut.profile.recommend.entity.UserTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface UserTagMapper {
    /**
     * 插入单条用户标签
     * @param tag 用户标签详细信息
     * @return 成功次数
     */
    int insertUserTagSingle(UserTag tag);
    /**
     * 插入多条用户标签
     * @param tags 用户标签详细信息
     * @return 成功次数
     */
    int insertUserTagMulti(@Param("tags") List<UserTag> tags);
    /**
     * 删除单条用户标签
     * @param tagId 用户标签ID
     * @return 成功次数
     */
    int deleteUserTagSingle(@Param("tagId") String tagId);
    /**
     * 删除多条用户标签
     * @param tagIds 多条用户标签ID
     * @return 成功次数
     */
    int deleteUserTagMulti(@Param("tagIds") List<String> tagIds);
    /**
     * 更新单条用户标签
     * @param tag 用户标签详细信息
     * @return 成功次数
     */
    int updateUserTagSingle(UserTag tag);
    /**
     * 更新多条用户标签
     * @param tags 多条用户标签详细信息
     * @return 成功次数
     */
    int updateUserTagMulti(@Param("tags") List<UserTag> tags);
    /**
     * 查询单条用户标签
     * @param tagId 用户标签ID
     * @return 单条用户标签详细信息
     */
    UserTag selectUserTagSingle(@Param("tagId") String tagId);
    /**
     * 查询多条用户标签
     * @param tagIds 多条用户标签ID
     * @return 多条用户标签详细信息
     */
    List<UserTag> selectUserTagMulti(@Param("tagIds") List<String> tagIds);
    /**
     * 根据用户id查询用户标签
     * @param userId 用户ID
     * @return 单条用户标签详细信息
     */
    UserTag selectUserTagByUserId(@Param("userId") String userId);

    /**
     * 查询所有用户标签
     * @return 多条所有标签详细信息
     */
    List<UserTag> selectAllUserTag();
}
