package com.m3gv.news.common.util;

import java.io.Closeable;

/**
 * Created by meikai on 16/12/18.
 */
public class IOUtil {

    public static void close(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                LogUtil.d("默认替换", e);
            }
        }
    }
}
