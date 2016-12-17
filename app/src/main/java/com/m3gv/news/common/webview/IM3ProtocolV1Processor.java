package com.m3gv.news.common.webview;

/**
 * Created by meikai on 16/5/26.
 */
public interface IM3ProtocolV1Processor {

    String version();

    void toast(String message);

    void log(String message);


    void action(String message);
}
