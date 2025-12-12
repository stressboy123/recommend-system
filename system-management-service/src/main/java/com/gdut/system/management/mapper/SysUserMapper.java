package com.gdut.system.management.mapper;

import com.gdut.system.management.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface SysUserMapper {
    /**
     * 插入单条用户
     * @param user 用户详细信息
     * @return 成功次数
     */
    int insertSysUserSingle(SysUser user);
    /**
     * 插入多条用户
     * @param users 用户详细信息
     * @return 成功次数
     */
    int insertSysUserMulti(@Param("users") List<SysUser> users);
    /**
     * 删除单条用户
     * @param userId 用户ID
     * @return 成功次数
     */
    int deleteSysUserSingle(@Param("userId") String userId);
    /**
     * 删除多条用户
     * @param userIds 多条用户ID
     * @return 成功次数
     */
    int deleteSysUserMulti(@Param("userIds") List<String> userIds);
    /**
     * 更新单条用户
     * @param user 用户详细信息
     * @return 成功次数
     */
    int updateSysUserSingle(SysUser user);
    /**
     * 更新多条用户
     * @param users 多条用户详细信息
     * @return 成功次数
     */
    int updateSysUserMulti(@Param("users") List<SysUser> users);
    /**
     * 查询单条用户
     * @param userId 用户ID
     * @return 单条用户详细信息
     */
    SysUser selectSysUserSingle(@Param("userId") String userId);
    /**
     * 查询多条用户
     * @param userIds 多条用户ID
     * @return 多条用户详细信息
     */
    List<SysUser> selectSysUserMulti(@Param("userIds") List<String> userIds);

    /**
     * 查询所有的用户Id
     * @return 用户Id列表
     */
    List<String> selectAllSysUserIds();
}
