package com.m3gv.news.common.util;

/**
 * Created by meikai on 16/12/3.
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return (str == null) || str.length() == 0;
    }

    public static boolean isNotEmpty(String source) {
        return !isEmpty(source);
    }

}
