package com.gdut.profile.recommend.util;

import com.gdut.profile.recommend.entity.Result;
import com.gdut.profile.recommend.enums.ResultCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE) // 私有构造，禁止实例化
public class ResultUtils {

    // ==================== 成功场景：8个方法 ====================
    /** 场景1：无数据+默认码+默认消息 */
    public static <T> Result<T> success() {
        return Result.success();
    }

    /** 场景2：带数据+默认码+默认消息 */
    public static <T> Result<T> success(T data) {
        return Result.success(data);
    }

    /** 场景3：无数据+默认码+自定义消息 */
    public static <T> Result<T> successWithCustomMsg(String msg) {
        return Result.successWithCustomMsg(msg);
    }

    /** 场景4：带数据+默认码+自定义消息 */
    public static <T> Result<T> successWithCustomMsgAndData(String msg, T data) {
        return Result.successWithCustomMsgAndData(msg, data);
    }

    /** 场景5：无数据+自定义码+默认消息 */
    public static <T> Result<T> successWithCustomCode(int code) {
        return Result.successWithCustomCode(code);
    }

    /** 场景6：带数据+自定义码+默认消息 */
    public static <T> Result<T> successWithCustomCodeAndData(int code, T data) {
        return Result.successWithCustomCodeAndData(code, data);
    }

    /** 场景7：无数据+自定义码+自定义消息 */
    public static <T> Result<T> successWithCustomCodeAndMsg(int code, String msg) {
        return Result.successWithCustomCodeAndMsg(code, msg);
    }

    /** 场景8：带数据+自定义码+自定义消息 */
    public static <T> Result<T> successWithCustomCodeMsgAndData(int code, String msg, T data) {
        return Result.successWithCustomCodeMsgAndData(code, msg, data);
    }

    // ==================== 失败场景：9个方法 ====================
    /** 场景1：基于ResultCode枚举+无数据 */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return Result.fail(resultCode);
    }

    /** 场景2：基于ResultCode枚举+自定义消息+无数据 */
    public static <T> Result<T> failWithCustomMsg(ResultCode resultCode, String msg) {
        return Result.failWithCustomMsg(resultCode, msg);
    }

    /** 场景3：基于ResultCode枚举+自定义数据+默认消息 */
    public static <T> Result<T> failWithData(ResultCode resultCode, T data) {
        return Result.failWithData(resultCode, data);
    }

    /** 场景4：基于ResultCode枚举+自定义消息+自定义数据 */
    public static <T> Result<T> failWithCustomMsgAndData(ResultCode resultCode, String msg, T data) {
        return Result.failWithCustomMsgAndData(resultCode, msg, data);
    }

    /** 场景5：自定义code+自定义msg+无数据 */
    public static <T> Result<T> failWithCustomCodeAndMsg(int code, String msg) {
        return Result.failWithCustomCodeAndMsg(code, msg);
    }

    /** 场景6：自定义code+默认msg（匹配ResultCode，匹配不到用系统异常）+无数据 */
    public static <T> Result<T> failWithCustomCode(int code) {
        return Result.failWithCustomCode(code);
    }

    /** 场景7：自定义code+自定义msg+自定义数据 */
    public static <T> Result<T> failWithCustomCodeMsgAndData(int code, String msg, T data) {
        return Result.failWithCustomCodeMsgAndData(code, msg, data);
    }

    /** 场景8：仅自定义msg（默认code=500）+无数据 */
    public static <T> Result<T> failWithOnlyMsg(String msg) {
        return Result.failWithOnlyMsg(msg);
    }

    /** 场景9：仅自定义msg（默认code=500）+自定义数据 */
    public static <T> Result<T> failWithOnlyMsgAndData(String msg, T data) {
        return Result.failWithOnlyMsgAndData(msg, data);
    }
}
