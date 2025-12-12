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
@TableName("user_tag")
public class UserTag {
    @TableId
    private String id;

    /**
     * 格式：user_id + 数字
     */
    @NotNull(message = "没有用户ID")
    private String userId;

    @NotNull(message = "没有标签JSON")
    private String tagJson;

    private String tagWeightJson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 0 - 定时更新
     * 1 - 触发更新
     */
    private Integer updateType;
}
