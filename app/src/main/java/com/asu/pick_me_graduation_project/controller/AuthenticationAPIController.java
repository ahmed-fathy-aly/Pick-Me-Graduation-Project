package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.LoginCallback;
import com.asu.pick_me_graduation_project.callback.SignUpCallback;
import com.asu.pick_me_graduation_project.model.CarDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.PreferencesUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmed on 12/17/2015.
 * manages login and sign up
 */
public class AuthenticationAPIController
{

    /* fields */
    private Context context;

    public AuthenticationAPIController(Context context)
    {
        this.context = context;
    }

    /* methods */

    /**
     * logs in a user that has signed up before
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     */
    public void login(String mail, String password, final LoginCallback callback)
    {

        // make a post request
        String url = "http://pickmeasu.azurewebsites.net/api/login";
        JsonObject userJson = new JsonObject();
        userJson.addProperty("email", mail);
        userJson.addProperty("password", password);
        Ion.with(context)
                .load("GET", url)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(userJson)
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

                        // parse the response
                        Log.e("Game", "log in result = " + result);
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
                            String token = response.getString("token");

                            // update preferences
                            setCurrentUser(user, token);

                            // invoke callback
                            callback.success(user, token);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });


    }

    /**
     * creates a new user profile then logs him in
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     */
    public void signUp(final String email, final String firstName, final String lastName, String password, final String gender, final SignUpCallback callback)
    {
        // make user json
        JsonObject userJson = new JsonObject();
        userJson.addProperty("fname", firstName);
        userJson.addProperty("lname", lastName);
        userJson.addProperty("email", email);
        userJson.addProperty("password", password);
        userJson.addProperty("gender", gender.equals(Constants.GENDER_MALE) ? 1 : 0);

        Log.e("Game", "sent user" + userJson.toString());

        // make a post request
        Ion.with(context)
                .load("POST", "http://pickmeasu.azurewebsites.net/api/sign_up")
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(userJson)
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

                        // parse the response
                        Log.e("Game", "sign up result = " + result);
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
                            String token = response.getString("token");

                            // update preferences
                            setCurrentUser(user, token);

                            // invoke callback
                            callback.success(user, token);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });

    }


    /**
     * calls api backend
     * clear the preferences
     */
    public void logOut()
    {
        PreferencesUtils.clear(context);
    }

    /**
     * returns the info of the user that has previously logged in
     *
     * @return null if no user is logged in
     */
    public User getCurrentUser()
    {
        return PreferencesUtils.getUser(context);
    }

    /**
     * checks if there's a user that has logged in before
     * a logged in user has an authentication token
     */
    public boolean isUserLoggedIn()
    {
        return PreferencesUtils.getAuthenticationToken(context).length() > 0;
    }

    /**
     * saves the user's info to the preferences
     */
    public void setCurrentUser(User user, String authorizationToken)
    {
        PreferencesUtils.clear(context);
        PreferencesUtils.setUser(context, user);
        PreferencesUtils.setAuthToken(context, authorizationToken);
    }

    /**
     * updates saved user info
     */
    public void updateUser(User user)
    {
        PreferencesUtils.setUser(context, user);
    }
}
