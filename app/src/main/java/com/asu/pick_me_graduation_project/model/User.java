package com.asu.pick_me_graduation_project.model;

import org.json.JSONObject;

/**
 * Created by ahmed on 2/7/2016.
 */
public class User
{
    /* fields */
    String userId;
    String userName;
    String email;
    String fullName;
    String profilePictureUrl;
    double locationLatitude;
    double locationAltitude;
    String gender;

    /* setters and getters */

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public double getLocationAltitude()
    {
        return locationAltitude;
    }

    public void setLocationAltitude(double locationAltitude)
    {
        this.locationAltitude = locationAltitude;
    }

    public double getLocationLatitude()
    {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude)
    {
        this.locationLatitude = locationLatitude;
    }

    public String getProfilePictureUrl()
    {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl)
    {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /* methods */

    /**
     * parses a json to get a user object
     */
    public static User fromJson(JSONObject json)
    {
       User user = new User();

        // TODO - still waiting for backend to give the data

        return user;
    }
}
