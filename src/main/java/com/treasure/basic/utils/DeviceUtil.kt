/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.os.StatFs

/**
 *
 * Create by SuoXuechao on 2021/5/21
 */
@SuppressLint("StaticFieldLeak")
object DeviceUtil {

  /**
   * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
   *
   * @return 平板返回 True，手机返回 False
   */
  @JvmStatic
  fun isPad(context: Context): Boolean {
    return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        >= Configuration.SCREENLAYOUT_SIZE_LARGE)
  }

  @JvmStatic
  fun platformSource(context: Context): String {
    return if (isPad(context)) "Android Pad" else "Android Phone"
  }

  @JvmStatic
  fun isSamsungDevice() = Build.MANUFACTURER.contains("samsung")

  @JvmStatic
  fun isHwMate() = Build.MODEL.contains("NOH-AN00") || Build.MODEL.contains("LIO-AL00")


  /**
   * 计算SD卡的剩余空间
   * @return 剩余空间
   */
  fun getSDAvailableSize(): Long {
    return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
      getAvailableSize(Environment.getExternalStorageDirectory().toString())
    } else 0
  }

  /**
   * 计算剩余空间
   * @param path
   * @return
   */
  private fun getAvailableSize(path: String): Long {
    val fileStats = StatFs(path)
    fileStats.restat(path)
    return fileStats.availableBlocks.toLong() * fileStats.blockSize // 注意与fileStats.getFreeBlocks()的区别
  }
}