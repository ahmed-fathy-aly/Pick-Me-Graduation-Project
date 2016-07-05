package com.asu.pick_me_graduation_project.events;

/**
 * Created by ahmed on 7/5/2016.
 */
public class UpdateCommunityMembersEvent
{
    private String communityId;

    public UpdateCommunityMembersEvent(String userId)
    {
        this.communityId = userId;
    }

    public String getCommunityId()
    {
        return communityId;
    }
}
