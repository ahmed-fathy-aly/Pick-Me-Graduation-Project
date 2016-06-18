package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.EditProfileCallback;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String url = Constants.HOST +  "get_profile"
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
    public void editProfile(final User user, String token, final EditProfileCallback callback)
    {
        String url = Constants.HOST + "EditProfile";
        JsonObject json = new JsonObject();
        json.addProperty("bio", user.getBio());
        json.addProperty("firstName", user.getFirstName());
        json.addProperty("lastName", user.getLastName());
        json.addProperty("dob", user.getdob());
        json.addProperty("residence", user.getResidence());
        //json.addProperty("carAc", user.getCarDetails().isConditioned());
        json.addProperty("carModel", user.getCarDetails().getModel());
        json.addProperty("caryear", user.getCarDetails().getYear());
        json.addProperty("carPlateNumber", user.getCarDetails().getPlateNumber());
        json.addProperty("profilePicture", user.getProfilePictureUrl());



        String body = json.toString();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        Fuel.put(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                    @Override
                    public void success(Request request, Response r, String s) {
                        try {
                            // check status
                            JSONObject response = new JSONObject(s);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse community
                            JSONObject userJson = response.getJSONObject("user");
                            User user = User.fromJson(userJson);

                            // invoke callback
                            callback.success(user);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response resp, FuelError fuelError) {
                        callback.fail(fuelError.getMessage());
                    }
                });

    }

    /**
     * seraches for all users having that substring
     */
    public void searchusers(final String searchString, final GetUsersCallback callback)
    {
        // cancel any previous request
        if (searchUsersRequest != null)
            searchUsersRequest.cancel();

        String url = Constants.HOST + "search_for_user"
                + "?searchString=" + searchString
                + "&count=-1";
        Log.e("Game", "searching for " + url);
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
                        JSONObject userJson = usersJson.getJSONObject(i);
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
