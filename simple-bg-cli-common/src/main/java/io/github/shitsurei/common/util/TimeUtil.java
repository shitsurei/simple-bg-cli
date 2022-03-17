package io.github.shitsurei.common.util;

import io.github.shitsurei.dao.enumerate.business.TimeZoneEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zgr
 * @Description 时间时区工具类
 * @createTime 2022年02月28日 00:25:00
 */
public class TimeUtil {

    private static String[] timeFormatArray = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};

    private static Map<String, SimpleDateFormat> sdfMap = Maps.newHashMap();

    static {
        for (String timeFormat : timeFormatArray) {
            sdfMap.put(timeFormat, new SimpleDateFormat(timeFormat));
        }
    }

    /**
     * 得到指定日期的一天的的最后时刻23:59:59
     *
     * @param date
     * @return
     */
    public static Date getDateEnd(Date date) {
        String temp = sdfMap.get(timeFormatArray[1]).format(date);
        temp += " 23:59:59";
        try {
            return sdfMap.get(timeFormatArray[1]).parse(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到指定日期的一天的开始时刻00:00:00
     *
     * @param date
     * @return
     */
    public static Date getDateStart(Date date) {
        String temp = sdfMap.get(timeFormatArray[1]).format(date);
        temp += " 00:00:00";
        try {
            return sdfMap.get(timeFormatArray[1]).parse(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取服务器默认时区
     *
     * @return
     */
    public static TimeZoneEnum getServerTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        for (TimeZoneEnum value : TimeZoneEnum.values()) {
            if (StringUtils.equals(value.getId(), timeZone.getID())) {
                return value;
            }
        }
        return TimeZoneEnum.CTT;
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    public static String transTime(Date time) {
        return transFormatTime(time, timeFormatArray[0]);
    }

    /**
     * 转换日期
     *
     * @param time
     * @return
     */
    public static String transDate(Date time) {
        return transFormatTime(time, timeFormatArray[1]);
    }

    /**
     * 时间戳转时间
     *
     * @param timeStamp
     * @return
     */
    public static Date transTime(long timeStamp) {
        return new Date(timeStamp);
    }

    /**
     * 转换目标格式时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String transFormatTime(Date time, String format) {
        return transTimeByTimeZone(time, TimeZone.getDefault().getID(), format);
    }

    /**
     * 转换目标时区时间
     *
     * @param time
     * @param targetZoneId
     * @param format
     * @return
     */
    public static String transTimeByTimeZone(Date time, String targetZoneId, String format) {
        SimpleDateFormat sdf = sdfMap.getOrDefault(format, new SimpleDateFormat(format));
        sdfMap.put(format, sdf);
        sdf.setTimeZone(TimeZone.getTimeZone(targetZoneId));
        return sdf.format(time);
    }

    /**
     * 转换目标时区时间（标准时间格式）
     *
     * @param time
     * @param targetZoneId
     * @return
     */
    public static String transTimeByTimeZone(Date time, String targetZoneId) {
        SimpleDateFormat sdf = sdfMap.getOrDefault(timeFormatArray[0], new SimpleDateFormat(timeFormatArray[0]));
        sdfMap.put(timeFormatArray[0], sdf);
        sdf.setTimeZone(TimeZone.getTimeZone(targetZoneId));
        return sdf.format(time);
    }

    /**
     * 解析目标时区时间
     *
     * @param time
     * @param sourceZoneId
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseTimeFromTimeZone(String time, String sourceZoneId, String format) {
        SimpleDateFormat sdf = sdfMap.getOrDefault(format, new SimpleDateFormat(format));
        sdfMap.put(format, sdf);
        sdf.setTimeZone(TimeZone.getTimeZone(sourceZoneId));
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析目标时区时间（标准时间格式）
     *
     * @param time
     * @param source
     * @return
     * @throws ParseException
     */
    public static Date parseTimeFromTimeZone(String time, TimeZoneEnum source) {
        SimpleDateFormat sdf = sdfMap.getOrDefault(timeFormatArray[0], new SimpleDateFormat(timeFormatArray[0]));
        sdfMap.put(timeFormatArray[0], sdf);
        sdf.setTimeZone(TimeZone.getTimeZone(source.getId()));
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 标准格式时间时区转换方法
     *
     * @param time
     * @param source
     * @param target
     * @return
     */
    public static String tranTimeZone(String time, TimeZoneEnum source, TimeZoneEnum target) {
        Date standardTime = parseTimeFromTimeZone(time, source.getId(), timeFormatArray[0]);
        return transTimeByTimeZone(standardTime, target.getId(), timeFormatArray[0]);
    }

    /**
     * 自定义格式时间时区转换方法
     *
     * @param time
     * @param source
     * @param target
     * @return
     */
    public static String tranTimeZone(String time, TimeZoneEnum source, TimeZoneEnum target, String format) {
        Date standardTime = parseTimeFromTimeZone(time, source.getId(), format);
        return transTimeByTimeZone(standardTime, target.getId(), format);
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = sdf.parse("2022-03-13 00:00:00");
        Date endTime = sdf.parse("2022-04-13 00:00:00");
        for (Date date : splitDateRange(startTime, endTime)) {
            System.out.println(sdf.format(date));
        }
    }

    /**
     * 获取时间区间中的每一天初始时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Date> splitDateRange(Date startTime, Date endTime) {
        Calendar startDay = Calendar.getInstance();
        startDay.setTime(startTime);
        startDay.set(Calendar.HOUR, 0);
        startDay.set(Calendar.MINUTE, 0);
        startDay.set(Calendar.SECOND, 0);
        startDay.set(Calendar.MILLISECOND, 0);
        Calendar endDay = Calendar.getInstance();
        endDay.setTime(endTime);
        List<Date> result = Lists.newArrayList();
        while (endTime.getTime() == startDay.getTime().getTime() || endTime.after(startDay.getTime())) {
            if (startTime.getTime() == startDay.getTime().getTime() || startTime.before(startDay.getTime())) {
                result.add(startDay.getTime());
            }
            startDay.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }
}
