package com.gdut.profile.recommend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liujunliang
 * @date 2025/12/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("item")
public class Item {
    // 格式：item_id + 随机数
    @TableId
    private String id;

    private String tags;

    private String categoryPath;

    /**
     * 1 - 文章 - article
     * 2 - 商品 - item
     * 3 - 图片 - image
     * 4 - 短视频 - shortvideo
     * 5 - 视频 - video
     * 6 - 音频 - audio
     * 7 - 菜谱 - recipe
     */
    private Integer itemType;
}
