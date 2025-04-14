package com.treasure.basic.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
    private const val DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val SIMPLE_TIME_FORMAT = "HH:mm"

    /**
     * 获取当前日期（格式：yyyy-MM-dd）
     */
    fun getTodayDate(): String {
        val sdf = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * 获取当前完整时间（格式：yyyy-MM-dd HH:mm:ss）
     */
    fun getNowTime(): String {
        val sdf = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * 获取当前完整时间（格式：HH:mm）
     */
    fun getNowSimpleTime(): String {
        val sdf = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * 时间戳（毫秒）转日期字符串（默认 yyyy-MM-dd）
     */
    fun formatDate(timestamp: Long, pattern: String = DEFAULT_DATE_FORMAT): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * 日期字符串转时间戳（毫秒）
     */
    fun parseDate(dateStr: String, pattern: String = DEFAULT_DATE_FORMAT): Long {
        return try {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.parse(dateStr)?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * 判断是否是今天
     */
    fun isToday(timestamp: Long): Boolean {
        val cal = Calendar.getInstance()
        val todayYear = cal.get(Calendar.YEAR)
        val todayDay = cal.get(Calendar.DAY_OF_YEAR)

        cal.timeInMillis = timestamp
        val compareYear = cal.get(Calendar.YEAR)
        val compareDay = cal.get(Calendar.DAY_OF_YEAR)

        return todayYear == compareYear && todayDay == compareDay
    }

    /**
     * 判断是否是昨天
     */
    fun isYesterday(timestamp: Long): Boolean {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)

        val sdf = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
        val yesterday = sdf.format(cal.time)
        val dateStr = sdf.format(Date(timestamp))

        return yesterday == dateStr
    }

    /**
     * 获取当前年份
     */
    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    /**
     * 获取当前月份（1-12）
     */
    fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    /**
     * 获取当前日（1-31）
     */
    fun getCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }
}
