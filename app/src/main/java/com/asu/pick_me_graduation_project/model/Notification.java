package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ahmed on 7/2/2016.
 */
public class Notification
{
    /* fields */
    String id;
    String type;
    String title;
    String message;
    String extras;
    Calendar date;
    boolean seen;

    /* setters and getters */

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getExtras()
    {
        return extras;
    }

    public void setExtras(String extras)
    {
        this.extras = extras;
    }

    public Calendar getDate()
    {
        return date;
    }

    public void setDate(Calendar date)
    {
        this.date = date;
    }

    public boolean isSeen()
    {
        return seen;
    }

    public void setSeen(boolean seen)
    {
        this.seen = seen;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    /* methods */
    public static Notification fromJson(JSONObject jsonObject)
    {
        Log.e("Game", jsonObject.toString());

        Notification notification = new Notification();

        try
        {
            notification.setId(jsonObject.getString("id"));
            if (!jsonObject.isNull("title"))
                notification.setTitle(jsonObject.getString("title"));
            if (!jsonObject.isNull("message"))
                notification.setMessage(jsonObject.getString("message"));
            if (!jsonObject.isNull("isSeen"))
                notification.setSeen(jsonObject.getBoolean("isSeen"));
            if (!jsonObject.isNull("type"))
                notification.setType(jsonObject.getString("type"));
            if (!jsonObject.isNull("extras"))
            {
                String extras= jsonObject.getString("extras").replace("\\", "");
                    notification.setExtras(extras);
                Log.e("Game", "extras " + extras );
            }
            Log.e("Game", "extras = " + notification.extras);
            if (!jsonObject.isNull("date"))
            {
                String dateString = jsonObject.getString("date");
                notification.setDate(TimeUtils.parseCalendar(dateString));
            }
        } catch (Exception e)
        {
            Log.e("Game", "error prasing notification " + e.getMessage());
        }

        return notification;
    }

}
