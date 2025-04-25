package com.treasure.basic.utils

import android.content.ComponentName
import android.content.pm.PackageManager


/**
 * Android Manifest meta-data 标签内容获取工具
 * Create by SuoXuechao on 2021/6/16
 */
object MetaDataUtil {

  /**
   * 获取Application节点下meta-data数据
   */
  @JvmStatic
  fun getAppMetaData(key: String): String? {
    return try {
      val appInfo = AppUtil.getAppContext().packageManager.getApplicationInfo(AppUtil.getAppContext().packageName, PackageManager.GET_META_DATA)
      appInfo.metaData.get(key)?.toString()
    } catch (e: PackageManager.NameNotFoundException) {
      
      e.printStackTrace()
      null
    }
  }

  /**
   * 获取Activity节点下 meta-data 数据
   */
  @JvmStatic
  fun getActivityMetaData(componentName: ComponentName, key: String): String? {
    return try {
      val activityInfo = AppUtil.getAppContext().packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
      activityInfo.metaData.get(key)?.toString()
    } catch (e: PackageManager.NameNotFoundException) {
      
      e.printStackTrace()
      null
    }
  }

  /**
   * 获取Receiver节点下 meta-data 数据
   */
  @JvmStatic
  fun getReceiverMetaData(componentName: ComponentName, key: String): String? {
    return try {
      val activityInfo = AppUtil.getAppContext().packageManager.getReceiverInfo(componentName, PackageManager.GET_META_DATA)
      activityInfo.metaData.get(key)?.toString()
    } catch (e: PackageManager.NameNotFoundException) {
      
      e.printStackTrace()
      null
    }
  }

  /**
   * 获取service节点下 meta-data 数据
   */
  @JvmStatic
  fun getServiceMetaData(componentName: ComponentName,key: String): String?{
    return try {
      val serviceInfo = AppUtil.getAppContext().packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA)
      serviceInfo.metaData.get(key)?.toString()
    } catch (e: PackageManager.NameNotFoundException) {
      
      e.printStackTrace()
      null
    }
  }
}