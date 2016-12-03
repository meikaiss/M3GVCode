package com.m3gv.news.common.util;

/**
 * Created by meikai on 16/12/3.
 */

public class TimeUtil {

    public static String toDurationString(int totalSecond) {
        int hourTemp = totalSecond / (60 * 60);
        int minuteTemp = totalSecond % (60 * 60) / 60;
        int secondTemp = totalSecond % 60;
        return hourTemp % 24 + ":" + String.format("%02d", minuteTemp % 60) + ":" + String
                .format("%02d", secondTemp % 60);
    }
}
