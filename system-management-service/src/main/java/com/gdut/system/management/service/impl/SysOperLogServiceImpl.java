package com.gdut.system.management.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdut.system.management.entity.Result;
import com.gdut.system.management.entity.SysOperLog;
import com.gdut.system.management.enums.ResultCode;
import com.gdut.system.management.exception.BusinessException;
import com.gdut.system.management.mapper.SysOperLogMapper;
import com.gdut.system.management.service.SysOperLogService;
import com.gdut.system.management.util.ResultUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    // IP地址正则校验（简单版）
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$");

    // 允许的操作类型（示例：可根据业务扩展）
//    private static final List<String> ALLOWED_OPER_TYPES = List.of("INSERT", "UPDATE", "DELETE", "SELECT", "EXPORT", "IMPORT");

    // final 修饰依赖，构造器注入
    private final SysOperLogMapper sysOperLogMapper;

    public SysOperLogServiceImpl(SysOperLogMapper sysOperLogMapper) {
        this.sysOperLogMapper = sysOperLogMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveSysOperLog(SysOperLog operLog) {
        // 1. 核心参数校验（日志必填字段）
        if (Objects.isNull(operLog.getOperUserId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "操作人ID（operUserId）不能为空");
        }
        if (Objects.isNull(operLog.getOperModule())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "操作模块（operModule）不能为空");
        }
        if (Objects.isNull(operLog.getOperType())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "操作类型（operType）不能为空");
        }
        if (Objects.isNull(operLog.getOperContent())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "操作内容（operContent）不能为空");
        }

        // 2. 业务规则校验
        // 2.1 操作类型合法性校验
//        if (!ALLOWED_OPER_TYPES.contains(operLog.getOperType().toUpperCase())) {
//            throw new BusinessException(ResultCode.PARAM_ERROR,
//                    "操作类型不合法，允许值：" + String.join(",", ALLOWED_OPER_TYPES));
//        }
        // 2.2 IP地址格式校验（可选，宽松校验）
        if (Objects.nonNull(operLog.getOperIp()) && !IP_PATTERN.matcher(operLog.getOperIp()).matches()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "IP地址格式不合法");
        }

        // 3. 设置默认值
        operLog.setOperTime(LocalDateTime.now()); // 操作时间自动填充当前时间
        operLog.setOperResult(Objects.isNull(operLog.getOperResult()) ? 1 : operLog.getOperResult()); // 1-成功，0-失败

        int count = sysOperLogMapper.insertSysOperLogSingle(operLog);
        return count > 0 ? ResultUtils.success(count) : ResultUtils.fail(ResultCode.DB_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> saveSysOperLogBatch(List<SysOperLog> operLogs) {
        if (operLogs == null || operLogs.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        operLogs.forEach(log -> {
            // 批量参数校验
            if (Objects.isNull(log.getOperUserId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量日志中存在操作人ID为空的记录");
            }
            if (Objects.isNull(log.getOperModule())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "日志【操作人：" + log.getOperUserId() + "】操作模块为空");
            }
            if (Objects.isNull(log.getOperType())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "日志【操作人：" + log.getOperUserId() + "】操作类型为空");
            }
//            if (!ALLOWED_OPER_TYPES.contains(log.getOperType().toUpperCase())) {
//                throw new BusinessException(ResultCode.PARAM_ERROR,
//                        "日志【操作人：" + log.getOperUserId() + "】操作类型不合法");
//            }
            // 默认值设置
            log.setOperTime(LocalDateTime.now());
            log.setOperResult(Objects.isNull(log.getOperResult()) ? 1 : log.getOperResult());
            // IP格式校验
            if (Objects.nonNull(log.getOperIp()) && !IP_PATTERN.matcher(log.getOperIp()).matches()) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "日志【操作人：" + log.getOperUserId() + "】IP格式不合法");
            }
        });

        int count = sysOperLogMapper.insertSysOperLogMulti(operLogs);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeSysOperLog(String logId) {
        if (Objects.isNull(logId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        SysOperLog operLog = sysOperLogMapper.selectSysOperLogSingle(logId);
        if (operLog == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "操作日志不存在，删除失败");
        }

        // 日志删除建议：实际项目可改为逻辑删除（如添加is_deleted字段），保留审计数据
        int count = sysOperLogMapper.deleteSysOperLogSingle(logId);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> removeSysOperLogBatch(List<String> logIds) {
        if (logIds == null || logIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        int count = sysOperLogMapper.deleteSysOperLogMulti(logIds);
        return count > 0 ? ResultUtils.success(count) : ResultUtils.failWithCustomMsg(ResultCode.DATA_NOT_FOUND, "无有效日志可删除");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateSysOperLog(SysOperLog operLog) {
        // 日志一般不允许修改，此处仅适配接口，实际使用需谨慎（如仅允许修改operResult）
        if (Objects.isNull(operLog.getId())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "日志ID不能为空");
        }

        SysOperLog existing = sysOperLogMapper.selectSysOperLogSingle(operLog.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.DATA_NOT_FOUND, "操作日志不存在，更新失败");
        }

        // 限制仅允许修改操作结果（operResult），其他字段禁止修改（如需开放可调整）
        SysOperLog updateLog = SysOperLog.builder()
                .id(operLog.getId())
                .operResult(operLog.getOperResult())
                .build();

        int count = sysOperLogMapper.updateSysOperLogSingle(updateLog);
        return ResultUtils.success(count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> updateSysOperLogBatch(List<SysOperLog> operLogs) {
        if (operLogs == null || operLogs.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        operLogs.forEach(log -> {
            if (Objects.isNull(log.getId())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "批量更新中存在日志ID为空的记录");
            }
            if (sysOperLogMapper.selectSysOperLogSingle(log.getId()) == null) {
                throw new BusinessException(ResultCode.DATA_NOT_FOUND, "日志ID【" + log.getId() + "】不存在");
            }
            // 同样限制仅允许修改operResult
            if (Objects.isNull(log.getOperResult())) {
                throw new BusinessException(ResultCode.PARAM_ERROR, "日志ID【" + log.getId() + "】操作结果不能为空");
            }
        });

        int count = sysOperLogMapper.updateSysOperLogMulti(operLogs);
        return ResultUtils.success(count);
    }

    @Override
    public Result<SysOperLog> getSysOperLog(String logId) {
        if (Objects.isNull(logId)) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        SysOperLog operLog = sysOperLogMapper.selectSysOperLogSingle(logId);
        return operLog != null ? ResultUtils.success(operLog) : ResultUtils.fail(ResultCode.DATA_NOT_FOUND);
    }

    @Override
    public Result<List<SysOperLog>> getSysOperLogBatch(List<String> logIds) {
        if (logIds == null || logIds.isEmpty()) {
            return ResultUtils.fail(ResultCode.PARAM_MISS);
        }

        List<SysOperLog> operLogs = sysOperLogMapper.selectSysOperLogMulti(logIds);
        return ResultUtils.success(operLogs);
    }
}
