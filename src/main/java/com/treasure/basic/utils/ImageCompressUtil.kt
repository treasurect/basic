package com.treasure.basic.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.treasure.basic.logger.CrashReport
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.min

/**
 *  Time : 2020/4/1
 *  Author : 霍昌峰
 *  Description : 获取压缩图片所需的参数
 *
 *  分辨率和文件大小都要处理的情况:
 *  1->获取原图bitmap option 看是否符合要求,不符合则压缩到与临界值最近且大于临界值的值
 *  2->采用矩阵缩放bitmap到要求的尺寸
 *  3->将修改的bitmap写入文件看看大小是否符合要求
 *  4->将不符合要求的再进行质量压缩
 */
class ImageCompressUtil(val file: File, private val compressType: Int = TYPE_MIDDLE) {

  companion object {
    const val TYPE_MIN = 0
    const val TYPE_MIDDLE = 1
    private const val COPY_NAME = "/compress_copy.jpg"
    private const val MIN_SIZE = 300 //缩略图临界尺寸
    private const val MIDDLE_SIZE = 1080 //中图临界尺寸
    private const val COMPRESS_RATIO = 50 //96时图片体积为之前的一半
    private const val NOT_COMPRESS = 1000
    private const val THIRTY_KB = 30 * 1024L
    private const val ONE_HUNDRED_KB = 100 * 1024L
  }

  var ratio = 100
  var bitmap: Bitmap? = null

  init {
    bitmap = compressImageJava()
  }

  /**
   * 通过缩放图片像素来减少图片占用内存大小
   * @return
   */
  private fun compressImageJava(): Bitmap? {

    //第一次采样
    val options: BitmapFactory.Options = BitmapFactory.Options()
    //该属性设置为true只会加载图片的边框进来，并不会加载图片具体的像素点
    options.inJustDecodeBounds = true
    //第一次加载图片，这时只会加载图片的边框进来，并不会加载图片中的像素点
    BitmapFactory.decodeFile(file.absolutePath, options)
    when(compressType) {
      TYPE_MIN -> {
        compressPxOption(MIN_SIZE, MIN_SIZE, options)
      }
      TYPE_MIDDLE -> {
        compressPxOption(MIDDLE_SIZE, MIDDLE_SIZE, options)
      }
      else -> {
        compressPxOption(MIDDLE_SIZE, MIDDLE_SIZE, options)
      }
    }
    val bitmap = BitmapFactory.decodeFile(file.absolutePath, options) ?: return null
    //加载图片并返回
    return if (options.inSampleSize == 1) { //分辨率没有处理过
      ratio = getCompressRatio(file, bitmap.width, bitmap.height) //直接用原文件计算大小
      bitmap
    } else { //分辨率需要处理
      zoomBitmap(bitmap)
    }
  }

  private fun compressPxOption(destWidth: Int, destHeight: Int, options: BitmapFactory.Options) {
    //获得原图的宽和高
    val outWidth = options.outWidth
    val outHeight = options.outHeight
    //定义缩放比例
    var sampleSize = 1
    while (outHeight / sampleSize > destHeight || outWidth / sampleSize > destWidth) {
      //如果宽高的任意一方的缩放比例没有达到要求，都继续增大缩放比例,
      //sampleSize应该为2的n次幂，如果给sampleSize设置的数字不是2的n次幂，那么系统会就近取值
      sampleSize *= 2
    }
    /** */
    //至此，第一次采样已经结束，我们已经成功的计算出了sampleSize的大小
    /** */
    //二次采样开始
    //二次采样时我需要将图片加载出来显示，不能只加载图片的框架，因此inJustDecodeBounds属性要设置为false
    options.inJustDecodeBounds = false
    //设置缩放比例,如果达到要求,说明已经小于临界值,变小了.所以取循环上一次的值
    options.inSampleSize = if (sampleSize > 1) sampleSize / 2 else sampleSize
    options.inPreferredConfig = Bitmap.Config.ARGB_8888
  }

  fun getCompressFolder(context: Context= AppUtil.getAppContext()): String = FileUtil.getExternalCacheDir().absolutePath

  /**
   * 根据图片分辨率大小判断其是否需要质量压缩
   * */
  private fun getCompressRatio(file: File, outWidth: Int, outHeight: Int): Int {
    when(compressType) {
      TYPE_MIN -> {
        return if (outWidth < MIN_SIZE && outHeight < MIN_SIZE) {
          handleSize(file, THIRTY_KB)
        } else if (outWidth > MIN_SIZE && outHeight > MIN_SIZE) {
          handleSize(file, THIRTY_KB * 2)
        } else {
          handleSize(file, THIRTY_KB * 3)
        }
      }
      TYPE_MIDDLE -> {
        return if (outWidth < MIDDLE_SIZE && outHeight < MIDDLE_SIZE) {
          handleSize(file, ONE_HUNDRED_KB)
        } else if (outWidth > MIDDLE_SIZE && outHeight > MIDDLE_SIZE) {
          handleSize(file, ONE_HUNDRED_KB * 2)
        } else {
          handleSize(file, ONE_HUNDRED_KB * 3)
        }
      }
      else -> {
        return NOT_COMPRESS
      }
    }
  }

  private fun handleSize(file: File, limit: Long): Int {
    return if (file.length() > limit) {
      COMPRESS_RATIO
    } else {
      NOT_COMPRESS
    }
  }

  private fun saveBitmap(bitmap: Bitmap, path: String): File {
    val file = File("$path$COPY_NAME")
    if (file.exists()) {
      file.delete()
    }
    val out: FileOutputStream
    try {
      out = FileOutputStream(file)
      if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
        out.flush()
        out.close()
      }
    } catch (e: FileNotFoundException) {
      CrashReport.postException(e)
      e.printStackTrace()
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return file
  }

  /**
   * 用于调整图片分辨率到合适的尺寸
   * */
  private fun zoomBitmap(bitmap: Bitmap): Bitmap {
    val matrix = Matrix()
    val width = bitmap.width
    val height = bitmap.height
    val min = min(width, height)
    val ratio = when(compressType) {
      TYPE_MIN -> {
        MIN_SIZE / min.toFloat()
      }
      TYPE_MIDDLE -> {
        MIDDLE_SIZE / min.toFloat()
      }
      else -> {
        1f
      }
    }
    matrix.postScale(ratio, ratio)
    val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    this.ratio = getCompressRatio(saveBitmap(newBitmap, getCompressFolder(AppUtil.getAppContext())), newBitmap.width,
      newBitmap.height)
    return newBitmap
  }

}