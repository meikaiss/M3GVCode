package com.m3gv.news.common.util;

import java.util.Collection;

/**
 * Created by meikai on 16/12/4.
 */
public class CollectionUtil {

    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    
    public static boolean isEmpty(Collection collection) {
        if (collection == null) {
            return true;
        } else if (collection.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
