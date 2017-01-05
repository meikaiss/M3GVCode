package com.m3gv.news.common.util;

/**
 * 计量单位 换算
 * Created by meikai on 16/12/4.
 */
public class UnitUtil {

    public static String toWan(int count) {
        if (count < 10000) {
            return String.valueOf(count);
        } else {
            return count / 10000 + "." + (count % 10000) / 1000 + "万";
        }
    }

    /**
     * 将比特速率转换为字节速率
     * 1KByte/s＝8Kbps
     */
    public static String toKByteSpeed(int bitSpeed) {
        return String.valueOf(bitSpeed / 8);
    }
}
