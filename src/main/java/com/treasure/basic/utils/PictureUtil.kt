/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import android.view.View
import com.treasure.basic.logger.CrashReport
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 *
 * Create by SuoXuechao on 2021/5/21
 */
object PictureUtil {

  /**
   * 保存Bitmap
   */
  @JvmStatic
  fun saveBitmap(mBitmap: Bitmap): String? {
    val savePath = "/sdcard/ClassIn/"
    val filePic: File
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
      try {
        filePic = File(savePath + UUID.randomUUID().toString() + ".jpg")
        if (! filePic.exists()) {
          filePic.parentFile.mkdirs()
          filePic.createNewFile()
        }
        val fos = FileOutputStream(filePic)
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
      } catch (e: IOException) {
        // TODO Auto-generated catch block
        CrashReport.postException(e)
        e.printStackTrace()
        return null
      }
      return filePic.absolutePath
    }
    return null
  }

  @JvmStatic
  fun snapshot(sv: View, dstWidth: Int, dstHeight: Int): ByteArray? {
    sv.isDrawingCacheEnabled = true
    val src = sv.drawingCache
    var dst: Bitmap? = null
    if (src != null && ! src.isRecycled) {
      dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true)
    }
    sv.isDrawingCacheEnabled = false
    var snapshot: ByteArray? = null
    if (dst != null) {
      val baos = ByteArrayOutputStream()
      dst.compress(Bitmap.CompressFormat.JPEG, 100, baos)
      snapshot = baos.toByteArray()
      try {
        baos.close()
      } catch (e: IOException) {
        CrashReport.postException(e)
        e.printStackTrace()
      }
      dst.recycle()
    }
    return snapshot
  }

  /**
   * @param x      横向截取的起点
   * @param y      竖向截取的起点
   * @param width  截取的宽度
   * @param height 截取的高度
   */
  @Throws(RuntimeException::class)
  @JvmStatic
  fun snapshotWithCrop(sv: View, x: Int, y: Int, width: Int, height: Int): ByteArray? {
    var width = width
    sv.isDrawingCacheEnabled = true
    val src = sv.drawingCache
    var dst: Bitmap? = null
    if (src != null) {
      if (src.width == 0 || src.width == 0) {
        throw RuntimeException()
      }
      if (x + width > src.width) {
        width = src.width - x
      }
      dst = Bitmap.createBitmap(src, x, y, width, height)
    }
    sv.isDrawingCacheEnabled = false
    var snapshot: ByteArray? = null
    if (dst != null) {
      val baos = ByteArrayOutputStream()
      dst.compress(Bitmap.CompressFormat.JPEG, 100, baos)
      snapshot = baos.toByteArray()
      try {
        baos.close()
      } catch (e: IOException) {
        CrashReport.postException(e)
        e.printStackTrace()
      }
      dst.recycle()
    }
    return snapshot
  }

  @JvmStatic
  fun saveSnapShotBitmap(sv: View, dstWidth: Int, dstHeight: Int): String? {
    sv.isDrawingCacheEnabled = true
    sv.destroyDrawingCache()
    val src = sv.drawingCache
    var dst: Bitmap? = null
    if (src != null && ! src.isRecycled) {
      dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true)
      src.recycle()
    }
    sv.isDrawingCacheEnabled = false
    val filePic: File
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED && dst != null) {
      try {
        filePic = File(AppUtil.getAppContext().externalCacheDir !!.absolutePath + UUID.randomUUID().toString() + ".jpg")
        if (! filePic.exists()) {
          filePic.parentFile.mkdirs()
          filePic.createNewFile()
        }
        val fos = FileOutputStream(filePic)
        dst.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
      } catch (e: IOException) {
        CrashReport.postException(e)
        e.printStackTrace()
        return null
      }
      return filePic.absolutePath
    }
    return null
  }


  /**
   * 计算出图片初次显示需要放大倍数
   * @param imagePath 图片的绝对路径
   */
  @JvmStatic
  fun getImageScale(context: Context, imagePath: String?): Float {
    if (TextUtils.isEmpty(imagePath)) {
      return 2.0f
    }
    var bitmap: Bitmap? = null
    try {
      bitmap = BitmapFactory.decodeFile(imagePath)
    } catch (error: OutOfMemoryError) {
      CrashReport.postException(error)
      error.printStackTrace()
    }
    if (bitmap == null) {
      return 2.0f
    }

    // 拿到图片的宽和高
    val dw = bitmap.width
    val dh = bitmap.height
    val wm = (context as Activity).windowManager
    val width = wm.defaultDisplay.width
    val height = wm.defaultDisplay.height
    var scale = 1.0f
    //图片宽度大于屏幕，但高度小于屏幕，则缩小图片至填满屏幕宽
    if (dw > width && dh <= height) {
      scale = width * 1.0f / dw
    }
    //图片宽度小于屏幕，但高度大于屏幕，则放大图片至填满屏幕宽
    if (dw <= width && dh > height) {
      scale = width * 1.0f / dw
    }
    //图片高度和宽度都小于屏幕，则放大图片至填满屏幕宽
    if (dw < width && dh < height) {
      scale = width * 1.0f / dw
    }
    //图片高度和宽度都大于屏幕，则缩小图片至填满屏幕宽
    if (dw > width && dh > height) {
      scale = width * 1.0f / dw
    }
    bitmap.recycle()
    return scale
  }
}