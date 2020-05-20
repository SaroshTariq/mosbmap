package com.mosbmap.usersservice.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Sarosh
 */
public class DateTimeUtil {

    /***
     * Generate current timestamp with fromat 'yyyy-MM-dd HH:mm:ss'
     * @return 
     */
    public static String currentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date currentDate = new Date();
        String currentTimeStamp = sdf.format(currentDate);
        return currentTimeStamp;
    }


    /***
     * Generate expiry time by adding seconds, hours or minutes with format 'yyyy-MM-dd HH:mm:ss'
     * @param seconds
     * @param minutes
     * @param hours
     * @return 
     */
    public static String expiryTimestamp(int seconds, int minutes, int hours) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, hours);
        c.add(Calendar.SECOND, seconds);
        c.add(Calendar.MINUTE, minutes);
        Date expiryDate = c.getTime();
        return dateFormat.format(expiryDate);
    }
}
