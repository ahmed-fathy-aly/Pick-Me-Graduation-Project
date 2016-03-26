package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.User;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public void createCommunity(String token, final String name, final String description, final CreateCommunityCallback callback)
    {

        // TODO - make a post to the back end
        JsonObject json = new JsonObject();
        json.addProperty("communityName", name);
        //json.addProperty("description", description);

        Ion.with(context)
                .load("http://pickmeasu.azurewebsites.net/api/Create_Community")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Log.e("Game", "error " + e.getMessage());
                        } else
                            Log.e("Game", "test create community result " + result);
                    }
                });


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
                    community.setIsMember(new Random(3).nextInt() == 0);
                    community.setName(searchString + i);
                    community.setDescription("description " + i);
                    community.setProfilePictureUrl("http://faculty.engineering.asu.edu/zhao/wp-content/uploads/2014/08/ASU-new-logo.jpg");
                    communityList.add(community);

                }

                callback.success(communityList);
            }
        }, 1000);
    }


    /**
     * gets the members who are in this community
     */
    public void getCommunityMembers(String tokken, String userId, String communityId, final GetUsersCallback callback)
    {
        // TODO now it just gets all users who have the character m

        String url = "http://pickmeasu.azurewebsites.net/api/search_for_user"
                + "?searchString=" + "m"
                + "&count=-1";
        Log.e("Game", "searching for " + url);
        Ion.with(context)
                .load("GET", url)
                .setHeader("Content-Type", "application/json")
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check failed
                        if (e != null)
                        {
                                callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "search result = " + result);

                        // parse the response
                        try
                        {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse user
                            JSONArray usersJson = response.getJSONArray("users");
                            List<User> usersList = new ArrayList<User>();
                            for (int i = 0; i < usersJson.length(); i++)
                            {
                                JSONObject userJson = usersJson.getJSONObject(i).getJSONObject("user");
                                User user = User.fromJson(userJson);
                                usersList.add(user);

                            }

                            // invoke callback
                            callback.success(usersList);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }
}
