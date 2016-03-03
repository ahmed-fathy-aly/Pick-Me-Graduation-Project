package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
import com.asu.pick_me_graduation_project.model.Community;

/**
 * Created by ahmed on 3/3/2016.
 */
public class CommunityAPIController
{
    Context context;

    public CommunityAPIController(Context context)
    {
        this.context = context;
    }

    public void createCommunity(String userId, String name, String description, CreateCommunityCallback callback)
    {
        // TODO - make a post to the back end

        // mock data for now
        Community community = new Community();
        community.setId("42");
        community.setName(name);
        community.setDescription(description);
        community.setIsAdmin(true);
        callback.success(community);
    }

}
