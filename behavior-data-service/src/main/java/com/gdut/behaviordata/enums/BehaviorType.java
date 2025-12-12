package com.gdut.behaviordata.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author liujunliang
 * @date 2025/12/3
 */
@Getter
public enum BehaviorType {
    /**
     * 浏览行为：用户打开内容详情页（如商品、文章）
     */
    VIEW(1, "浏览行为"),

    /**
     * 点击行为：用户点击推荐列表、按钮、链接
     */
    CLICK(2, "点击行为"),

    /**
     * 收藏行为：用户收藏目标内容（如收藏商品、小说）
     */
    COLLECT(3, "收藏行为"),

    /**
     * 分享行为：用户分享内容至第三方平台
     */
    SHARE(4, "分享行为"),

    /**
     * 取消收藏：用户取消已收藏的内容
     */
    CANCEL_COLLECT(5, "取消收藏"),

    /**
     * 停留行为：用户在页面停留超过30秒（需前端上报）
     */
    STAY(6, "停留行为");

    /**
     * 数据库存储值（整数类型，不可修改）
     */
    private final Integer code;

    /**
     * 行为描述（业务含义，用于日志打印、前端展示）
     */
    private final String desc;

    /**
     * 私有构造方法（枚举类构造方法必须私有）
     */
    BehaviorType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 工具方法：根据数据库存储值（code）获取枚举实例
     * @param code 数据库存储的行为类型值（1-6）
     * @return 对应的枚举实例，无匹配值则抛出 IllegalArgumentException
     */
    public static BehaviorType getByCode(Integer code) {
        if (Objects.isNull(code)) {
            throw new IllegalArgumentException("行为类型code不能为空");
        }
        // 遍历枚举，匹配code
        return Arrays.stream(values())
                .filter(enumItem -> enumItem.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("非法行为类型code：" + code));
    }

    /**
     * 工具方法：校验code是否为合法的行为类型
     * @param code 待校验的code
     * @return true=合法，false=非法
     */
    public static boolean isValidCode(Integer code) {
        if (Objects.isNull(code)) {
            return false;
        }
        // 遍历枚举，判断是否存在匹配的code
        return Arrays.stream(values())
                .anyMatch(enumItem -> enumItem.getCode().equals(code));
    }
}
