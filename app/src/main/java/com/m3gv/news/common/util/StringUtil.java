package com.m3gv.news.common.util;

/**
 * Created by meikai on 16/12/3.
 */

public class StringUtil {

    public static boolean isEmpty(String source) {
        if (source == null) {
            return true;
        } else if (source.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String source) {
        return !isEmpty(source);
    }

}
