package com.treasure.basic.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastUtils {

    private var toast: Toast? = null
    private val handler = Handler(Looper.getMainLooper())

    /**
     * 显示短时长 Toast
     */
    fun show(context: Context, message: String?) {
        showToast(context, message, Toast.LENGTH_SHORT)
    }

    /**
     * 显示长时长 Toast
     */
    fun showLong(context: Context, message: String?) {
        showToast(context, message, Toast.LENGTH_LONG)
    }

    /**
     * 核心方法：在主线程显示 Toast，防止重复弹出
     */
    private fun showToast(context: Context, message: String?, duration: Int) {
        if (message.isNullOrEmpty()) return

        handler.post {
            toast?.cancel()
            toast = Toast.makeText(context.applicationContext, message, duration)
            toast?.show()
        }
    }

    /**
     * 取消当前 Toast
     */
    fun cancel() {
        handler.post {
            toast?.cancel()
            toast = null
        }
    }
}
