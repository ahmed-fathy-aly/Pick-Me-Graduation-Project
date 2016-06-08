package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ahmed on 5/2/2016.
 */
public class Location implements Serializable
{
    /* fields */
    String id;
    double latitude;
    double longitude;
    LocationType type;

     int order;
    User user;

    /* cosntructor */

    public Location()
    {

    }

    /* setters and getters */

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public LocationType getType()
    {
        return type;
    }

    public void setType(LocationType type)
    {
        this.type = type;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public static Location fromJson(JSONObject jsonObject)
    {
        Location location = new Location();

        try
        {
            location.setId(jsonObject.getString("id"));
            location.setLatitude(jsonObject.getDouble("latitude"));
            location.setLongitude(jsonObject.getDouble("longitude"));
            location.setOrder(jsonObject.getInt("order"));
            location.setType(jsonObject.getBoolean("type") ?
                    LocationType.DESTINATION: LocationType.SOURCE);

            // TODO user's stuff
            User user = new User();
            user.setUserId(jsonObject.getString("pinpointedUserId"));
            location.setUser(user);
        } catch (Exception e)
        {
            Log.e("Game", "parse lcoation exception " + e.getMessage());
        }
        return location;
    }

    public enum LocationType
    {
        SOURCE, DESTINATION
    }
}
