package com.m3gv.news.common.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by meikai on 16/12/18.
 */

public class StorageUtil {

    public static long getAvailableInternalStorageSize() {
        File file = Environment.getDataDirectory();
        if(file != null && file.exists()) {
            StatFs sdFs = new StatFs(file.getPath());
            if(sdFs != null) {
                long sdBlockSize = (long)sdFs.getBlockSize();
                long sdAvailCount = (long)sdFs.getAvailableBlocks();
                return sdAvailCount * sdBlockSize;
            }
        }

        return 0L;
    }

    public static boolean checkAvailableInternalStorage(long size) {
        long availabelStorage = getAvailableInternalStorageSize();
        return size < 0L?true:(availabelStorage <= 0L?false:availabelStorage >= size);
    }
}
