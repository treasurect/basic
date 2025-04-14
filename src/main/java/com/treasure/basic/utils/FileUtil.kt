/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.utils

import android.content.Context
import android.text.TextUtils
import java.io.File

/**
 *
 * Create by SuoXuechao on 2021/5/21
 */
object FileUtil {

  private val logger = LoggerFactory.getLogger(javaClass.simpleName)
  private val mContext: Context by lazy {AppUtil.getAppContext()}
  private const val SIZE_TYPE_B = 1 //文件大小单位为B
  private const val SIZE_TYPE_KB = 2 // 文件大小单位为KB
  private const val SIZE_TYPE_MK = 3 // 文件大小单位为MB
  private const val SIZE_TYPE_GB = 4 // 文件大小单位为GB

  private const val DIR_DOCUMENTS = "Documents"
  private const val DIR_PICTURES = "Pictures"
  private const val DIR_THUMBNAILS = "Thumbnails"
  private const val DIR_MICRO_LECTURE = "MicroLecture"
  private const val DIR_EMOTICONS = "Emoticons"
  private const val DIR_VOICES = "Voices"
  private const val DIR_VIDEOS = "Videos"
  private const val DIR_TEMPS = "Temps"
  private const val DIR_Hide = ".Hide"


  /**
   * sdcard data目录下的cache目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalCacheDir(type: String? = null): File {
    return if (type == null) {
      mContext.externalCacheDir ?: mContext.cacheDir
    } else {
      File(mContext.externalCacheDir ?: mContext.cacheDir, type).apply {
        if (! exists()) {
          mkdirs()
        }
      }
    }
  }

  /**
   * sdcard data目录下的hide目录
   */
  @JvmStatic
  fun getExternalHideDir() = getExternalFilesDir(DIR_Hide)

  /**
   * sdcard data目录下的file目录
   * /storage/emulated/0/Android/data/cn.eeo.classinfv/files
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalFilesDir(type: String? = null): File {
    return if (type == null) {
      mContext.getExternalFilesDir(type) ?: mContext.filesDir
    } else {
      File(mContext.getExternalFilesDir(null) ?: mContext.filesDir, type).apply {
        if (! exists()) {
          mkdirs()
        }
      }
    }
  }

  /**
   * 文件存储目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalDocumentsDir() = getExternalFilesDir(DIR_DOCUMENTS)

  /**
   * 图片存储目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalPicturesDir() = getExternalFilesDir(DIR_PICTURES)

  /**
   * 缩略图存储目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalThumbnailsDir() = getExternalFilesDir(DIR_THUMBNAILS)

  @JvmStatic
  fun getExternalMicroLectureDir() = getExternalFilesDir(DIR_MICRO_LECTURE)

  /**
   * 表情存储目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalEmoticonsDir() = getExternalFilesDir(DIR_EMOTICONS)

  /**
   * 声音存储目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalVoicesDir() = getExternalFilesDir(DIR_VOICES)

  /**
   * 视频存储目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalVideosDir() = getExternalFilesDir(DIR_VIDEOS)

  /**
   * 临时文件目录
   * @return (java.io.File..java.io.File?)
   */
  @JvmStatic
  fun getExternalTempsDir() = getExternalFilesDir(DIR_TEMPS)


  fun createFile(path: String?): Boolean {
    if (TextUtils.isEmpty(path)) {
      return false
    }
    val file = File(path)
    return file.exists() || file.mkdirs()
  }
}