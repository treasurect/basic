package com.treasure.basic.utils

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 *
 * Create by SuoXuechao on 2021/6/1
 */
object VideoMediaUtil {

  /**
   * 保存视频到媒体库
   */
  suspend fun saveVideoToMediaStore(file: File): Uri? = withContext(Dispatchers.IO) {

    var outputUri: Uri? = null
    if (! file.exists()) {
      return@withContext outputUri
    }

    val volumeName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.VOLUME_EXTERNAL_PRIMARY else MediaStore.VOLUME_EXTERNAL
    val contentUri = MediaStore.Video.Media.getContentUri(volumeName)
    val relativePath = "${Environment.DIRECTORY_DCIM}/ClassIn"
    val contentValue = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
      put(MediaStore.MediaColumns.TITLE, file.nameWithoutExtension)
      put(MediaStore.MediaColumns.MIME_TYPE, "video/*")

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        // 锁定占有
        put(MediaStore.Video.Media.IS_PENDING, 1)
      }else {
        val dataDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "ClassIn")
        if (!dataDir.exists()){
          dataDir.mkdirs()
        }
        put(MediaStore.MediaColumns.DATA, "${dataDir.absolutePath}/${file.name}")
      }
    }

    AppUtil.getAppContext().contentResolver.insert(contentUri, contentValue)?.let {videoUri ->

      try {
        AppUtil.getAppContext().contentResolver.openOutputStream(videoUri)?.use {output ->
          file.inputStream().use {input ->
            input.copyTo(output)
            output.flush()
          }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          //释放占有
          contentValue.clear()
          contentValue.put(MediaStore.Video.Media.IS_PENDING, 0)
          AppUtil.getAppContext().contentResolver.update(videoUri, contentValue, null, null)
        }
        outputUri = videoUri
      } catch (e: Exception) {
        e.printStackTrace()
        AppUtil.getAppContext().contentResolver.delete(videoUri, null, null)
      }
    }

    return@withContext outputUri
  }

}