package com.gdut.system.management.exception;

import com.gdut.system.management.entity.Result;
import com.gdut.system.management.enums.ResultCode;
import com.gdut.system.management.util.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Slf4j
@RestControllerAdvice // 作用于所有 @RestController 注解的类
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常（优先级最高）
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 业务异常仅打印 INFO 日志（无需堆栈，便于排查业务问题）
        log.info("业务异常：code={}, msg={}", e.getCode(), e.getMsg(), e);
        return ResultUtils.failWithCustomCodeAndMsg(e.getCode(), e.getMsg());
    }

    /**
     * 处理参数校验异常（@Valid 注解触发）
     * 例如：实体类字段加 @NotNull、@Size 等校验失败时抛出
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        // 提取所有字段校验失败的消息，拼接成字符串
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        log.warn("参数校验异常：{}", errorMsg, e);
        return ResultUtils.failWithCustomMsg(ResultCode.PARAM_ERROR, errorMsg);
    }

    /**
     * 处理参数类型不匹配异常
     * 例如：接口参数要求 Integer，实际传入 String
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMsg = String.format("参数类型不匹配：参数【%s】要求类型【%s】，实际传入【%s】",
                e.getName(), e.getRequiredType().getSimpleName(), e.getValue());
        log.warn(errorMsg, e);
        return ResultUtils.failWithCustomMsg(ResultCode.PARAM_ERROR, errorMsg);
    }

    /**
     * 处理数据库相关异常（SQL执行失败、连接异常等）
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public Result<Void> handleDatabaseException(Exception e) {
        log.error("数据库操作异常：", e); // 打印完整堆栈，便于排查数据库问题
        return ResultUtils.fail(ResultCode.DB_ERROR);
    }

    /**
     * 处理空指针异常（单独捕获，返回更友好的提示）
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常：", e);
        return ResultUtils.failWithCustomMsg(ResultCode.SYSTEM_ERROR, "系统异常：数据为空，请联系开发人员排查");
    }

    /**
     * 处理其他未捕获的系统异常（兜底处理）
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统未知异常：", e); // 打印完整堆栈，便于排查未知问题
        return ResultUtils.failWithCustomMsg(ResultCode.SYSTEM_ERROR, "系统异常，请稍后重试");
    }
}
