package com.gdut.gateway.entitiy;

import com.gdut.gateway.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Data
public class Result<T> implements Serializable {
    private int code;       // 响应状态码（对应ResultCode）
    private String msg;     // 响应提示消息
    private T data;         // 响应业务数据（泛型，可空）
    private long timestamp; // 响应时间戳（毫秒）

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }

    /**
     * 成功响应（自定义消息+数据）
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 失败响应（基于ResultCode枚举）
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    /**
     * 失败响应（自定义状态码+消息）
     */
    public static <T> Result<T> error(ResultCode code, String msg) {
        return new Result<>(code.getCode(), msg, null);
    }

    /**
     * 失败响应（状态码+消息）
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 失败响应（自定义状态码+消息+数据）
     */
    public static <T> Result<T> error(int code, String msg, T data) {
        return new Result<>(code, msg, data);
    }
}
