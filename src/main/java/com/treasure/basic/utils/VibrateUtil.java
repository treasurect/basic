package com.treasure.basic.utils;

import static android.Manifest.permission.VIBRATE;

import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.RequiresPermission;

import com.treasure.basic.utils.AppUtil;

/**
 * VibrateUtils
 *
 * @Description: 震动相关的工具类
 * @Markdown: https://github.com/Blankj/AndroidUtilCode/blob/master/lib/utilcode/README-CN.md
 * @Funtion: 相关方法如下所示
 * vibrate: 震动
 * cancel : 取消
 */
public final class VibrateUtil {

  private static Vibrator vibrator;

  private VibrateUtil() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Vibrate.
   * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
   *
   * @param milliseconds The number of milliseconds to vibrate.
   */
  @RequiresPermission(VIBRATE)
  public static void vibrate(final long milliseconds) {
    Vibrator vibrator = getVibrator();
    if (vibrator == null) return;
    vibrator.vibrate(milliseconds);
  }

  /**
   * Vibrate.
   * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
   *
   * @param pattern An array of longs of times for which to turn the vibrator on or off.
   * @param repeat  The index into pattern at which to repeat, or -1 if you don't want to repeat.
   */
  @RequiresPermission(VIBRATE)
  public static void vibrate(final long[] pattern, final int repeat) {
    Vibrator vibrator = getVibrator();
    if (vibrator == null) return;
    vibrator.vibrate(pattern, repeat);
  }

  /**
   * Cancel vibrate.
   * <p>Must hold {@code <uses-permission android:name="android.permission.VIBRATE" />}</p>
   */
  @RequiresPermission(VIBRATE)
  public static void cancel() {
    Vibrator vibrator = getVibrator();
    if (vibrator == null) return;
    vibrator.cancel();
  }

  private static Vibrator getVibrator() {
    if (vibrator == null) {
      vibrator = (Vibrator) AppUtil.getAppContext().getSystemService(Context.VIBRATOR_SERVICE);
    }
    return vibrator;
  }
}