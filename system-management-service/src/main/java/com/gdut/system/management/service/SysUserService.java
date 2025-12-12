package com.gdut.system.management.service;

import com.gdut.system.management.entity.Result;
import com.gdut.system.management.entity.SysUser;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface SysUserService {
    /**
     * 新增单条用户（默认密码可加密，此处预留扩展）
     */
    Result<Integer> saveSysUser(SysUser sysUser);

    /**
     * 批量新增用户
     */
    Result<Integer> saveSysUserBatch(List<SysUser> sysUsers);

    /**
     * 删除单条用户（逻辑删除/物理删除可按需调整）
     */
    Result<Integer> removeSysUser(String userId);

    /**
     * 批量删除用户
     */
    Result<Integer> removeSysUserBatch(List<String> userIds);

    /**
     * 更新单条用户（密码更新需单独处理，避免直接覆盖）
     */
    Result<Integer> updateSysUser(SysUser sysUser);

    /**
     * 批量更新用户
     */
    Result<Integer> updateSysUserBatch(List<SysUser> sysUsers);

    /**
     * 查询单条用户（密码可脱敏返回，此处预留扩展）
     */
    Result<SysUser> getSysUser(String userId);

    /**
     * 批量查询用户
     */
    Result<List<SysUser>> getSysUserBatch(List<String> userIds);

    /**
     * 查询所有的用户Id
     */
    Result<List<String>> getAllSysUserIds();

    /**
     * 批量新增1000条用户
     */
    Result<Integer> saveUserBatch();
}
