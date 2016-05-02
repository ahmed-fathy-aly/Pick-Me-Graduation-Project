package com.asu.pick_me_graduation_project.model;

/**
 * Created by ahmed on 5/2/2016.
 */
public class Location
{
    /* fields */
    String id;
    double latitude;
    double longitude;
    LocationType type;
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

    public enum LocationType
    {
        SOURCE, DESTINATION
    }
}
