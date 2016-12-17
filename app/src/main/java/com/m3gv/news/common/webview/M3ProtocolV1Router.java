package com.m3gv.news.common.webview;

import android.net.Uri;
import android.util.Log;

/**
 * Created by meikai on 16/5/15.
 */
public class M3ProtocolV1Router implements IM3WebProtocolHandler {

    private String host;
    private String path;
    private String params;

    {
        setIM3ProtocolV1Processor(new M3ProtocolV1ProcessorImpl());
    }

    private static IM3ProtocolV1Processor mikeProtocolV1Processor;

    public static void setIM3ProtocolV1Processor(IM3ProtocolV1Processor IMikeProtocolV1Helper) {
        mikeProtocolV1Processor = IMikeProtocolV1Helper;
    }

    @Override
    public String handleProtocol(Uri uri) {

        host = uri.getHost();
        path = uri.getPath();

        String result = "";

        if ("system".equals(host)) {
            if ("/version".equals(path)) {
                result = mikeProtocolV1Processor.version();
            } else if ("/toast".equals(path)) {
                params = uri.getQueryParameter("message");
                mikeProtocolV1Processor.toast(params);
            } else if ("/log".equals(path)) {
                params = uri.getQueryParameter("message");
                mikeProtocolV1Processor.log(params);
            }
        }

        if ("jump".equals(host)) {
            if ("/action".equals(path)) {
                params = uri.getQueryParameter("message");
                mikeProtocolV1Processor.action(params);
            }
        }

        return result;
    }


}
