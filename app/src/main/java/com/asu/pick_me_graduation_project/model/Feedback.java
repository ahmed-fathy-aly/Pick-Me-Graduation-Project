package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by ahmed on 6/14/2016.
 */
public class Feedback
{
    /* fields */
    String userId;
    String rideId;
    String comment;
    int attitude;
    int punctuality;
    DrivingFeedback drivingFeedback;
    User fromUser;

    /* constructor */
    public Feedback()
    {
    }

    /* getters and setters */
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public int getAttitude()
    {
        return attitude;
    }

    public void setAttitude(int attitude)
    {
        this.attitude = attitude;
    }

    public int getPunctuality()
    {
        return punctuality;
    }

    public void setPunctuality(int punctuality)
    {
        this.punctuality = punctuality;
    }

    public DrivingFeedback getDrivingFeedback()
    {
        return drivingFeedback;
    }

    public void setDrivingFeedback(DrivingFeedback drivingFeedback)
    {
        this.drivingFeedback = drivingFeedback;
    }


    public String getRideId()
    {
        return rideId;
    }

    public void setRideId(String rideId)
    {
        this.rideId = rideId;
    }


    public User getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(User fromUser)
    {
        this.fromUser = fromUser;
    }

    /**
     * parses from a json
     */
    public static Feedback fromJson(JSONObject jsonObject)
    {
        Feedback feedback = new Feedback();

        try
        {
            feedback.setRideId(jsonObject.getString("rideId"));
            feedback.setAttitude(jsonObject.getInt("attitude"));
            feedback.setPunctuality(jsonObject.getInt("punctuality"));
            feedback.setComment(jsonObject.getString("comment"));
            feedback.setFromUser(User.fromJson(jsonObject.getJSONObject("fromUser")));
        } catch (JSONException e)
        {
            Log.e("Game", "error parsing feedback " + e.getMessage());
        }

        return feedback;
    }
}
