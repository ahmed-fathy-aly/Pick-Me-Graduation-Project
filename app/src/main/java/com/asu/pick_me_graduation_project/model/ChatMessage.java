package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmed on 3/2/2016.
 */
public class ChatMessage
{
    /* fields */
    String id;
    User from;
    User to;
    String content;
    Calendar date;
    String extras;

    /* getters and setters */

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public User getFrom()
    {
        return from;
    }

    public void setFrom(User user)
    {
        this.from = user;
    }

    public User getTo()
    {
        return to;
    }

    public void setTo(User user)
    {
        this.to = user;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Calendar getDate()
    {
        return date;
    }

    public void setDate(Calendar date)
    {
        this.date = date;
    }

    public String getExtras()
    {
        return extras;
    }

    public void setExtras(String extras)
    {
        this.extras = extras;
    }

    /* methods */
    public static ChatMessage fromJson(JSONObject json)
    {
        ChatMessage chatMessage = new ChatMessage();

        try
        {
            chatMessage.setId(json.getString("id"));
            chatMessage.setContent(json.getString("content"));
            chatMessage.setDate(TimeUtils.parseCalendar(json.getString("date")));
            chatMessage.setFrom(User.fromJson(json.getJSONObject("from")));
            chatMessage.setTo(User.fromJson(json.getJSONObject("to")));
            if (!json.isNull("extras"))
                chatMessage.setExtras(json.getString("extras"));

        } catch (JSONException e)
        {
            Log.e("Game", "parse message error " + e.getMessage());
            e.printStackTrace();
        }
        return chatMessage;
    }

    /**
     * parses a latlng from the extras
     *
     * @return null if there's no latlng in the extras
     */
    public LatLng getLatLngExtra()
    {
        try
        {
            JSONObject jsonObject = new JSONObject(extras).getJSONObject("location");
            double latitude = jsonObject.getDouble("latitude");
            double longitude = jsonObject.getDouble("longitude");
            return new LatLng(latitude, longitude);
        } catch (Exception e)
        {
            return null;
        }
    }


    /**
     * puts a json object called location with latitude and longitude in the extras
     *
     * @param latlng
     */
    public static String getExtrasLatlng(LatLng latlng)
    {
        try
        {
            JSONObject locationJson = new JSONObject();
            locationJson.put("latitude", latlng.latitude);
            locationJson.put("longitude", latlng.longitude);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("location", locationJson);
            return jsonObject.toString();
        } catch (Exception e)
        {
            return null;
        }

    }
}
