package com.m3gv.news.common.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by meikai on 16/12/18.
 */
public class FileUtil {

    /**
     * 将资源文件的数据复制到新文件中
     *
     * @param assetsFilePath String 需要写入的资源文件路径
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(Context context, String assetsFilePath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            InputStream inStream = context.getAssets().open(assetsFilePath);
            if (inStream != null && inStream.available() > 0) {
                File newFile = new File(newPath);
                if (!newFile.exists()) {
                    if (newFile.isFile()) {

                    } else {
                        if (newFile.getParentFile() != null && !newFile.getParentFile().exists()) {
                            newFile.getParentFile().mkdirs();
                        }
                    }
                }
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            LogUtil.e("copyFile", e.getMessage());
        }
        return false;
    }

    /**
     * 创建文件夹，若父目录不存在，则自动递归创建父目录
     *
     * @param path 文件夹路径
     * @param overwrite 是否覆盖
     */
    public static boolean makeDirs(String path, boolean overwrite) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }

        File file = new File(path);
        if (file.exists()) {
            if (overwrite) {
                file.delete();
                file.mkdirs();
            }
        } else {
            file.mkdirs();
        }
        return true;
    }

    public static String writeFile(InputStream is, String newPath, String newName) {
        if (is == null || StringUtil.isEmpty(newPath) || StringUtil.isEmpty(newName)) {
            return null;
        }

        String newOne = newPath + newName;
        FileUtil.makeDirs(newPath, false);

        File newFile = new File(newOne);
        FileOutputStream fos = null;
        byte[] buffer = new byte[1024 * 4];
        int byteRead;
        try {
            if (newFile.exists()) {
                newFile.delete();
            }
            newFile.createNewFile();
            fos = new FileOutputStream(newFile);
            while ((byteRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            if (newFile.exists()) {
                newFile.delete();
            }
        } finally {
            IOUtil.close(is);
            IOUtil.close(fos);
        }

        try {
            return newFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
