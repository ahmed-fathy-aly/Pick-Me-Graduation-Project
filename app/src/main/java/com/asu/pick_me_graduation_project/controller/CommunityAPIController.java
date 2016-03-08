package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;

import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.model.Community;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /**
     * makes a new community with that user as admin
     */
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

    public void getMyCommunities(String userId, final GetCommunitiesCallback callback)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {


                // mock data
                List<Community> communityList = new ArrayList<>();
                for (int i = 0; i < 5; i++)
                {
                    Community community = new Community();
                    community.setId(i + "");
                    community.setIsAdmin(i % 3 == 0);
                    community.setIsMember(true);
                    community.setName("Community " + i);
                    community.setDescription("description " + i);
                    community.setProfilePictureUrl("http://faculty.engineering.asu.edu/zhao/wp-content/uploads/2014/08/ASU-new-logo.jpg");
                    communityList.add(community);

                }

                callback.success(communityList);
            }
        }, 1000);
    }

    public void searchCommunities(String userId, final String searchString, final GetCommunitiesCallback callback)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {


                // mock data
                List<Community> communityList = new ArrayList<>();
                int size = new Random().nextInt(6) + 1;
                for (int i = 0; i < size; i++)
                {
                    Community community = new Community();
                    community.setId(i + "");
                    community.setIsAdmin(i % 3 == 0);
                    community.setIsMember(true);
                    community.setName(searchString + i);
                    community.setDescription("description " + i);
                    community.setProfilePictureUrl("http://faculty.engineering.asu.edu/zhao/wp-content/uploads/2014/08/ASU-new-logo.jpg");
                    communityList.add(community);

                }

                callback.success(communityList);
            }
        }, 1000);
    }


}
