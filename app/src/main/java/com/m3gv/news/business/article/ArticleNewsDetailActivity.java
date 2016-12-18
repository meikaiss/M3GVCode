package com.m3gv.news.business.article;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.webview.M3WebView;

/**
 * Created by meikai on 16/12/15.
 */

public class ArticleNewsDetailActivity extends M3gBaseActivity {

    M3WebView m3WebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m3WebView = new M3WebView(this);
        setContentView(m3WebView);

        m3WebView.init();

//        m3WebView.loadUrl("file:///android_asset/webview/demo/testV1.0.html");

        m3WebView.loadUrl("file:///android_asset/article/news.html");
    }


}
