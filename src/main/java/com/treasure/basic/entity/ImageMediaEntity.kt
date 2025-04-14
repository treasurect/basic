/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 图片信息
 * Create by SuoXuechao on 2021/5/21
 */
@Parcelize
data class ImageMediaEntity(
  val id: String,
  val title: String,
  val displayName: String,
  val mimeType: String,
  val uri: Uri,
  val size: Long,
  val width: Int,
  val height: Int,
  val modified: Long
): Parcelable