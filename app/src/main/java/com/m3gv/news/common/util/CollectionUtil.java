package com.m3gv.news.common.util;

import java.util.List;

/**
 * Created by meikai on 16/12/4.
 */
public class CollectionUtil {

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(List list) {
        if (list == null) {
            return true;
        } else if (list.size() == 0) {
            return true;
        } else {
            return false;
        }
    }


}
