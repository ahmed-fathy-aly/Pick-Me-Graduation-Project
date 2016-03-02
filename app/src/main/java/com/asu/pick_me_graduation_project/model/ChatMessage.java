package com.asu.pick_me_graduation_project.model;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmed on 3/2/2016.
 */
public class ChatMessage
{
    /* fields */
    User user;
    String content;
    Calendar date;

    /* getters and setters */
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
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

    /* methods */
    public static ChatMessage fromJson(JSONObject json)
    {
        ChatMessage chatMessage = new ChatMessage();

        // TODO - parse the json

        return chatMessage;
    }
}
