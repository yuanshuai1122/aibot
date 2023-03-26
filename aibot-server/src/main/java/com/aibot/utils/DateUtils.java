package com.aibot.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author: yuanshuai
 * @create: 2023-03-24 21:50
 */
public class DateUtils {

  /**
   * 获取当前时间15天后的时间
   * @param date 时间
   * @param step 步数
   * @return 时间
   */
  public static Date getAfterDate15(Date date, int step) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, 15 * step);
    return cal.getTime();

  }

  /**
   * 获取当前时间3天后的时间
   * @param date 时间
   * @param step 步数
   * @return 时间
   */
  public static Date getAfterDate3(Date date, int step) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.DAY_OF_MONTH, 3 * step);
    return cal.getTime();

  }

}
