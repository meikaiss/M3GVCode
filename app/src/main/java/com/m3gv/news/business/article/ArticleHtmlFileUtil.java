package com.m3gv.news.business.article;

import android.content.Context;

import com.m3gv.news.M3gApplication;
import com.m3gv.news.common.util.FileUtil;
import com.m3gv.news.common.util.IOUtil;
import com.m3gv.news.common.util.LogUtil;
import com.m3gv.news.common.util.StorageUtil;
import com.m3gv.news.common.util.UIUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by meikai on 16/12/18.
 */

public class ArticleHtmlFileUtil {


    /**
     * 获取新闻html缓存文件的存储根路径
     * 如：/data/data/com.m3gv.news/article/index.html
     */
    public static String getArticleRootPath() {
        return M3gApplication.application.getFilesDir().getAbsolutePath() + File.separator + "articleCache"
                + File.separator;
    }

    /**
     * 生成新的文章
     *
     * @param content 文章内容
     */
    public static String createNewArticle(Context context, InputStream content) {
        String path = getArticleRootPath();
        String cacheFilePath;
        boolean successful = FileUtil.makeDirs(path, false);
        if (successful) {
            // 先清除上一篇文章新闻的缓存文件
            File cacheFile = new File(path + "index.html");
            if (cacheFile.exists()) {
                cacheFile.delete();
            }

            // 创建新缓存文件，并写入数据
            cacheFilePath = FileUtil.writeFile(content, path, "index.html");

            // 若js、css文件不存在，则将js、css文件写入到缓存目录
            File cacheFileDir = new File(path);
            if (cacheFileDir.listFiles().length < 5) {
                // 1个缓存的html文件 ，4个js、css文件
                FileUtil.copyFile(context, "article/news.css", path + "news.css");
                FileUtil.copyFile(context, "article/clickFuncBind.js", path + "clickFuncBind.js");

                FileUtil.copyFile(context, "webview/source/core.js", path + "webview/source/core.js");
                FileUtil.copyFile(context, "webview/source/router.js", path + "webview/source/router.js");
            }

            // 判断 剩余空间 是否 小于 1M
            if (!StorageUtil.checkAvailableInternalStorage(1L * 1024 * 1024)) {
                UIUtil.showToast("存储卡剩余空间不足啦, 快去清理一下吧");
            }
        } else {
            return null;
        }
        return cacheFilePath;
    }

    public static String createArticleHtml(Context context, String title, String body) {
        //模板的绝对路径为： assests/article/news.html
        return createArticleHtml(context, "article/news.html", title, body);
    }

    /**
     * 为body新闻体生成完事的html实体
     *
     * @param context 上下文
     * @param fileName 模板html文件的相对路径, 相对assets文件夹
     * @param body html的body内容
     * @return 返回新闻完事的html实体
     */
    public static String createArticleHtml(Context context, String fileName, String title, String body) {
        InputStream is = null;
        BufferedReader br = null;
        String str = "";
        String newStr;
        try {
            String tempStr;
            is = context.getAssets().open(fileName);
            br = new BufferedReader(new InputStreamReader(is));
            while ((tempStr = br.readLine()) != null) {
                str = str + tempStr;
            }
            newStr = str.replace("###title###", title);
            newStr = newStr.replace("###body###", body);
            return newStr;
        } catch (Exception e) {
            LogUtil.e("createArticleHtml", e.getMessage());
        } finally {
            IOUtil.close(is);
            IOUtil.close(br);
        }
        return null;
    }

}
