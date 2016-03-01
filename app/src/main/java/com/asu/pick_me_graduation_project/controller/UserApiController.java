package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.EditProfileCallback;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.callback.SearchUserCallback;
import com.asu.pick_me_graduation_project.model.CarDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 2/7/2016.
 * manages general requests made by user
 */
public class UserApiController
{
    /* fields */
    Context context;
    private ResponseFuture<String> searchUsersRequest;

    /* constructor */
    public UserApiController(Context context)
    {
        this.context = context;
    }

    /* methods */

    /**
     * gets the profile info of a specific user
     */
    public void getProfile(String userId, final GetProfileCallback callback)
    {
        String url = "http://pickmeasu.azurewebsites.net/api/get_profile"
                + "?userId=" + userId;
        Ion.with(context)
                .load("GET", url)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {

                        // check failed
                        if (e != null)
                        {
                            Log.e("Game", "error " + e.getMessage());
                            callback.fail(e.getMessage());
                            return;
                        }

                        // parse the response
                        Log.e("Game", "get profile result = " + result);
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
                            JSONObject userJson = response.getJSONObject("user");
                            User user = User.fromJson(userJson);


                            // invoke callback
                            callback.success(user);
                        } catch (Exception e2)
                        {
                            Log.e("Game", "parsing failed " + e2.getMessage());
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    /**
     * updates the user's profile
     */
    public void editProfile(final User user, final EditProfileCallback callback)
    {
        // make a delay to mock the request
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

                // invoke callback
                callback.success(user);

            }
        }, 1000);
    }

    /**
     * seraches for all users having that substring
     */
    public void searchusers(final String searchString, final SearchUserCallback callback)
    {
        // cancel any previous request
        if (searchUsersRequest != null)
        {
            searchUsersRequest.cancel();
        }

        String url = "http://pickmeasu.azurewebsites.net/api/search"
                + "?searchString=" + searchString
                + "&count=-1";
        searchUsersRequest = Ion.with(context)
                .load("GET", url)
                .setHeader("Content-Type", "application/json")
                .asString();
        searchUsersRequest.setCallback(new FutureCallback<String>()
        {
            @Override
            public void onCompleted(Exception e, String result)
            {
                // check failed
                if (e != null)
                {
                    if (!searchUsersRequest.isCancelled())
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
