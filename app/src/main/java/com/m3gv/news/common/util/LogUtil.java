package com.m3gv.news.common.util;

import android.util.Log;

import com.m3gv.news.base.M3Config;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 此类维护输出日志相关的工具方法。
 * 代替系统的 {@link Log} 为了做到更好的控制和扩展业务。
 */
public class LogUtil {

    private LogUtil() {
    }

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int LEVEL_VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int LEVEL_DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int LEVEL_INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int LEVEL_WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int LEVEL_ERROR = 6;
    /**
     * 表示关闭日志.
     */
    public static final int LEVEL_OFF = Integer.MAX_VALUE;

    private static class LevelHolder {
        static volatile int level = M3Config.isDebug() ? LEVEL_DEBUG : LEVEL_ERROR;
    }

    public static synchronized void setLevel(int newLevel) {
        LevelHolder.level = newLevel;
    }

    public static int getLevel() {
        return LevelHolder.level;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        if (LEVEL_VERBOSE >= getLevel()) {
            msg = validateMessage(msg);
            return Log.v(tag, msg);
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        if (LEVEL_DEBUG >= getLevel()) {
            msg = validateMessage(msg);
            return Log.d(tag, msg);
        }
        return 0;
    }

    /**
     * debug级别打印出tr的stacktrace
     */
    public static int d(String tag, Throwable tr) {
        if (LEVEL_DEBUG >= getLevel()) {
            return Log.d(tag, getStackTraceString(tr));
        }
        return 0;
    }

    /**
     * debug级别打印出tr的stacktrace
     */
    public static int d(String tag, String msg, Throwable tr) {
        if (LEVEL_DEBUG >= getLevel()) {
            msg = validateMessage(msg);
            return Log.d(tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        if (LEVEL_INFO >= getLevel()) {
            msg = validateMessage(msg);
            return Log.i(tag, msg);
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        if (LEVEL_INFO >= getLevel()) {
            msg = validateMessage(msg);
            return Log.i(tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        if (LEVEL_WARN >= getLevel()) {
            msg = validateMessage(msg);
            return Log.w(tag, msg);
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        if (LEVEL_WARN >= getLevel()) {
            msg = validateMessage(msg);
            return Log.w(tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    /*
     * Send a {@link #WARN} log message and log the exception.
     * 
     * @param tag Used to identify the source of a log message. It usually identifies the class or activity where the
     * log call occurs.
     * 
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
        if (LEVEL_WARN >= getLevel()) {
            return Log.w(tag, getStackTraceString(tr));
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        if (LEVEL_ERROR >= getLevel()) {
            msg = validateMessage(msg);
            return Log.e(tag, msg);
        }
        return 0;
    }

    /**
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        if (LEVEL_ERROR >= getLevel()) {
            msg = validateMessage(msg);
            return Log.e(tag, msg + '\n' + getStackTraceString(tr));
        }
        return 0;
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable.
     *
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 检查msg是否为空，如果为空则替换为默认提示
     * @param msg
     * @return
     */
    private static String validateMessage(String msg) {
        return msg == null ? "Log message can't be null." : msg;
    }
}
