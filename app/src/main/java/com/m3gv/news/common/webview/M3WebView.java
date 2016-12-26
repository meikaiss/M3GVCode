package com.m3gv.news.common.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.m3gv.news.base.M3Config;
import com.m3gv.news.common.util.LogUtil;

import java.util.Arrays;

/**
 * Created by meikai on 16/5/15.
 */
public class M3WebView extends WebView {


    private boolean online = true;

    private M3WebViewInterface m3WebViewInterface;
    private IM3WebProtocolHandler m3Protocol;

    public String[] dataTypes;

    public OnImageClickListener onImageClickListener;

    public M3WebView(Context context) {
        super(context);
    }

    public M3WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public M3WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public M3WebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void init() {

        this.getSettings().setJavaScriptEnabled(true);
        this.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        this.m3Protocol = new M3ProtocolV1Router();

        this.m3WebViewInterface = new M3WebViewInterface();

        addJavascriptInterface(m3WebViewInterface, "m3WebViewInterface");
    }

    class M3WebViewInterface {

        @JavascriptInterface
        public String getM3WebViewData(String url, final String callback) {

            Uri uri = Uri.parse(url);
            m3Protocol.handleProtocol(uri);

            return "test1";
        }

        @JavascriptInterface
        public String getM3WebViewData(String url) {

            Uri uri = Uri.parse(url);

            return m3Protocol.handleProtocol(uri);
        }

        @JavascriptInterface
        public void clickArticleImage(String indexStr) {
            if (onImageClickListener != null) {
                int index;
                try {
                    index = Integer.parseInt(indexStr);
                    onImageClickListener.onImageClick(index, M3WebView.this.dataTypes);
                } catch (Exception e) {
                    LogUtil.e("clickArticleImage", e.getMessage());
                }
            }
        }

        @JavascriptInterface
        public void setArticleImageArr(String[] dataTypes) {
            M3WebView.this.dataTypes = dataTypes;
            LogUtil.e("setArticleImageArr", (dataTypes == null ? "null" : Arrays.toString(dataTypes)));
        }
    }


    private void convulsions() {
        this.post(new Runnable() {
            @Override
            public void run() {
                M3WebView.this.setNetworkAvailable(online = !online);
            }
        });
    }


    public interface OnImageClickListener {
        void onImageClick(int index, String[] dataTypes);
    }

}
