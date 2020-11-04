package cn.sh.qianyi.coinspider.util;

import java.text.*;
import java.util.*;

public class TimeStampFormatUtils
{
    public static String timeStamp2Date(final String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(String.valueOf(seconds) + "000")));
    }
    
    public static String date2TimeStamp(final String date_str, final String format) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000L);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    public static String timeStamp() {
        final long time = System.currentTimeMillis();
        final String t = String.valueOf(time / 1000L);
        return t;
    }
    
    public static void main(final String[] args) {
        final String date = timeStamp2Date("1515204470", "yyyy-MM-dd HH:mm:ss");
        System.out.println("date=" + date);
    }
}
