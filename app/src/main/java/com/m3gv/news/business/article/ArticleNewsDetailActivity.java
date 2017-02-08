package com.m3gv.news.business.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.m3gv.news.R;
import com.m3gv.news.base.M3Config;
import com.m3gv.news.base.M3gBaseActivity;
import com.m3gv.news.common.util.UIUtil;
import com.m3gv.news.common.webview.M3WebView;

import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取完整的 html内容字符串
                String newsHtmlStr = ArticleHtmlFileUtil.createArticleHtml(ArticleNewsDetailActivity.this,
                        articleNewsEntity.articleTitle, articleNewsEntity.content);

                // 将完整的 html内容 写入到 缓存的html文件中
                final String cacheFilePath = ArticleHtmlFileUtil.createNewArticle(ArticleNewsDetailActivity.this,
                        new ByteArrayInputStream(newsHtmlStr.getBytes()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m3WebView.loadUrl("file://" + cacheFilePath);
                    }
                });
            }
        }).start();

        m3WebView.onImageClickListener = new M3WebView.OnImageClickListener() {
            @Override
            public void onImageClick(int index, String[] dataTypes) {
                Toast.makeText(M3Config.getCurrentActivity(), "index=" + index + ", " + Arrays.toString(dataTypes),
                        Toast.LENGTH_LONG).show();
            }
        };

        if (Math.random() > 0.5) {
            setupSlideableSpotAd();
        }
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

    @Override
    public void onBackPressed() {
        // 点击后退关闭轮播插屏广告
        if (SpotManager.getInstance(this).isSlideableSpotShowing()) {
            SpotManager.getInstance(this).hideSlideableSpot();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 轮播插屏广告
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 轮播插屏广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 轮播插屏广告
        SpotManager.getInstance(this).onDestroy();
    }

    /**
     * 设置轮播插屏广告
     */
    private void setupSlideableSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(this)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        SpotManager.getInstance(this)
                .showSlideableSpot(this, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.e("", "轮播插屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.e("", "轮播插屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                UIUtil.showToast("网络异常");
                                break;
                            case ErrorCode.NON_AD:
                                UIUtil.showToast("暂无轮播插屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                UIUtil.showToast("轮播插屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                UIUtil.showToast("请勿频繁展示");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                UIUtil.showToast("请设置插屏为可见状态");
                                break;
                            default:
                                UIUtil.showToast("请稍后再试");
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.e("", "轮播插屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.e("", "轮播插屏被点击");
                        Log.e("", String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                    }
                });
    }
}
