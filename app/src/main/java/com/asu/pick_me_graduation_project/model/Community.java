package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ahmed on 3/3/2016.
 */
public class Community implements Serializable
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
    public static Community fromJson(JSONObject jsonObject)
    {
        Community community = new Community();

        // TODO - parse json
        try
        {
            if (!jsonObject.isNull("id"))
                community.setId(jsonObject.getString("id"));
            else
                community.setId(jsonObject.getString("communityId"));
            community.setName(jsonObject.getString("name"));
            if (!jsonObject.isNull("description"))
                community.setDescription(jsonObject.getString("description"));
            community.setProfilePictureUrl(jsonObject.getString("profilePicture"));
            if (!jsonObject.isNull("isAdmin"))
                community.setIsAdmin(jsonObject.getBoolean("isAdmin"));
            if (!jsonObject.isNull("isJoinedByMe"))
                community.setIsMember(jsonObject.getBoolean("isJoinedByMe"));
        } catch (Exception e)
        {
            e.printStackTrace();
            Log.e("Game", "parsing community error " + e.getMessage());
        }
        return community;
    }


}
