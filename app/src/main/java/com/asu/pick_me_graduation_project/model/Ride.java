package com.asu.pick_me_graduation_project.model;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmed on 5/2/2016.
 */
public class Ride
{
    /* fields */
    String id;
    String description;
    User rider;
    Calendar time;
    List<Location> locations;

    /* constructor */
    public Ride()
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public User getRider()
    {
        return rider;
    }

    public void setRider(User rider)
    {
        this.rider = rider;
    }

    public Calendar getTime()
    {
        return time;
    }

    public void setTime(Calendar time)
    {
        this.time = time;
    }

    public List<Location> getLocations()
    {
        return locations;
    }

    public void setLocations(List<Location> locations)
    {
        this.locations = locations;
    }
}

