package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.LoginCallback;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.PreferencesUtils;
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
        // make a delay to mock the request
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // make mock data
                User user = new User();
                user.setEmail("egor@mail.com");
                user.setUserName("Egor");
                user.setFullName("Egor Kulikov");
                user.setProfilePictureUrl("http://graph.facebook.com/100001144443949/picture?type=square");
                user.setGender(Constants.GENDER_MALE);
                user.setLocationLatitude(30.0412772);
                user.setLocationAltitude(31.2658458);
                String token = "abfgfgf_fdsfd";

                // TODO update preferences
                setCurrentUser(user, token);

                // invoke callback
                callback.success(user, token);

            }
        }, 1000);

    }

    /**
     * saves the user's info to the preferences
     */
    private void setCurrentUser(User user, String authorizationToken)
    {
        PreferencesUtils.clear(context);
        PreferencesUtils.setUser(context, user);
        PreferencesUtils.setAuthToken(context, authorizationToken);
    }



}
