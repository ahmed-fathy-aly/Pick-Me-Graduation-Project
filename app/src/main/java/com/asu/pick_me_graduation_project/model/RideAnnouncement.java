package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ahmed on 7/2/2016.
 */
public class RideAnnouncement
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
    public static RideAnnouncement fromJson(JSONObject jsonObject)
    {
        RideAnnouncement rideAnnouncement = new RideAnnouncement();

        try
        {
            rideAnnouncement.setId(jsonObject.getString("id"));
            rideAnnouncement.setRideId(jsonObject.getString("rideId"));
            rideAnnouncement.setContent(jsonObject.getString("content"));
            rideAnnouncement.setUser(User.fromJson(jsonObject.getJSONObject("user")));
            String dateStr = jsonObject.getString("date");
            rideAnnouncement.setDate(TimeUtils.parseCalendar(dateStr));
        } catch (Exception e)
        {
            Log.e("Game", "parsing announcment exception " + e.getMessage());
        }

        return rideAnnouncement;
    }
}
