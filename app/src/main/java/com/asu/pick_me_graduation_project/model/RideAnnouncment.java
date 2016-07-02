package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ahmed on 7/2/2016.
 */
public class RideAnnouncment
{
    /* fields */
    String id;
    String rideId;
    String content;
    User user;
    Calendar date;

    /* getters and setters */
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Calendar  getDate()
    {
        return date;
    }

    public void setDate(Calendar date)
    {
        this.date = date;
    }

    public String getRideId()
    {
        return rideId;
    }

    public void setRideId(String rideId)
    {
        this.rideId = rideId;
    }

    /* methods */
    public static RideAnnouncment fromJson(JSONObject jsonObject)
    {
        RideAnnouncment rideAnnouncment = new RideAnnouncment();

        try
        {
            rideAnnouncment.setId(jsonObject.getString("id"));
            rideAnnouncment.setRideId(jsonObject.getString("rideId"));
            rideAnnouncment.setContent(jsonObject.getString("content"));
            String dateStr = jsonObject.getString("date");
            rideAnnouncment.setDate(TimeUtils.parseCalendar(dateStr));
        } catch (Exception e)
        {
            Log.e("Game", "parsing announcment exception " + e.getMessage());
        }

        return rideAnnouncment;
    }
}
