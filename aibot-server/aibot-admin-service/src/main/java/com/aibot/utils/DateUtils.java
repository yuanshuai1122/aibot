package com.aibot.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 *
 * @author: aabb
 * @create: 2023-03-31 22:18
 */
public class DateUtils {

  // 获得当天0点时间
  public static Date getTimesmorning() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  // 获得当天24点时间
  public static Date getTimesnight() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 24);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  // 获得昨天0点时间
  public static Date getYesterdaymorning() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);
    return cal.getTime();
  }

  // 获得当天近7天时间
  public static Date getWeekFromNow() {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);
    return cal.getTime();
  }

  // 获得本周一0点时间
  public static Date getTimesWeekmorning() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    return cal.getTime();
  }

  // 获得本周日24点时间
  public static Date getTimesWeeknight() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getTimesWeekmorning());
    cal.add(Calendar.DAY_OF_WEEK, 7);
    return cal.getTime();
  }

  // 获得本月第一天0点时间
  public static Date getTimesMonthmorning() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
    return cal.getTime();
  }

  // 获得本月最后一天24点时间
  public static Date getTimesMonthnight() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 24);
    return cal.getTime();
  }

  public static Date getLastMonthStartMorning() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getTimesMonthmorning());
    cal.add(Calendar.MONTH, -1);
    return cal.getTime();
  }

  public static Date getCurrentQuarterStartTime() {
    Calendar c = Calendar.getInstance();
    int currentMonth = c.get(Calendar.MONTH) + 1;
    SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    Date now = null;
    try {
      if (currentMonth >= 1 && currentMonth <= 3)
        c.set(Calendar.MONTH, 0);
      else if (currentMonth >= 4 && currentMonth <= 6)
        c.set(Calendar.MONTH, 3);
      else if (currentMonth >= 7 && currentMonth <= 9)
        c.set(Calendar.MONTH, 4);
      else if (currentMonth >= 10 && currentMonth <= 12)
        c.set(Calendar.MONTH, 9);
      c.set(Calendar.DATE, 1);
      now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return now;
  }

  /**
   * 当前季度的结束时间，即2012-03-31 23:59:59
   *
   * @return
   */
  public static Date getCurrentQuarterEndTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentQuarterStartTime());
    cal.add(Calendar.MONTH, 3);
    return cal.getTime();
  }


  public static Date getCurrentYearStartTime() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
    return cal.getTime();
  }

  public static Date getCurrentYearEndTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentYearStartTime());
    cal.add(Calendar.YEAR, 1);
    return cal.getTime();
  }

  public static Date getLastYearStartTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentYearStartTime());
    cal.add(Calendar.YEAR, -1);
    return cal.getTime();
  }


  // 获取今年是哪一年
  public static Integer getNowYear() {
    Date date = new Date();
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    return Integer.valueOf(gc.get(1));
  }

  public static Date getStartOfYear() {
    LocalDate now = LocalDate.now();
    LocalDate yearStart = LocalDate.of(now.getYear(), Month.JANUARY, 1);
    return Date.from(yearStart.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

}
