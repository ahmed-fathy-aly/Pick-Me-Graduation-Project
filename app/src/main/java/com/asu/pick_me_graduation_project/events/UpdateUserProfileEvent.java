package com.asu.pick_me_graduation_project.events;

/**
 * Created by ahmed on 7/3/2016.
 */
public class UpdateUserProfileEvent
{
    private String userId;

    public UpdateUserProfileEvent(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
}
