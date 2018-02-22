package xyz.yooniks.cproxy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilities {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
    private static SimpleDateFormat parseDateFormat = new SimpleDateFormat("dd-MM-yyyy:HH:mm:ss");

    public static String getDate(final long time) {
        return dateFormat.format(new Date(time));
    }

    public static long getDateFromString(final String string) {
        try {
            return parseDateFormat.parse(string).getTime();
        } catch (Throwable e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
