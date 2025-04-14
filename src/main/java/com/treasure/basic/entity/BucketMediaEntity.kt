/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * 图库
 * Create by SuoXuechao on 2021/5/21
 */
@Parcelize
data class BucketMediaEntity(val bucketId: String, val bucketName: String): Parcelable
