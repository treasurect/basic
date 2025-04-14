package com.treasure.basic.utils

import android.os.SystemClock

/**
 * create by zhangsj
 * 防止短时间内频繁点击。默认500ms，可自定义延迟时间mDelayTime
 * 此类不止可以针对点击的按钮，也可以直接作用的普通方法中，同样有防止连接触发的效果
 */
class AntiShake {

  companion object {

    var mOneClickList = mutableListOf<OneClick>()

    /**
     * 检查是否在短时间内连续点击
     * @param any
     * @param mDelayTime
     * @return true 是
     */
    fun check(any:Any?, mDelayTime: Long = 0L):Boolean{
      var flag = ""
      if (any == null){
        flag = Thread.currentThread().stackTrace[2].methodName
      }else{
        flag = any.toString()
      }
      mOneClickList.forEach(){
        if (it.methodName == flag) return it.check()
      }

      var oneClick = OneClick(methodName = flag)
      if (mDelayTime>0){
        oneClick.clickDelayTime = mDelayTime
      }
      mOneClickList.add(oneClick)
      return oneClick.check()
    }

    /**
     * 获取默认的flag标志，class类名+指定的方法名称
     */
    fun <T> getDefaultFlag(clazz: Class<T>,methodName: String):String{
       return clazz.name.toString()+"."+methodName
    }
  }


  class OneClick(val methodName: String = "", var clickDelayTime: Long = 500L, var lastTime: Long = 0) {

    /**
     * 当前时间与上一次时间间隔大于延迟返回false
     */
    fun check(): Boolean {
      val currentTime = SystemClock.elapsedRealtime();
      if (currentTime - lastTime > clickDelayTime) {
        lastTime = currentTime
        return false
      }
      return true
    }
  }
}