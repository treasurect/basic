package com.treasure.basic.utils;

import android.util.Log;

public class LogHelper {
    private static final String DEFAULT_TAG = "LedgerAPP";
    private static boolean isDebug = true; // 是否开启日志

    // 设置日志开关
    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

    // Verbose 日志
    public static void logV(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void logV(String msg) {
        logV(DEFAULT_TAG, msg);
    }

    // Debug 日志
    public static void logD(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void logD(String msg) {
        logD(DEFAULT_TAG, msg);
    }

    // Info 日志
    public static void logI(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void logI(String msg) {
        logI(DEFAULT_TAG, msg);
    }

    // Warn 日志
    public static void logW(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void logW(String msg) {
        logW(DEFAULT_TAG, msg);
    }

    // Error 日志
    public static void logE(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void logE(String msg) {
        logE(DEFAULT_TAG, msg);
    }
}

