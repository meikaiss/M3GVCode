package com.m3gv.news.business.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.util.StringUtil;
import com.m3gv.news.common.webview.M3WebView;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by meikai on 16/12/15.
 */

public class ArticleNewsDetailActivity extends M3gBaseActivity {

    private static String KEY_ARTICLE_NEWS_ENTITY = "key_article_news_entity";

    private M3WebView m3WebView;
    private ArticleNewsEntity articleNewsEntity;

    public static void start(Activity activity, ArticleNewsEntity articleNewsEntity) {
        Intent intent = new Intent(activity, ArticleNewsDetailActivity.class);
        intent.putExtra(KEY_ARTICLE_NEWS_ENTITY, articleNewsEntity);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m3WebView = new M3WebView(this);
        setContentView(m3WebView);

        m3WebView.init();
        m3WebView.loadUrl("file:///android_asset/article/news.html");

        articleNewsEntity = getIntent().getParcelableExtra(KEY_ARTICLE_NEWS_ENTITY);

        // 获取完事的 html内容字符串
        String newsHtmlStr = ArticleHtmlFileUtil.createArticleHtml(this, articleNewsEntity.content);

        // 将完事的 html内容 写入到 缓存的html文件中
        String cacheFilePath = ArticleHtmlFileUtil.createNewArticle(this,
                new ByteArrayInputStream(newsHtmlStr.getBytes()));

        if (StringUtil.isNotEmpty(cacheFilePath)) {
            m3WebView.loadUrl("file://" + cacheFilePath);
        }
//        m3WebView.loadUrl("file:///data/data/com.m3gv.news/files/article/1/index.html");
    }


}
