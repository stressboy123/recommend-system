package com.gdut.system.management.controller;

import com.gdut.system.management.entity.Result;
import com.gdut.system.management.entity.SysOperLog;
import com.gdut.system.management.service.SysOperLogService;
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
@RequestMapping("/api/system/log") // 接口路径：系统操作日志模块
public class SysOperLogController {

    // final 修饰依赖，构造器注入
    private final SysOperLogService sysOperLogService;

    public SysOperLogController(SysOperLogService sysOperLogService) {
        this.sysOperLogService = sysOperLogService;
    }

    /**
     * 新增单条操作日志（建议通过AOP自动记录，此接口用于特殊场景手动补充）
     */
    @PostMapping
    public Result<Integer> addSysOperLog(@Valid @RequestBody SysOperLog operLog) {
        return sysOperLogService.saveSysOperLog(operLog);
    }

    /**
     * 批量新增操作日志
     */
    @PostMapping("/batch")
    public Result<Integer> addSysOperLogBatch(@Valid @RequestBody List<SysOperLog> operLogs) {
        return sysOperLogService.saveSysOperLogBatch(operLogs);
    }

    /**
     * 删除单条操作日志
     */
    @DeleteMapping("/{id}")
    public Result<Integer> deleteSysOperLog(@PathVariable("id") String logId) {
        return sysOperLogService.removeSysOperLog(logId);
    }

    /**
     * 批量删除操作日志（用于日志清理）
     */
    @DeleteMapping("/batch")
    public Result<Integer> deleteSysOperLogBatch(@RequestBody List<String> logIds) {
        return sysOperLogService.removeSysOperLogBatch(logIds);
    }

    /**
     * 更新单条操作日志（仅允许修改操作结果）
     */
    @PutMapping
    public Result<Integer> updateSysOperLog(@Valid @RequestBody SysOperLog operLog) {
        return sysOperLogService.updateSysOperLog(operLog);
    }

    /**
     * 批量更新操作日志（仅允许修改操作结果）
     */
    @PutMapping("/batch")
    public Result<Integer> updateSysOperLogBatch(@Valid @RequestBody List<SysOperLog> operLogs) {
        return sysOperLogService.updateSysOperLogBatch(operLogs);
    }

    /**
     * 查询单条操作日志
     */
    @GetMapping("/{id}")
    public Result<SysOperLog> getSysOperLog(@PathVariable("id") String logId) {
        return sysOperLogService.getSysOperLog(logId);
    }

    /**
     * 批量查询操作日志
     */
    @GetMapping("/batch")
    public Result<List<SysOperLog>> getSysOperLogBatch(@RequestParam("ids") List<String> logIds) {
        return sysOperLogService.getSysOperLogBatch(logIds);
    }
}
