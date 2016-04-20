package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ahmed on 3/2/2016.
 */
public class ChatMessage {
    /* fields */
    String id;
    User from;
    User to;
    String content;
    Calendar date;

    /* getters and setters */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User user) {
        this.from = user;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User user) {
        this.to = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }


    /* methods */
    public static ChatMessage fromJson(JSONObject json) {
        ChatMessage chatMessage = new ChatMessage();

        // TODO - parse the json
        try {
            chatMessage.setId(json.getString("id"));
            chatMessage.setContent(json.getString("content"));
            chatMessage.setDate(TimeUtils.parseCalendar(json.getString("date")));
            chatMessage.setFrom(User.fromJson(json.getJSONObject("from")));
            chatMessage.setTo(User.fromJson(json.getJSONObject("to")));

        } catch (JSONException e) {
            Log.e("Game", "parse message error " + e.getMessage());
            e.printStackTrace();
        }
        return chatMessage;
    }
}
