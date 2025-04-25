/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Size
import com.treasure.basic.entity.BucketMediaEntity
import com.treasure.basic.entity.ImageMediaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 图片媒体库操作工具
 * Create by SuoXuechao on 2021/5/21
 */
object ImageMediaUtil {

  val MIME_TYPE_IMAGE_JPEG = "image/jpeg"
  val MIME_TYPE_IMAGE_PNG = "image/png"
  val MIME_TYPE_IMAGE_JPG = "image/jpg"
  val MIME_TYPE_IMAGE_GIF = "image/gif"

  /**
   * 加载缩略图
   */
  suspend fun loadThumbnail(uri: Uri, size: Size) = withContext(Dispatchers.IO) {
    try {
      return@withContext if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        AppUtil.getAppContext().contentResolver.loadThumbnail(uri, size, null)
      } else {
        val options = BitmapFactory.Options().apply {
          outWidth = size.width
          outHeight = size.height
        }
        MediaStore.Images.Thumbnails.getThumbnail(
          AppUtil.getAppContext().contentResolver, uri.lastPathSegment !!.toLong(),
          MediaStore.Images.Thumbnails.MICRO_KIND, options
        )
      }
    } catch (e: Exception) {
      
      e.printStackTrace()
    }
    return@withContext null
  }

  /**
   * 加载相册
   */
  @SuppressLint("Range")
  suspend fun loadBuckets() = withContext(Dispatchers.IO) {
    var cursor: Cursor? = null
    try {
      val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
      val projection = arrayOf(MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
      cursor = AppUtil.getAppContext().contentResolver.query(uri, projection, null, null, null)

      if (cursor != null) {
        val buckets = mutableListOf<BucketMediaEntity>()
        while (cursor.moveToNext()) {
          val bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
          val bucketName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
          if (bucketId != null && bucketName != null) {
            buckets.add(BucketMediaEntity(bucketId, bucketName))
          }
        }
        if (buckets.isNotEmpty()) {
          return@withContext buckets.distinctBy {it.bucketId}
        }
      }

    } catch (e: Exception) {
      
      e.printStackTrace()
    } finally {
      cursor?.close()
    }
    return@withContext emptyList()
  }

  /**
   * 加载媒体库图片
   */
  @SuppressLint("Range")
  suspend fun loadImages(bucketId: String): List<ImageMediaEntity> = withContext(Dispatchers.IO) {
    val SELECTION_MIME_TYPE =
      "${MediaStore.Images.Media.MIME_TYPE}=? or ${MediaStore.Images.Media.MIME_TYPE}=? or ${MediaStore.Images.Media.MIME_TYPE}=?"
    val SELECTION_WITH_BUCKET_ID = "${MediaStore.Images.Media.BUCKET_ID} =? and ( $SELECTION_MIME_TYPE )"

    val images = mutableListOf<ImageMediaEntity>()
    var cursor: Cursor? = null

    try {
      val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.TITLE,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.DATE_MODIFIED
      )
      val selection = if (bucketId.isBlank()) SELECTION_MIME_TYPE else SELECTION_WITH_BUCKET_ID
      val selectionArgs = if (bucketId.isBlank()) arrayOf(MIME_TYPE_IMAGE_JPG, MIME_TYPE_IMAGE_JPEG, MIME_TYPE_IMAGE_PNG) else arrayOf(
        bucketId,
        MIME_TYPE_IMAGE_JPG,
        MIME_TYPE_IMAGE_JPEG,
        MIME_TYPE_IMAGE_PNG
      )
      val order = "${MediaStore.Images.Media.DATE_MODIFIED} desc"
      cursor =
        AppUtil.getAppContext().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, order)
      if (cursor != null) {
        while (cursor.moveToNext()) {
          val id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID))
          val title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE))
          val displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
          val mimetype = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
          val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
          val width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
          val height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
          val modified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
          val uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
          images.add(ImageMediaEntity(id, title, displayName, mimetype, uri, size, width, height, modified))
        }
      }
    } catch (e: Exception) {
      
      e.printStackTrace()
    } finally {
      cursor?.close()
    }
    return@withContext images
  }

  /**
   * 插入图片
   */
  suspend fun saveImageToMediaStore(path: String) {
    saveImageToMediaStore(File(path))
  }

  /**
   * 插入图片
   */
  suspend fun saveImageToMediaStore(file: File) {
    saveImageToMediaStore(BitmapFactory.decodeFile(file.absolutePath), file.nameWithoutExtension)
  }

  /**
   * 插入图片
   */
  suspend fun saveImageToMediaStore(bitmap: Bitmap, displayName: String) = withContext(Dispatchers.IO) {
    val volumeName = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.VOLUME_EXTERNAL_PRIMARY else MediaStore.VOLUME_EXTERNAL
    val contentUri = MediaStore.Images.Media.getContentUri(volumeName)
    return@withContext AppUtil.getAppContext().contentResolver.insert(contentUri, ContentValues().apply {
      put(MediaStore.Images.Media.TITLE, displayName)
      put(MediaStore.Images.Media.DISPLAY_NAME, "${displayName}.jpg")
      put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_IMAGE_JPEG)
      put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
      put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val relativePath = "${Environment.DIRECTORY_DCIM}/ClassIn"
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
      } else {
        val dataDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "ClassIn")
        if (! dataDir.exists()) {
          dataDir.mkdirs()
        }
        put(MediaStore.MediaColumns.DATA, "${dataDir.absolutePath}/${displayName}.jpg")
      }
    })?.also {uri ->
      try {
        AppUtil.getAppContext().contentResolver.openOutputStream(uri)?.use {
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
          it.flush()
        }
      } catch (e: Exception) {
        
        e.printStackTrace()
        AppUtil.getAppContext().contentResolver.delete(uri, null, null)
      }
    }
  }

  /**
   * 打开图片
   */
  suspend fun openImage(uri: Uri) = withContext(Dispatchers.IO) {
    try {
      return@withContext AppUtil.getAppContext().contentResolver.openInputStream(uri)?.let {
        BitmapFactory.decodeStream(it)
      }
    } catch (e: Exception) {
      
      e.printStackTrace()
    }
    return@withContext null
  }
}