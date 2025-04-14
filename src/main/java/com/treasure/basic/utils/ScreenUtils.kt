package com.treasure.basic.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.WindowCompat


object ScreenUtils {

    fun getStatusBarHeight(activity:Activity): Int {
        var statusBarHeight = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val windowInsets: WindowInsets = activity.window.decorView.rootWindowInsets
            val cutout = windowInsets.displayCutout
            if (cutout != null) {
                statusBarHeight = cutout.safeInsetTop
            }
        } else {
            val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = activity.resources.getDimensionPixelSize(resourceId)
            }
        }
        return statusBarHeight
    }

    fun immersiveStatusBarDark(activity: Activity) {
        immersiveStatusBar(activity,true)
    }

    fun immersiveStatusBar(activity: Activity) {
        immersiveStatusBar(activity,false)
    }

    fun immersiveStatusBar(activity: Activity, mask:Boolean) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        immersiveStatusBarBase(activity, mask)
    }

    fun immersiveStatusBarBase(activity: Activity, mask: Boolean) {
        activity.window.statusBarColor = Color.TRANSPARENT
        val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        //true 状态栏字体颜色为黑色
        insetsController.isAppearanceLightStatusBars = mask
        insetsController.isAppearanceLightNavigationBars = mask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) activity.window.isNavigationBarContrastEnforced = false
        activity.window.navigationBarColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Color.TRANSPARENT else Color.BLACK
    }

    /**
     * 获取屏幕宽度（px）
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获取屏幕高度（px）
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * dp 转 px
     */
    fun dp2px(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * px 转 dp
     */
    fun px2dp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    /**
     * sp 转 px
     */
    fun sp2px(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * px 转 sp
     */
    fun px2sp(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.scaledDensity
    }
}