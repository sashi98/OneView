package oneview.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    //public final static SimpleDateFormat SDF_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat(DP_YYYY_MM_DD_HH_MM_SS);


    public static Date getCurrentDateObj(String format) {
        return getDate(Calendar.getInstance(), format);
    }

    public static String getCurrentDateAsStr(String format) {
        return DateFormatUtils.format(Calendar.getInstance().getTime(), format);
    }

    private static Date getDate(Calendar calendar, String format) {
        String str = DateFormatUtils.format(calendar.getTime(), format);
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(Calendar cal, String format) {
        String dateStr = DateFormatUtils.format(cal.getTime(), format);
        System.out.println(dateStr);
        return dateStr;
    }

    public static String getTimeElapsed(Date now, Date thenDate) {
        Instant nowDateTime = Instant.ofEpochMilli(now.getTime());
        Instant then = Instant.ofEpochMilli(thenDate.getTime());
        Duration timeElapsed = Duration.between(then, nowDateTime);
        long seconds = timeElapsed.getSeconds();
        long hrs = timeElapsed.toHours();
        long min = timeElapsed.toMinutes();
        long days = timeElapsed.toDays();
        long weeks = timeElapsed.toDays() / 7;
        long months = timeElapsed.toDays() / 30;
        long years = timeElapsed.toDays() / 365;
        if (years != 0) {
            return years + " year(s) ago";
        }
        if (months != 0) {
            return months + " month(s) ago";
        }
        if (weeks != 0) {
            return weeks + " week(s) ago";
        }
        if (days != 0) {
            return days + " day(s) ago";
        }
        if (hrs != 0) {
            return hrs + " hour(s) ago";
        }
        if (min != 0) {
            return min + " minute(s) ago";
        }
        if (seconds >= 10 && seconds < 60) {
            return "few seconds ago";
        }
        if (seconds < 10) {
            return "a moment ago";
        }
        return "";
    }


}
