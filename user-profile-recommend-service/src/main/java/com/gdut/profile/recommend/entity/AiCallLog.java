package com.gdut.profile.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author liujunliang
 * @date 2025/12/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("ai_call_log")
public class AiCallLog {
    @TableId
    private String id;

    @NotNull(message = "没有用户ID")
    private String userId;

    @NotNull(message = "没有调用类型")
    private Integer apiType;

    @NotNull(message = "没有请求参数")
    private String requestJson;

    private String responseJson;

    private Integer status;

    private Integer retryCount;

    private Integer costTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
