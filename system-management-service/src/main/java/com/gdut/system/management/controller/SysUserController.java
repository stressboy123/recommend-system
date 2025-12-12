package com.gdut.system.management.controller;

import com.gdut.system.management.entity.Result;
import com.gdut.system.management.entity.SysUser;
import com.gdut.system.management.service.SysUserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@RestController
@RequestMapping("/api/system/user") // 接口路径：系统用户模块
public class SysUserController {

    // final 修饰依赖，构造器注入
    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 新增单条用户
     */
    @PostMapping("/add")
    public Result<Integer> addSysUser(@Valid @RequestBody SysUser sysUser) {
        return sysUserService.saveSysUser(sysUser);
    }

    /**
     * 批量新增用户
     */
    @PostMapping("/addBatch")
    public Result<Integer> addSysUserBatch(@Valid @RequestBody List<SysUser> sysUsers) {
        return sysUserService.saveSysUserBatch(sysUsers);
    }

    /**
     * 删除单条用户
     */
    @DeleteMapping("/delete/{id}")
    public Result<Integer> deleteSysUser(@PathVariable("id") String userId) {
        return sysUserService.removeSysUser(userId);
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/deleteBatch")
    public Result<Integer> deleteSysUserBatch(@RequestBody List<String> userIds) {
        return sysUserService.removeSysUserBatch(userIds);
    }

    /**
     * 更新单条用户
     */
    @PutMapping("/update")
    public Result<Integer> updateSysUser(@Valid @RequestBody SysUser sysUser) {
        return sysUserService.updateSysUser(sysUser);
    }

    /**
     * 批量更新用户
     */
    @PutMapping("/updateBatch")
    public Result<Integer> updateSysUserBatch(@Valid @RequestBody List<SysUser> sysUsers) {
        return sysUserService.updateSysUserBatch(sysUsers);
    }

    /**
     * 查询单条用户
     */
    @GetMapping("/query/{id}")
    public Result<SysUser> getSysUser(@PathVariable("id") String userId) {
        return sysUserService.getSysUser(userId);
    }

    /**
     * 批量查询用户
     */
    @PostMapping("/queryBatch")
    public Result<List<SysUser>> getSysUserBatch(@RequestBody List<String> userIds) {
        return sysUserService.getSysUserBatch(userIds);
    }

    /**
     * 查询所有的用户Id
     * @return 用户Idl列表
     */
    @PostMapping("/queryAllId")
    public Result<List<String>> getAllSysUserIds() {
        return sysUserService.getAllSysUserIds();
    }

    /**
     * 批量新增1000条用户
     */
    @PostMapping("/addUserBatch")
    public Result<Integer> addUserBatch() {
        return sysUserService.saveUserBatch();
    }
}
