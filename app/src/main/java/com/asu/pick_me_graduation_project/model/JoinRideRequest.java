package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 6/9/2016.
 */
public class JoinRideRequest
{
    /* fields */
    User user;
    List<Location> locationList;
    String message;

    /* constructor */
    public JoinRideRequest()
    {
    }

    /* getters and setters */

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public List<Location> getLocationList()
    {
        return locationList;
    }

    public void setLocationList(List<Location> locationList)
    {
        this.locationList = locationList;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }


    /**
     * parses a join ride request from a json
     */
    public static JoinRideRequest fromJson(JSONObject jsonObject)
    {
        JoinRideRequest joinRideRequest = new JoinRideRequest();

        try
        {
            // request details
            joinRideRequest.setMessage(jsonObject.getString("message"));

            // the user
            User user = new User();
            user.setUserId(jsonObject.getString("userId"));
            user.setFirstName(jsonObject.getString("fisrtName"));
            user.setLastName(jsonObject.getString("lastName"));
            user.setProfilePictureUrl(jsonObject.getString("profilePicture"));
            joinRideRequest.setUser(user);

            // locations
            JSONArray locationsJson = jsonObject.getJSONArray("locations");
            List<Location> locations = new ArrayList<>();
            for (int i = 0; i < locationsJson.length(); i++)
                locations.add(Location.fromJson(locationsJson.getJSONObject(i)));
            joinRideRequest.setLocationList(locations);


        } catch (JSONException e)
        {
            Log.e("Game", "Exception parsing " + e.getMessage());
            e.printStackTrace();
        }

        return joinRideRequest;
    }
}
