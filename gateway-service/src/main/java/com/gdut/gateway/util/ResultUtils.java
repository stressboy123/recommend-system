package com.gdut.gateway.util;

import com.gdut.gateway.entitiy.Result;
import com.gdut.gateway.enums.ResultCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // 私有构造，禁止实例化
public class ResultUtils {
    public static <T> Result<T> ok() {
        return Result.success();
    }

    public static <T> Result<T> ok(T data) {
        return Result.success(data);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return Result.success(msg, data);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return Result.error(resultCode);
    }

    public static <T> Result<T> fail(ResultCode code, String msg) {
        return Result.error(code, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return Result.error(code, msg);
    }

    /**
     * 异常转换为Result（捕获业务异常，返回对应提示）
     */
    public static <T> Result<T> exception(Throwable e) {
        // 可扩展：根据不同异常类型返回不同状态码（如NullPointerException返回400，SQLException返回502）
        return Result.error(ResultCode.SYSTEM_ERROR, e.getMessage());
    }
}
