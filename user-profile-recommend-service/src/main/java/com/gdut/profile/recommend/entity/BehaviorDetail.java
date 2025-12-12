package com.gdut.profile.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@TableName("behavior_detail")
public class BehaviorDetail {
    @TableId
    private String id;

    /**
     * 格式：user_id + 数字
     */
    @NotNull(message = "没有用户ID")
    private String userId;

    /**
     * 1 - 浏览 - expose
     * 2 - 点击 - click
     * 3 - 收藏 - collect
     * 4 - 分享 - share
     * 5 - 取消收藏 - uncollect
     * 6 - 停留 - stay
     * 7 - 不喜欢 - dislike
     */
    @NotNull(message = "行为类型不能为空")
    private Integer behaviorType;

    /**
     * 格式：item_id + 数字
     */
    @NotNull(message = "没有对象ID")
    private String targetId;

    /**
     * 1 - 文章 - article
     * 2 - 商品 - item
     * 3 - 图片 - image
     * 4 - 短视频 - shortvideo
     * 5 - 视频 - video
     * 6 - 音频 - audio
     * 7 - 菜谱 - recipe
     */
    @NotNull(message = "对象类型不能为空")
    private Integer targetType;

    private String behaviorContent;

    private String deviceNo;

    private String ipAddr;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 0 - 已删除
     * 1 - 正常
     */
    private Integer isValid;
}
