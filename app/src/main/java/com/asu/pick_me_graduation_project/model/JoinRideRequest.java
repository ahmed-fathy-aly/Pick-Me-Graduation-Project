package com.asu.pick_me_graduation_project.model;

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
    {}

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
}
