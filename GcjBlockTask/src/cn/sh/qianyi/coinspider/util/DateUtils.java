package cn.sh.qianyi.coinspider.util;

import java.util.*;

import org.apache.commons.lang3.*;

import java.text.*;

public class DateUtils {
    public static Date getNextDay() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(5, -2);
        return calendar.getTime();
    }

    public static boolean checkEndDate(final String dateString) {
        return checkEndDate(dateString, null);
    }

    public static boolean checkEndDate(final String dateString, final String dateStyle) {
        Date checkDate = null;
        if (dateStyle != null && StringUtils.isNotEmpty(dateStyle)) {
            checkDate = parseDate(dateString, dateStyle);
        }
        if (checkDate == null) {
            checkDate = parseDateYYYYMMDDHyphen(dateString);
        }
        if (checkDate == null) {
            checkDate = parseDateYYYYMMDDSlash(dateString);
        }
        final Date endDate = getNextDay();
        return endDate == null || (checkDate != null && checkDate.getTime() >= endDate.getTime());
    }

    public static String formateDate(final String strDate) {
        if (strDate == null || "".equals(strDate.trim())) {
            return "";
        }
        final SimpleDateFormat strsdf = new SimpleDateFormat("yyyy-MM-dd");
        String str = "";
        try {
            final Date date = strsdf.parse(strDate);
            final SimpleDateFormat datesdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = datesdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Date parseDateYYYYMMDDHyphen(final String yyyymmdd) {
        return parseDate(yyyymmdd, "yyyy-MM-dd");
    }

    public static Date parseDateYYYYMMDDSlash(final String yyyymmdd) {
        return parseDate(yyyymmdd, "yyyy/MM/dd");
    }

    public static Date parseDate(final String dateValueString, final String sdf) {
        final SimpleDateFormat strsdf = new SimpleDateFormat(sdf);
        Date date = null;
        try {
            date = strsdf.parse(dateValueString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String toDateString(final Date dateValue, final String sdf) {
        final SimpleDateFormat strsdf = new SimpleDateFormat(sdf);
        return strsdf.format(dateValue);
    }

    public static String getCurrentTime() {
        return new StringBuilder(String.valueOf(new Date().getTime())).toString();
    }

    public static String dateToStamp(final String s) throws ParseException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = simpleDateFormat.parse(s);
        final long ts = date.getTime();
        final String res = String.valueOf(ts);
        return res;
    }

    public static String stampToDate(final String s) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final long lt = new Long(s);
        final Date date = new Date(lt);
        final String res = simpleDateFormat.format(date);
        return res;
    }

    public static String getTimeByHour(final int hour) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(11, calendar.get(11) + hour);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    public static String getTimeByMinute(final int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(12, minute);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    public static String getTimeByDay(final int day) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(5, day);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    public static String timestamp2Date(final String str_num, final String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (str_num.length() == 13) {
            final String date = sdf.format(new Date(Long.parseLong(str_num)));
            return date;
        }
        final String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
        return date;
    }

    public static void main(final String[] args) {
        System.out.println(DateUtils.stampToDate(DateUtils.getCurrentTime()));
    }
}
