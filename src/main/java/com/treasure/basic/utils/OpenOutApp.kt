package com.treasure.basic.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object OpenOutApp {

  fun openBrowser(context: Context, url: String?) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.data = Uri.parse(url)
    // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
    // 官方解释 : Name of the component implementing an activity that can display the intent
    if (intent.resolveActivity(context.packageManager) != null) {
      // ComponentName componentName = intent.resolveActivity(context.getPackageManager());
      context.startActivity(Intent.createChooser(intent, "请选择浏览器"))
    } else {
      Toast.makeText(AppUtil.getAppContext(), "链接错误或无浏览器", Toast.LENGTH_SHORT).show()
    }
  }
}