package com.asu.pick_me_graduation_project.model;

import android.text.format.DateUtils;
import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ahmed on 4/10/2016.
 */
public class CommunityPost {

    /* fields */
    String id;
    String communityId;
    String content;
    Calendar date;

    /* getters and setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
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

    public static CommunityPost parseFromJson(JSONObject jsonObject) {
        CommunityPost post = new CommunityPost();

        try {

            post.setId(jsonObject.getString("id"));
            post.setCommunityId(jsonObject.getString("communityId"));
            post.setContent(jsonObject.getString("content"));
            post.setDate(TimeUtils.parseCalendar(jsonObject.getString("date")));

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return post;
    }
}
