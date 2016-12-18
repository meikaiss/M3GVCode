package com.m3gv.news.business.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.m3gv.news.R;
import com.m3gv.news.base.M3Config;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.util.StringUtil;
import com.m3gv.news.common.webview.M3WebView;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

/**
 * Created by meikai on 16/12/15.
 */
public class ArticleNewsDetailActivity extends M3gBaseActivity implements View.OnClickListener {

    private static String KEY_ARTICLE_NEWS_ENTITY = "key_article_news_entity";

    private ImageView imageViewBack;
    private M3WebView m3WebView;
    private ArticleNewsEntity articleNewsEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_activity);

        imageViewBack = (ImageView) findViewById(R.id.img_btn_back);
        imageViewBack.setOnClickListener(this);
        m3WebView = (M3WebView) findViewById(R.id.article_m3_web_view);

        m3WebView.init();
        m3WebView.loadUrl("file:///android_asset/article/news.html");

        articleNewsEntity = getIntent().getParcelableExtra(KEY_ARTICLE_NEWS_ENTITY);

        // 获取完事的 html内容字符串
        String newsHtmlStr = ArticleHtmlFileUtil.createArticleHtml(this, articleNewsEntity.content);

        // 将完整的 html内容 写入到 缓存的html文件中
        String cacheFilePath = ArticleHtmlFileUtil.createNewArticle(this,
                new ByteArrayInputStream(newsHtmlStr.getBytes()));

        if (StringUtil.isNotEmpty(cacheFilePath)) {
            m3WebView.loadUrl("file://" + cacheFilePath);
        }

        m3WebView.onImageClickListener = new M3WebView.OnImageClickListener() {
            @Override
            public void onImageClick(int index, String[] dataTypes) {
                Toast.makeText(M3Config.getCurrentActivity(), "index=" + index + ", " + Arrays.toString(dataTypes),
                        Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    public static void start(Activity activity, ArticleNewsEntity articleNewsEntity) {
        Intent intent = new Intent(activity, ArticleNewsDetailActivity.class);
        intent.putExtra(KEY_ARTICLE_NEWS_ENTITY, articleNewsEntity);
        activity.startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_btn_back:
                this.onBackPressed();
                break;
            default:
                break;
        }
    }

}
