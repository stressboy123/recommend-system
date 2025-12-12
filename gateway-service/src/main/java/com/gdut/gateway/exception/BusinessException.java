package com.gdut.gateway.exception;

import com.gdut.gateway.enums.ResultCode;
import lombok.Getter;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 异常对应的错误码（关联 ResultCode） */
    private final int code;

    /** 异常提示消息 */
    private final String msg;

    /**
     * 构造方法1：直接传入 ResultCode 枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
    }

    /**
     * 构造方法2：传入 ResultCode + 自定义消息
     */
    public BusinessException(ResultCode resultCode, String customMsg) {
        super(customMsg);
        this.code = resultCode.getCode();
        this.msg = customMsg;
    }

    /**
     * 构造方法3：自定义 code + 自定义消息
     */
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 构造方法4：传入 ResultCode + 自定义消息 + 异常原因
     */
    public BusinessException(ResultCode resultCode, String customMsg, Throwable cause) {
        super(customMsg, cause);
        this.code = resultCode.getCode();
        this.msg = customMsg;
    }
}
