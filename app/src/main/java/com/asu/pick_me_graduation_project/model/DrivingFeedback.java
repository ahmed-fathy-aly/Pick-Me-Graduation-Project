package com.asu.pick_me_graduation_project.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmed on 7/1/2016.
 */
public class DrivingFeedback
{
    /* fields */
    String userId;
    String rideId;
    int driving;
    boolean sameCar;
    boolean sameModel;
    boolean samePlate;
    boolean sameAc;
    User fromUser;

    /* setters and getters */
    public int getDriving()
    {
        return driving;
    }

    public void setDriving(int driving)
    {
        this.driving = driving;
    }

    public boolean isSameCar()
    {
        return sameCar;
    }

    public void setSameCar(boolean sameCar)
    {
        this.sameCar = sameCar;
    }

    public boolean isSameModel()
    {
        return sameModel;
    }

    public void setSameModel(boolean sameModel)
    {
        this.sameModel = sameModel;
    }

    public boolean isSamePlate()
    {
        return samePlate;
    }

    public void setSamePlate(boolean samePlate)
    {
        this.samePlate = samePlate;
    }

    public boolean isSameAc()
    {
        return sameAc;
    }

    public void setSameAc(boolean sameAc)
    {
        this.sameAc = sameAc;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public User getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(User fromUser)
    {
        this.fromUser = fromUser;
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

    /**
     * parses from a json
     */
    public static DrivingFeedback fromJson(JSONObject jsonObject)
    {
        DrivingFeedback drivingFeedback = new DrivingFeedback();

        try
        {
            drivingFeedback.setRideId(jsonObject.getString("rideId"));
            drivingFeedback.setDriving(jsonObject.getInt("driving"));
            drivingFeedback.setFromUser(User.fromJson(jsonObject.getJSONObject("fromUser")));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return drivingFeedback;
    }
}
