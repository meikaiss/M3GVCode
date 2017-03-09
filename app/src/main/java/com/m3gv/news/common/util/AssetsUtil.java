package com.m3gv.news.common.util;

import com.m3gv.news.base.M3Config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by meikai on 17/3/9.
 */
public class AssetsUtil {

    private static final String TAG = "AssetsUtil";
    private static final String UTF_8 = "UTF-8";

    public static String readAssetFileContent(String filePath) {
        InputStream is = null;
        try {
            is = M3Config.getContext().getAssets().open(filePath);
            String content = readFromStream(is, UTF_8);
            return content;
        } catch (Exception ex) {
            LogUtil.d(TAG, ex);
        } finally {
            IOUtil.close(is);
        }
        return null;
    }

    /**
     * 从字符流中获取字符串，默认使用 utf-8 编码
     */
    public static String readFromStream(InputStream is) {
        return readFromStream(is, UTF_8);
    }

    public static String readFromStream(InputStream is, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
            //TODO 建议之后使用统一的 ByteArrayPool 避免内存碎片的产生
            char[] buffer = new char[1024];
            int length = -1;
            while ((length = br.read(buffer)) != -1) {
                sb.append(buffer, 0, length);
            }
        } catch (Exception ex) {
            LogUtil.d(TAG, ex);
        }
        return sb.toString();
    }
}
