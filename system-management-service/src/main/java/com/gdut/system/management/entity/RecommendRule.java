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
@TableName("recommend_rule")
public class RecommendRule {
    @TableId
    private String id;

    private String ruleName;

    private Integer scene;

    private String tagWeightJson;

    private String modeRatioJson;

    private Integer cacheExpire;

    private Integer isEnable;

    private String createUserId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
