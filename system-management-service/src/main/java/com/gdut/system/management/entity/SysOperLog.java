package com.gdut.system.management.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author liujunliang
 * @date 2025/12/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("sys_oper_log")
public class SysOperLog {
    @TableId
    private String id;

    private String operUserId;

    private String operModule;

    private String operType;

    private String operContent;

    private String operIp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operTime;

    private Integer operResult;
}
