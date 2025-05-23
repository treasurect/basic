/*
 * Copyright (c) 2019. Beijing EEO Education Ltd. All Rights Reserved.
 */

package com.treasure.basic.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by codbking on 2016/6/1.
 */
public class CalendarUtil {

  //获取一月的第一天是星期几
  public static int getDayOfWeek(int y, int m, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(y, m - 1, day);
    return calendar.get(Calendar.DAY_OF_WEEK);
  }

  //获取一月最大天数
  public static int getDayOfMaonth(int y, int m) {
    Calendar cal = Calendar.getInstance();
    cal.set(y, m - 1, 1);
    int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
    return dateOfMonth;
  }

  public static int getMothOfMonth(int y, int m) {
    Calendar cal = Calendar.getInstance();
    cal.set(y, m - 1, 1);
    int dateOfMonth = cal.get(Calendar.MONTH);
    return dateOfMonth + 1;
  }

  public static int[] getYMD(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return new int[] {
        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)
    };
  }

  public static int[] getYMDHMS(long dateTime) {// 单位是毫秒
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(dateTime);
    return new int[] {
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE),
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
        calendar.get(Calendar.SECOND)
    };
  }

  public static String[] getDaysOfMonth(int y, int m) {
    int maxDayCount = getDayOfMaonth(y, m);
    String[] days = new String[maxDayCount];
    for (int i = 1; i <= maxDayCount; i++) {
      days[i - 1] = i + "日";
    }
    return days;
  }

  public static String getDefaultDateString() {
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat();
    mSimpleDateFormat.applyPattern("yyyy-MM-dd");
    return mSimpleDateFormat.format(new Date());
  }
}
