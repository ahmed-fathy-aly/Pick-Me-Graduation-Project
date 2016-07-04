package com.asu.pick_me_graduation_project.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by ahmed on 4/10/2016.
 */
public class TimeUtils
{

    /**
     * parses a string like 2016-04-10T14:32:36.857
     */
    public static Calendar parseCalendar(String dateStr)
    {

        try
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            if (!dateStr.contains("."))
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = dateFormat.parse(dateStr);
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            return c;
        } catch (ParseException e)
        {
            e.printStackTrace();
            return Calendar.getInstance();
        }

    }


    /**
     * converts the calendar to something like "4 days ago"
     */
    public static String getUserFriendlyDate(Context context, Calendar date)
    {
        return DateUtils.getRelativeTimeSpanString(context, date.getTimeInMillis()).toString();
    }


    public static String getUserFriendlyDOB(Calendar dob)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(dob.getTime());
    }

    /**
     * converts to something like 6/20/2016 05:00:00 AM
     */
    public static String convertToBackendTime(Calendar time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(time.getTime()).replace("am", "AM").replace("pm", "PM");
    }

    /**
     * converts to something like 6/20/2016 05:00:00 AM
     */
    public static String convertToBackendTime2(Calendar time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(time.getTime());
    }
    /**
     * converts to a string like 11 05 2017 10 30 00
     */
    public static String convertToDatabaseTime(Calendar time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH mm ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(time.getTime());
    }

    /**
     * converts from a string like 11 05 2017 10 30 00
     */
    public static Calendar getDatabaseTime(String timeStr)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH mm ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        try
        {
            Date date = formatter.parse(timeStr);
            Calendar c = new GregorianCalendar();
            c.setTime(date);
            return c;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new GregorianCalendar();
    }

    /**
     * computes the age of this date of birth
     */
    public static int getAge(Calendar calendar)
    {
        Calendar now = Calendar.getInstance();
        int years = now.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
        calendar.add(Calendar.YEAR, years);
        if (calendar.compareTo(now) < 0)
            years++;
        return years;
    }

}
