package com.gdut.system.management.mapper;

import com.gdut.system.management.entity.SysOperLog;
import com.gdut.system.management.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Mapper
public interface SysOperLogMapper {
    /**
     * 插入单条日志
     * @param log 日志详细信息
     * @return 成功次数
     */
    int insertSysOperLogSingle(SysOperLog log);
    /**
     * 插入多条日志
     * @param logs 日志详细信息
     * @return 成功次数
     */
    int insertSysOperLogMulti(List<SysOperLog> logs);
    /**
     * 删除单条日志
     * @param logId 日志ID
     * @return 成功次数
     */
    int deleteSysOperLogSingle(String logId);
    /**
     * 删除多条日志
     * @param logIds 多条日志ID
     * @return 成功次数
     */
    int deleteSysOperLogMulti(List<String> logIds);
    /**
     * 更新单条日志
     * @param log 日志详细信息
     * @return 成功次数
     */
    int updateSysOperLogSingle(SysOperLog log);
    /**
     * 更新多条日志
     * @param logs 多条日志详细信息
     * @return 成功次数
     */
    int updateSysOperLogMulti(List<SysOperLog> logs);
    /**
     * 查询单条日志
     * @param logId 日志ID
     * @return 单条日志详细信息
     */
    SysOperLog selectSysOperLogSingle(String logId);
    /**
     * 查询多条日志
     * @param logIds 多条日志ID
     * @return 多条日志详细信息
     */
    List<SysOperLog> selectSysOperLogMulti(List<String> logIds);
}
