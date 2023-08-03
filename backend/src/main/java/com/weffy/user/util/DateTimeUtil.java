package com.weffy.user.util;

import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String formatDateTime(String localDateTime) {
        return String.format(String.valueOf(formatter));
    }

    public static String getFormattedTime(String formatTime) {
        return DateTimeUtil.formatDateTime(formatTime);
    }
}
