package com.gdut.system.management.service;

import com.gdut.system.management.entity.Result;
import com.gdut.system.management.entity.SysOperLog;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
public interface SysOperLogService {
    /**
     * 新增单条操作日志（日志一般无需手动新增，建议结合AOP自动记录，此处预留手动接口）
     */
    Result<Integer> saveSysOperLog(SysOperLog operLog);

    /**
     * 批量新增操作日志
     */
    Result<Integer> saveSysOperLogBatch(List<SysOperLog> operLogs);

    /**
     * 删除单条操作日志（日志多用于审计，建议逻辑删除或按时间清理）
     */
    Result<Integer> removeSysOperLog(String logId);

    /**
     * 批量删除操作日志
     */
    Result<Integer> removeSysOperLogBatch(List<String> logIds);

    /**
     * 更新单条操作日志（日志一般不允许修改，此处仅适配Mapper方法，实际使用需谨慎）
     */
    Result<Integer> updateSysOperLog(SysOperLog operLog);

    /**
     * 批量更新操作日志
     */
    Result<Integer> updateSysOperLogBatch(List<SysOperLog> operLogs);

    /**
     * 查询单条操作日志
     */
    Result<SysOperLog> getSysOperLog(String logId);

    /**
     * 批量查询操作日志
     */
    Result<List<SysOperLog>> getSysOperLogBatch(List<String> logIds);
}
