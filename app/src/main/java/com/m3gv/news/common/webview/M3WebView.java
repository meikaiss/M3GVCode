package com.m3gv.news.common.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.m3gv.news.base.M3Config;

import java.util.Arrays;

/**
 * Created by meikai on 16/5/15.
 */
public class M3WebView extends WebView {


    private boolean online = true;

    private M3WebViewInterface m3WebViewInterface;
    private IM3WebProtocolHandler m3Protocol;


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
        this.setWebViewClient(new WebViewClient());

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
        public void clickArticleImage(String index) {
            Toast.makeText(M3Config.getCurrentActivity(), "" + index, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void setArticleImageArr(String[] dataTypes) {
            Log.e("setArticleImageArr", (dataTypes == null ? "null" : Arrays.toString(dataTypes)));
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

}
