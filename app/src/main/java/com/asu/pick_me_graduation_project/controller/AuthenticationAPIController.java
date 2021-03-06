package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.LoginCallback;
import com.asu.pick_me_graduation_project.database.DatabaseHelper;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.PreferencesUtils;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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

        // the request header
        String url = Constants.HOST
                + "/login";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // form the body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", mail);
        jsonObject.addProperty("password", password);
        String body = jsonObject.toString();


        // make the post
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new Handler<String>()
                {
                    @Override
                    public void success(Request request, Response fuelResponse, String result)
                    {
                        handleLoginResponse(callback, result);
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });


    }

    /**
     * logs in a user that may or may not have signed in before with facebook
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     */
    public void loginByFacebook(String token, final LoginCallback callback)
    {

        // the request header
        String url = Constants.HOST
                + "/login_by_facebook";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // form the body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);
        String body = jsonObject.toString();


        // make the post
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new Handler<String>()
                {
                    @Override
                    public void success(Request request, Response fuelResponse, String result)
                    {
                        handleLoginResponse(callback, result);

                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });

    }

    /**
     * logs in a user that may or may not have signed in before with facebook
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     */
    public void loginByGmail(String token, final LoginCallback callback)
    {

        // the request header
        String url = Constants.HOST
                + "/login_by_google";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // form the body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);
        String body = jsonObject.toString();


        // make the post
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new Handler<String>()
                {
                    @Override
                    public void success(Request request, Response fuelResponse, String result)
                    {
                        handleLoginResponse(callback, result);
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });
    }

    /**
     * creates a new user profile then logs him in
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     */
    public void signUp(final String email, final String firstName, final String lastName
            , String password, final String gender, String profilePicture
            ,final LoginCallback callback)
    {
        // the request header
        String url = Constants.HOST
                + "/sign_up";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        // form the body
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);
        json.addProperty("firstName", firstName);
        json.addProperty("lastName", lastName);
        json.addProperty("profilePicture", profilePicture);
        json.addProperty("gender", gender.equals(Constants.GENDER_MALE));
        String body = json.toString();

        // make the post
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new Handler<String>()
                {
                    @Override
                    public void success(Request request, Response fuelResponse, String result)
                    {
                        handleLoginResponse(callback, result);
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });
    }



    private void handleLoginResponse(LoginCallback callback, String result)
    {
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

    /**
     * calls api backend
     * clear the preferences
     * clears the database
     */
    public void logOut()
    {
        PreferencesUtils.clear(context);
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.clearTables();
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
     * returns the authorization tokken of the user currently logged in
     * returns "" if no user is logged in
     */
    public String getTokken()
    {
        return PreferencesUtils.getAuthenticationToken(context);
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
