package com.wm.common.util;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 描述:
 *
 *
 *
 * @auther WangMin
 * @create 2022-06-07 10:00
 */
public class DateKit {

    public enum DateFormat {
        ALL_ZIP_TIME {public String getValue() {
                return "yyyyMMddHHmmss";
            }},
        ALL_TIME {public String getValue() { return "yyyy-MM-dd HH:mm:ss"; }},
        ONLY_MINUTE {public String getValue() {
                return "yyyy-MM-dd HH:mm";
            }},
        ONLY_HOUR {public String getValue() {
                return "yyyy-MM-dd HH";
            }},
        ONLY_DAY {public String getValue() {
                return "yyyy-MM-dd";
            }},
        ONLY_MONTH {public String getValue() {
                return "yyyy-MM";
            }},
        ONLY_MONTH_DAY {public String getValue() {
                return "MM-dd";
            }},
        ONLY_MONTH_SEC {public String getValue() {
                return "MM-dd HH:mm";
            }},
        ONLY_TIME {public String getValue() {
                return "HH:mm:ss";
            }},
        ONLY_HOUR_MINUTE {public String getValue() {
                return "HH:mm";
            }};

        public abstract String getValue();
    }

    /*****************************date localdate long string转换   *****************************************/
    public static String localDateTime2String(LocalDateTime localDateTime, DateFormat dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat.getValue());
        return localDateTime.format(formatter);
    }
    public static LocalDateTime string2LocalDateTime(String timeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timeStr);
        return LocalDateTime.parse(timeStr,dateTimeFormatter);
    }
    public static LocalDateTime long2LocalDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
    public static long localDateTime2long(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }
    public static LocalDateTime date2LocalDateTime(Date date){
        if(null == date) { return null; }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime == null){ return null;}
        ZonedDateTime zonedDateTime = localDateTime.atZone( ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
    public static Date localDate2Date(LocalDate localDate) {
        if (null == localDate) { return null; }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
    public static LocalDate date2LocalDate(Date date){
        if(null == date) { return null; }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 获取UTC格式，世界标准时间。2019-12-18T15:25:30.176Z
     * @param localDateTime
     * @return 2019-12-18T15:25:30.176Z
     */
    public static String getISO8601Time(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        TimeZone utc = TimeZone.getTimeZone("UTC");
        df.setTimeZone(utc);
        return df.format(date);
    }

    /**
     * 获取精确到分的时间戳
     * @param localDateTime
     * @return
     */
    public static long getTimeMills(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), localDateTime.getHour(), localDateTime.getMinute(), 0)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /************************************** 查询  ******************************************/
    //LocalDateTime localDateTime = LocalDateTime.now(); //2022年6月7号 星期二
    //System.out.println("dayofWeek:"+localDateTime.getDayOfWeek());  //TUESDAY
    //System.out.println("dayofWeekValue:"+localDateTime.getDayOfWeek().getValue());  //2
    //System.out.println("dayofMonth:"+localDateTime.getDayOfMonth()); //7
    //System.out.println("dayofyear:"+localDateTime.getDayOfYear()); //158
    //System.out.println("month:"+localDateTime.getMonth()); //JUNE
    //System.out.println("monthValue:"+localDateTime.getMonthValue());  //6

    /**
     * 获取某一天是星期几(返回中文)
     * @param localDate
     * @return  周一
     */
    public static String getWeekDayOfLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        String[] weekDays = { "", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return weekDays[localDate.getDayOfWeek().getValue()];
    }

    /************************************根据当前时间来设置时间  ************************************************/




    /********************************* 根据某一天为坐标来获取其他时间  ***************************************************/

    /**
     * 获取某一天的凌晨
     * @param localDate 2022-06-07
     * @return  2022-06-07 00:00
     */
    public static LocalDateTime getDayStartOfDay(LocalDate localDate){
        return localDate.atStartOfDay();
    }
    /**
     * 获取某一天的23:59:59.999
     * @param localDate 2022-06-07
     * @return  2022-06-07 00:00
     */
    public static LocalDateTime getEndDayOfDay(LocalDate localDate){
        return LocalDateTime.of(localDate,LocalTime.MAX);
    }


    /**
     * 获取当前月的第一天的凌晨
     * @param localDate 2022-06-07
     * @return  2022-06-01 00:00:00 000
     */
    public static LocalDateTime getFirstDayStartOfMonth(LocalDate localDate){
        return localDate.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
    }
    /**
     * 获取当前月的第一天的23:59:59 9999
     * @param localDate 2022-06-07
     * @return  2022-06-01 23:59:59 9999
     */
    public static LocalDateTime getFirstDayEndOfMonth(LocalDate localDate){
        return LocalDateTime.of(localDate.with(TemporalAdjusters.firstDayOfMonth()),LocalTime.MAX);
    }

    /**
     * 获取当前月的最后一天的23:59:59 99999
     * @param localDate 2022-06-07
     * @return  2022-06-30 23:59:59 9999
     */
    public static LocalDateTime getLastDayEndOfMonth(LocalDate localDate){
        return LocalDateTime.of(localDate.with(TemporalAdjusters.lastDayOfMonth()),LocalTime.MAX);
    }


    /**
     * 获取下个月的第一天的凌晨
     * @param localDate 2022-06-07
     * @return 2022-07-01 00:00:00 0000
     */
    public static LocalDateTime getFirstDayStartOfNextMonth(LocalDate localDate){
        return localDate.with(TemporalAdjusters.firstDayOfNextMonth()).atStartOfDay();
    }

    /**
     * 切割日期。按照周期切割成小段日期段(左闭右闭)
     * @param start 开始日期（yyyy-MM-dd）
     * @param end   结束日期（yyyy-MM-dd）
     * @param period    周期（天,周,月,年）
     * @return 切割之后的日期集合
     * <li>startDate="2019-02-28",endDate="2019-03-05",period="day"</li>
     * <li>结果为：[2019-02-28, 2019-03-01, 2019-03-02, 2019-03-03, 2019-03-04, 2019-03-05]</li><br>
     * <li>startDate="2019-02-28",endDate="2019-03-25",period="week"</li>
     * <li>结果为：[2019-02-28,2019-03-06, 2019-03-07,2019-03-13, 2019-03-14,2019-03-20,
     * 2019-03-21,2019-03-25]</li><br>
     * <li>startDate="2019-02-28",endDate="2019-05-25",period="month"</li>
     * <li>结果为：[2019-02-28,2019-02-28, 2019-03-01,2019-03-31, 2019-04-01,2019-04-30,
     * 2019-05-01,2019-05-25]</li><br>
     * <li>startDate="2019-02-28",endDate="2020-05-25",period="year"</li>
     * <li>结果为：[2019-02-28,2019-12-31, 2020-01-01,2020-05-25]</li><br>
     */
    public static List<String> spitDateByPeriod(LocalDate start, LocalDate end, String period) {
        List<String> result = new ArrayList<>();
        LocalDate tmp = start;
        switch (period) {
            case "day":
                while (start.isBefore(end) || start.isEqual(end)) {
                    result.add(start.toString());
                    start = start.plusDays(1);
                }
                break;
            case "week":
                while (tmp.isBefore(end) || tmp.isEqual(end)) {
                    if (tmp.plusDays(6).isAfter(end)) {
                        result.add(tmp.toString() + "," + end);
                    } else {
                        result.add(tmp.toString() + "," + tmp.plusDays(6));
                    }
                    tmp = tmp.plusDays(7);
                }
                break;
            case "month":
                while (tmp.isBefore(end) || tmp.isEqual(end)) {
                    LocalDate lastDayOfMonth = tmp.with(TemporalAdjusters.lastDayOfMonth());
                    if (lastDayOfMonth.isAfter(end)) {
                        result.add(tmp.toString() + "," + end);
                    } else {
                        result.add(tmp.toString() + "," + lastDayOfMonth);
                    }
                    tmp = lastDayOfMonth.plusDays(1);
                }
                break;
            case "year":
                while (tmp.isBefore(end) || tmp.isEqual(end)) {
                    LocalDate lastDayOfYear = tmp.with(TemporalAdjusters.lastDayOfYear());
                    if (lastDayOfYear.isAfter(end)) {
                        result.add(tmp.toString() + "," + end);
                    } else {
                        result.add(tmp.toString() + "," + lastDayOfYear);
                    }
                    tmp = lastDayOfYear.plusDays(1);
                }
                break;
            default:
                break;
        }
        return result;
    }



    /************************ 时间计算******************************************/
    /**
     * 获取相隔几秒
     * @param startTime
     * @param endTime
     * @return
     */
    public long getSecondsBetween(LocalDateTime startTime,LocalDateTime endTime){
        Duration duration = Duration.between(startTime,endTime);
        return duration.getSeconds();
    }

    /**
     * 获取相隔几天
     * @param startDate
     * @param endDate
     * @return
     */
    public long getDaysBetween(LocalDate startDate,LocalDate endDate){
        Period period = startDate.until(endDate);
        return period.getDays();
    }


 /*****************************************************第三方jar包  *******************************************/
 //<dependency>
 //  <groupId>com.github.xkzhangsan</groupId>
 //  <artifactId>xk-time</artifactId>
 //  <version>3.0.1</version>
 //</dependency>
 //节假日计算工具类 HolidayUtil
 //日期计算工具类  DateTimeCalculatorUtil
 //农历日期类 LunarDate
 //计算耗时工具 CostUtil
 //时间自然语言分析工具类（NLP） TimeNLPUtil






 /*********************************************************/

 public static List<TimeSlot> mergeTimeSlots(List<TimeSlot> timeSlots) {
     if (timeSlots.size() == 1) {
         return timeSlots;
     }
     for (int i = 0; i < timeSlots.size(); i++) {
         for (int j = i + 1; j < timeSlots.size(); j++) {
             TimeSlot timeSlot1 = timeSlots.get(i);
             TimeSlot timeSlot2 = timeSlots.get(j);
             List<TimeSlot> mergeSlot = mergeTwo(timeSlot1, timeSlot2);
             // 如果两个时间段能合并则递归继续合并
             if (mergeSlot.size() == 1) {
                 timeSlots.remove(timeSlot1);
                 timeSlots.remove(timeSlot2);
                 timeSlots.addAll(mergeSlot);
                 mergeTimeSlots(timeSlots);
             }
         }
     }
     return timeSlots;
 }

    private static List<TimeSlot> mergeTwo(TimeSlot timeSlot1, TimeSlot timeSlot2) {
        List<TimeSlot> result = new ArrayList<>();
        LocalDateTime start1 = timeSlot1.getStartTime();
        LocalDateTime start2 = timeSlot2.getStartTime();
        LocalDateTime end1 = timeSlot1.getEndTime();
        LocalDateTime end2 = timeSlot2.getEndTime();
        // 如果两个时间段完全没有交集则直接返回
        if (end1.isBefore(start2) || start1.isAfter(end2)) {
            result.add(timeSlot1);
            result.add(timeSlot2);
        }
        // 如果有完全包含则去掉小的那个
        else if (!start1.isAfter(start2) && !end1.isBefore(end2)) {
            result.add(timeSlot1);
        } else if (!start2.isAfter(start1) && !end2.isBefore(end1)) {
            result.add(timeSlot2);
        }
        // 有交集则合并
        else if (start1.isBefore(start2) && end1.isBefore(end2)) {
            timeSlot1.setEndTime(end2);
            result.add(timeSlot1);
        } else if (start2.isBefore(start1) && end2.isBefore(end1)) {
            timeSlot2.setEndTime(end1);
            result.add(timeSlot2);
        }
        return result;
    }

    @Data
    private static class TimeSlot {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }




    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now(); //2022年6月7号 星期二
/*        System.out.println("dayofWeek:"+localDateTime.getDayOfWeek());  //TUESDAY
        System.out.println("dayofWeekValue:"+localDateTime.getDayOfWeek().getValue());  //2
        System.out.println("dayofMonth:"+localDateTime.getDayOfMonth()); //7
        System.out.println("dayofyear:"+localDateTime.getDayOfYear()); //158
        System.out.println("month:"+localDateTime.getMonth()); //JUNE
        System.out.println("monthValue:"+localDateTime.getMonthValue());  //6*/
        System.out.println(localDateTime2long(LocalDateTime.now()));

    }


}
