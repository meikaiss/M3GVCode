package com.m3gv.news.common.util;

/**
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
}
