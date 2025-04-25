/*
 * Copyright (c) 2021. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.text.TextUtils
import kotlinx.coroutines.*

/**
 *
 * Create by SuoXuechao on 2021/5/21
 */
@SuppressLint("StaticFieldLeak")
object AppUtil {

  private lateinit var _context: Context

  // 打包渠道
  @JvmField
  var packageChannel = ""

  fun bindContext(context: Context) {
    this._context = context
  }

  @JvmStatic
  fun getAppContext(): Context {
    if (! this::_context.isInitialized) {
      throw IllegalStateException("请先初始化Context上下文")
    }
    return this._context
  }

  /**
   * 异步操作 提供给Java调用
   */
  @JvmStatic
  fun async(action: Runnable): Job {
    return GlobalScope.launch(Dispatchers.Default) {
      action.run()
    }
  }

  /**
   * 切换到主线程调用 Java调用
   */
  @JvmStatic
  fun runOnUi(action: Runnable): Job {
    return GlobalScope.launch(Dispatchers.Main) {
      action.run()
    }
  }

  /**
   * 超时执行
   */
  @JvmStatic
  fun withTimeout(timeMillis: Long = 2000, block: () -> Unit): Job {
    return GlobalScope.launch(Dispatchers.Main) {
      delay(timeMillis)
      block()
    }
  }

  @JvmStatic
  fun isDebug(): Boolean {
    return try {
      getAppContext().applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: Exception) {
      
      false
    }
  }

  /**
   * 获取应用名
   */
  @JvmStatic
  fun getAppName(): String {
    try {
      return _context.packageManager.getApplicationLabel(_context.applicationInfo).toString()
    } catch (e: Exception) {
      
    }
    return ""
  }

  /**
   * Version Name
   */
  @JvmStatic
  fun getVersionName(): String {
    val versionName = "1.0.0"
    try {
      val pm = _context.packageManager
      val packageInfo = pm.getPackageInfo(_context.packageName, 0)
      return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
      
      e.printStackTrace()
    }
    return versionName
  }

  @JvmStatic
  fun getAppVersionCode(): Int {
    var result = 0
    try {
      result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        _context.packageManager
          .getPackageInfo(_context.packageName, 0).longVersionCode.toInt()
      } else {
        _context.packageManager.getPackageInfo(_context.packageName, 0).versionCode
      }
    } catch (e: Exception) {
      
      e.printStackTrace()
    }

    return result
  }

  /**
   * 获取设备名
   */
  @JvmStatic
  fun getDeviceName(): String {
    var deviceName = try {
      Settings.Secure.getString(_context.contentResolver, "bluetooth_name")
    } catch (e: Exception) {
      
      Build.MODEL
    }
    if (TextUtils.isEmpty(deviceName)) {
      deviceName = Build.MODEL
    }
    return deviceName
  }

  /**
   * 检测应用是否安装
   */
  @JvmStatic
  fun isAppInstalled(packageName: String): Boolean {
    try {
      val info = getAppContext().packageManager.getPackageInfo(packageName, 0)
      if (info != null) {
        return true
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return false
  }

  /**
   * 发现拒绝服务黑名单应用
   */
  @JvmStatic
  fun foundDenialServiceBlacklistApp(blackList: List<String>): Boolean {
    return blackList.count {packageName -> isAppInstalled(packageName)} > 0
  }

  /**
   * 检查是否安装支付宝
   */
  @JvmStatic
  fun isAliPayInstalled(): Boolean {
    val uri = Uri.parse("alipays://platformapi/startApp")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val componentName = intent.resolveActivity(getAppContext().packageManager)
    return componentName != null
  }

  /**
   * 检查是否安装了微信
   */
  @JvmStatic
  fun isWeixinInstalled(): Boolean = isAppInstalled("com.tencent.mm")


  /**
   * 检查是否安装了微博
   */
  @JvmStatic
  fun isWeiboInstalled(): Boolean = isAppInstalled("com.sina.weibo")

  /**
   * 判断是否为主进程
   */
  @JvmStatic
  fun isMainProcess(app: Application?): Boolean {
    val pid = Process.myPid()
    if (app != null) {
      val am = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
      am?.runningAppProcesses?.forEach {
        if (it.pid == pid) {
          return it.processName.equals(app.packageName, true)
        }
      }
    }
    return false
  }

  @JvmStatic
  fun isPad(context: Context): Boolean {
    return (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        >= Configuration.SCREENLAYOUT_SIZE_LARGE)
  }

  @JvmStatic
  fun platformSource(context: Context): String? {
    return if (isPad(context)) "Android Pad" else "Android Phone"
  }

  @JvmStatic
  fun isSamsungDevice() = Build.MANUFACTURER.contains("samsung")

  /**
   * 判断api 是否大于28 (android 9.0)
   */
  @JvmStatic
  fun isGreaterApi28() = Build.VERSION.SDK_INT > Build.VERSION_CODES.P

  /**
   * 判断api 是否小于29 (android 10.0)
   */
  @JvmStatic
  fun isLessApi29() = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q

}