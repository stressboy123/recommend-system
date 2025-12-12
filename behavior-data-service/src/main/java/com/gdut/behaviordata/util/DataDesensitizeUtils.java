package com.gdut.behaviordata.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataDesensitizeUtils {
    /**
     * 设备号脱敏：保留后4位，前面全部替换为*
     *
     * @param deviceNo 原始设备号
     * @return 脱敏后的设备号
     */
    public static String desensitizeDeviceNo(String deviceNo) {
        // 空值/空白字符串直接返回
        if (isBlank(deviceNo)) {
            return deviceNo;
        }

        int length = deviceNo.length();
        // 长度≤4时，直接返回原设备号（避免过度脱敏）
        if (length <= 4) {
            return deviceNo;
        }

        // 截取后4位，前面补充对应长度的*
        String suffix = deviceNo.substring(length - 4);
        return repeat("*", length - 4) + suffix;
    }

    /**
     * IP地址脱敏：隐藏中间两段（第二、三段）为xxx
     *
     * @param ipAddr 原始IP地址
     * @return 脱敏后的IP地址
     */
    public static String desensitizeIp(String ipAddr) {
        // 空值/空白字符串直接返回
        if (isBlank(ipAddr)) {
            return ipAddr;
        }

        // 按.分割IP段（注意转义，.在正则中是任意字符）
        String[] ipSegments = ipAddr.split("\\.");
        // 非四段IP，直接返回原IP
        if (ipSegments.length != 4) {
            return ipAddr;
        }

        // 简易校验每段是否为纯数字（避免非法IP格式）
        for (String segment : ipSegments) {
            if (!isNumeric(segment)) {
                return ipAddr;
            }
        }

        // 拼接脱敏后的IP：第一段 + .xxx.xxx. + 第四段
        return ipSegments[0] + ".xxx.xxx." + ipSegments[3];
    }

    private static boolean isNumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        // 遍历字符，判断是否全为数字
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static String repeat(String str, int times) {
        if (times <= 0 || str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(times * str.length());
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
