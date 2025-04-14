package com.treasure.basic.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.treasure.basic.R
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * * Time : 2020/8/17
 * * Author : 霍昌峰
 * * Description : 对文字设置高亮,主要是在搜索的时候使用
 */
object TextHighLightUtil {


  fun setTextHighLight(content: String, targetText: String): SpannableStringBuilder {
    return setTextHighLight(content, targetText, "")
  }

  /**
   *
   * 含有脱敏内容的调用此方法,会对脱敏的*号做单独处理
   * @param content 文本内容,也是实际需要显示的内容
   * @param targetText 需要高亮的内容
   * @param originalText 被脱敏之前的原始内容,例如邮箱或者手机号
   * */
  fun setTextHighLight(content: String, targetText: String, originalText: String?): SpannableStringBuilder {
    val spannable = SpannableStringBuilder(content)
    val span = ForegroundColorSpan(AppUtil.getAppContext().resources.getColor(R.color.white))
    if (originalText.isNullOrEmpty()) {
      if (content == targetText) { //如果完全匹配直接返回结果
        spannable.setSpan(span, 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
      }
    } else {
      if (targetText == originalText) {
        spannable.setSpan(span, 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
      }
    }

    try {
      if (originalText.isNullOrEmpty()) {
        val pattern: Pattern = Pattern.compile(targetText, Pattern.CASE_INSENSITIVE) //关键字正则
        val m = pattern.matcher(content) //匹配关键字
        if (m.find()) {
          val end = m.start() + targetText.length
          if (end > content.length) return spannable
          spannable.setSpan(span, m.start(), m.start() + targetText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
      } else {
        val pattern: Pattern = Pattern.compile(targetText, Pattern.CASE_INSENSITIVE) //关键字正则
        val m = pattern.matcher(originalText) //匹配关键字
        if (m.find()) {
          var start = m.start()
          var end = start + targetText.length

          val pAsterisk = Pattern.compile("\\*{3,}")//匹配字符串中*的位置
          val mAsterisk = pAsterisk.matcher(content)
          while (mAsterisk.find()) {
            val sAsterisk = mAsterisk.start()
            val eAsterisk = mAsterisk.end()
            when {
              start == sAsterisk && end == eAsterisk -> {//完全匹配所有星号的,不需要处理
              }
              else -> {//如果内容在多个*之间,则不将*改颜色,全部匹配后,将所有*都改颜色
                if (end in sAsterisk until eAsterisk) { //结束在星号之间,去星号的开头位置
                  end = sAsterisk
                }
                if (start in sAsterisk + 1 until eAsterisk) { //121430853*****oa.com 如果匹配start匹配的正好是sAsterisk,不需要重新赋值
                  start = eAsterisk
                }
              }
            }
          }

          if (end > content.length) return spannable
          if (end <= start) return spannable

          spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
      }

    } catch (e: PatternSyntaxException) { //颜文字等内容会出现此异常
      //      EOLogger.e("搜索匹配出错-> content-${content} targetText-$targetText")
      if (content.contains(targetText)) {
        val start = content.indexOf(targetText, ignoreCase = true)
        val end = start + targetText.length
        if (end > content.length) return spannable
        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
      }
      return spannable
    }
    return spannable
  }
}