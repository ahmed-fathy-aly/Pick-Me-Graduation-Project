package com.asu.pick_me_graduation_project.model;

import org.json.JSONObject;

/**
 * Created by ahmed on 3/3/2016.
 */
public class Community
{
    /* fields */
    String id;
    String name;
    String description;
    boolean isAdmin; // am i an admin of this community ?
    private boolean isMember;
    private String profilePictureUrl;

    /* setters and getters */

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public void setIsMember(boolean isMember)
    {
        this.isMember = isMember;
    }

    public boolean isMember()
    {
        return isMember;
    }

    public String getProfilePictureUrl()
    {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl)
    {
        this.profilePictureUrl = profilePictureUrl;
    }

    /* methods */
    public Community fromJson(JSONObject jsonObject)
    {
        Community community = new Community();

        // TODO - parse json

        return community;
    }



}
