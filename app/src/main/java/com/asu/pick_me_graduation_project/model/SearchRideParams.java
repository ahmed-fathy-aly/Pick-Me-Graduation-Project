package com.asu.pick_me_graduation_project.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ahmed on 6/8/2016.
 */
public class SearchRideParams implements Serializable
{
    /* fields */
    Location source;
    Location destination;
    Calendar time;
    List<Community> filteredCommunities;
    List<Ride> result;

    /* constructor */
    public SearchRideParams()
    {
    }

    /* getters and setters */

    public Location getSource()
    {
        return source;
    }

    public void setSource(Location source)
    {
        this.source = source;
    }

    public Location getDestination()
    {
        return destination;
    }

    public void setDestination(Location destination)
    {
        this.destination = destination;
    }

    public Calendar getTime()
    {
        return time;
    }

    public void setTime(Calendar time)
    {
        this.time = time;
    }

    public List<Community> getFilteredCommunities()
    {
        return filteredCommunities;
    }

    public void setFilteredCommunities(List<Community> filteredCommunities)
    {
        this.filteredCommunities = filteredCommunities;
    }

    public List<Ride> getResult()
    {
        return result;
    }

    public void setResult(List<Ride> result)
    {
        this.result = result;
    }
}
