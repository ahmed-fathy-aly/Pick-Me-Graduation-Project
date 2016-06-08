package com.asu.pick_me_graduation_project.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;

/**
 * Created by ahmed on 4/10/2016.
 */
public class TimeUtils {

    /**
     * parses a string like 2016-04-10T14:32:36.857
     */
    public static Calendar parseCalendar(String dateStr) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = dateFormat.parse(dateStr);
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            return  c;
        } catch (ParseException e) {
            e.printStackTrace();
            return Calendar.getInstance();
        }

    }

    /**
     * converts the calendar to something like "4 days ago"
     */
    public static String getUserFriendlyDate(Context context, Calendar date) {
        return DateUtils.getRelativeTimeSpanString(context, date.getTimeInMillis()).toString();
    }

    public static String convertToBackendTime(Calendar time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        return formatter.format(time.getTime()).replace("am", "AM").replace("pm", "PM");
    }
}
