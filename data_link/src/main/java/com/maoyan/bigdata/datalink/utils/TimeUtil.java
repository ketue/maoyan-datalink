package com.maoyan.bigdata.datalink.utils;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期处理类
 *
 * @author lipeng10
 * @version 1.0 2016-10-12
 */
public class TimeUtil {
    private static final Logger logger = Logger.getLogger(TimeUtil.class);
    //日期格式化
    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    //fast date format
    private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    private static final FastDateFormat fdfYMD = FastDateFormat.getInstance("yyyyMMdd");
    //日期时间格式化
    private static final SimpleDateFormat DTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //周列表
    private static final String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static final String[] hourStrArr = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};

    /**
     * 两个日期之间的绝对值,
     *
     * @param start yyyy-MM-dd
     * @param end   yyyy-MM-dd
     * @return 当返回-1 则表示发生异常
     */
    public static int betweenTwoDayAbs(String start, String end) {
        try {
            return (int) (Math.abs(DF.parse(start).getTime() - DF.parse(end).getTime()) / 86400000L);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取给定日期所在月的最后一天
     *
     * @param date
     * @return
     */
    public static String getLastDayOfMonth(String date) {
        String[] t = date.split("-");
        int year = StringUtils.parseInt(t[0]);
        //需要注意的是：月份是从0开始的，比如说如果输入5的话，实际上显示的是4月份的最后一天，千万不要搞错了哦
        int month = StringUtils.parseInt(t[1]) - 1;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return DF.format(cal.getTime());
    }

    /**
     * 获取给定日期坐在月的第一天
     *
     * @param date
     * @return
     */
    public static String getFirstDayOfMonth(String date) {
        String[] t = date.split("-");
        int year = StringUtils.parseInt(t[0]);
        //需要注意的是：月份是从0开始的，比如说如果输入5的话，实际上显示的是4月份的最后一天，千万不要搞错了哦
        int month = StringUtils.parseInt(t[1]) - 1;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return DF.format(cal.getTime());
    }

    /**
     * 获取指定日期和差值所得的时间
     *
     * @param dateTime
     * @param days
     * @param format
     * @return
     */
    public static String getDateTimeByCompute(String dateTime, int days, String format) {
        String[] t = dateTime.split("-");
        if (t.length < 3) {
            return null;
        }
        int year = StringUtils.parseInt(t[0]);
        //需要注意的是：月份是从0开始的，比如说如果输入5的话，实际上显示的是4月份的最后一天，千万不要搞错了哦
        int month = StringUtils.parseInt(t[1]) - 1;
        int day = StringUtils.parseInt(t[2].substring(0, 2));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.add(Calendar.DAY_OF_MONTH, days);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(cal.getTime());
    }

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getCurDatetime() {
        return DTF.format(new Date());
    }

    /**
     * 获取符合指定格式的当前时间
     *
     * @param format
     * @return
     */
    public static String getCurDatetime(String format) {
        SimpleDateFormat dt = new SimpleDateFormat(format);
        return dt.format(new Date());
    }

    /**
     * 将timestamp类型的时间转换为string类型
     *
     * @param timestamp
     * @return
     */
    public static String timestampToString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return DTF.format(timestamp);
    }

    /**
     * 将给定日期转换为想要的日期类型
     *
     * @param srcDateTime
     * @param srcFormat
     * @param destFormat
     * @return
     */
    public static String srcToDestDateTime(String srcDateTime, String srcFormat, String destFormat) {
        try {
            SimpleDateFormat srcDf = new SimpleDateFormat(srcFormat);
            Date date = srcDf.parse(srcDateTime);
            SimpleDateFormat destDf = new SimpleDateFormat(destFormat);
            return destDf.format(date);
        } catch (ParseException e) {
            logger.error("日期【" + srcDateTime + "】从【" + srcFormat + "】类型转换为【" + destFormat + "】失败");
            return srcDateTime;
        }
    }

    /**
     * 获取指定日期对应星期几
     *
     * @param dateTime -- 指定日期
     * @param format   -- 日期格式化方式
     * @return -- 对应星期几
     */
    public static String getDayOfWeek(String dateTime, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date date = df.parse(dateTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int weekIndex = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekIndex < 0) {
                weekIndex = 0;
            }
            return weeks[weekIndex];
        } catch (Exception e) {
            logger.error("格式为【" + format + "】的日期【" + dateTime + "】获取对应星期*失败");
            return null;
        }
    }

    /**
     * 计算两个日期间的差值
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getDayDiff(String startDate, String endDate) {
        try {
            //logger.info("dayDiff startTime and endTime:"+ startDate +" & "+ endDate);
            Date start = DF.parse(startDate);
            Date end = DF.parse(endDate);
            int result = (int) ((end.getTime() - start.getTime()) / (1000L * 60 * 60 * 24));
            return result;
        } catch (ParseException e) {
            logger.info("计算【" + startDate + "】到【" + endDate + "】的日期差值异常", e);
            return 0;
        }
    }

    /**
     * 日期类型从format格式，转换成long型
     *
     * @param dateInput 输入日期
     * @param format    对应日期的格式
     * @return
     */
    public static long timediff(String dateInput, String format) {
        long outcome = -100;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(format);
            Date date = inputFormat.parse(dateInput);
            outcome = date.getTime();
        } catch (ParseException e) {
            logger.error("获取指定时间对应的Long类型失败", e);
        }
        return outcome;
    }

    /**
     * 将长整形日期转换为对应的字符串日期
     *
     * @param format   -- 输出格式
     * @param longTime -- long日期
     * @return
     */
    public static String getDateTimeFromLong(String format, Long longTime) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date dt = new Date(longTime);
        return df.format(dt);
    }


    /**
     * @param start
     * @param end
     * @return
     */
    public static List<String> getDateBetweenTwoDay(String start, String end) {
        List<String> daysList = new ArrayList<>();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            long startT = df.parse(start).getTime();
            long endT = df.parse(end).getTime();
            while (endT >= startT) {
                daysList.add(df.format(new Date(startT)));
                startT += 86400000L;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return daysList;
    }

    /**
     * @param source 起始日期
     * @param add    需要增加的天数,负数为减
     * @param format 输入输出的日期格式
     * @return
     */
    public static String addDay(String source, int add, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            long desTime = df.parse(source).getTime() + (add * 86400000L);
            return df.format(new Date(desTime));
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    /**
     * 当前日期相差N天的日期
     *
     * @param add    需要增加的天数,负数为减
     * @param format 输入输出的日期格式
     * @return
     */
    public static String currentAddDay(int add, String format) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(format);
        return fastDateFormat.format(new Date(System.currentTimeMillis() + add * 86400000L));
    }

    /**
     * 和今天差intervalDay的yyyyMMMdd类型日期
     *
     * @param intervalDay
     * @return
     */
    public static int getIntDate(int intervalDay) {
        return Integer.valueOf(fdfYMD.format(new Date(System.currentTimeMillis() + intervalDay * 86400000L)));
    }

    public static List<String> getDayListBySubDays(Date beginDate,int subDays,String format){
        List<String> dayList = new ArrayList<>();
        FastDateFormat fastDateFormat = FastDateFormat.getInstance(format);
        int tag = subDays>0?1:-1;
        int absSub = Math.abs(subDays);
        for (int i = 0; i < absSub; i++) {
            System.out.println(beginDate.getTime()+tag*i*86400000L);
            dayList.add(fastDateFormat.format(beginDate.getTime()+tag*i*86400000L));
        }
        return dayList;
    }

    public static String format(Date date,String pattern){
        FastDateFormat fdf=FastDateFormat.getInstance(pattern);
        return fdf.format(date);
    }

    /**
     * 减去相应单位日期
     * @param date 时间
     * @param num 减去数量
     * @param unit 单位：例：Calendar.MONTH
     * @return
     */
    public static Date subtract(Date date,int num,int unit) {
        Calendar curr = Calendar.getInstance();
        curr.setTime(date);
        curr.set(unit, curr.get(unit) - num);
        return curr.getTime();
    }

    /**
     * 获取时间单位的开始；例：date为20191028 unit为Calendar.DATE 返回20191001
     * @param date 日期
     * @param unit 时间单位
     * @return
     */
    public static Date getFirstUnit(Date date, int unit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(unit, cal.getActualMinimum(unit));
        return cal.getTime();
    }

    /**
     * 获取时间单位的结束；例：date为20191028 unit为Calendar.DATE 返回20191001
     * @param date 日期
     * @param unit 时间单位
     * @return
     */
    public static Date getLastUnit(Date date, int unit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(unit, cal.getActualMaximum(unit));
        return cal.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        return getFirstUnit(date,Calendar.DAY_OF_MONTH);
    }
    public static Date getLastDayOfMonth(Date date) {
        return getLastUnit(date,Calendar.DAY_OF_MONTH);
    }

    public static void main(String[] args) {
//        List<String> dayList = TimeUtil.getDateBetweenTwoDay("20181102", "20181102");
//        System.out.println(dayList);
//        System.out.println(TimeUtil.subtract(new Date(),600, Calendar.SECOND).toLocaleString());
//        System.out.println(TimeUtil.getLastDayOfMonth(TimeUtil.subtract(new Date(),30, Calendar.DAY_OF_MONTH)).toLocaleString());
//        System.out.println(TimeUtil.getFirstDayOfMonth(new Date()).toLocaleString());
//        System.out.println(TimeUtil.getFirstUnit(new Date(),Calendar.DAY_OF_WEEK).toLocaleString());
        Calendar c=Calendar.getInstance();
//        c.setTime(TimeUtil.subtract(new Date(),30, Calendar.DAY_OF_MONTH));
        c.setTime(new Date());
        System.out.println("before time:"+c.getTime().toLocaleString());
        System.out.println("max day:"+c.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println("max day:"+c.getActualMinimum(Calendar.DAY_OF_MONTH));
    }




}
