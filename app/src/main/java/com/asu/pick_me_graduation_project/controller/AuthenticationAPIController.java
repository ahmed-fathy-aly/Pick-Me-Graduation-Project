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
                user.setUserId("42");
                user.setEmail("egor@mail.com");
                user.setFirstName("Egor");
                user.setLastName("Egor Kulikov");
                user.setProfilePictureUrl("http://graph.facebook.com/100001144443949/picture?type=square");
                user.setGender(Constants.GENDER_MALE);
                user.setLocationLatitude(30.0412772);
                user.setLocationAltitude(31.2658458);
                String token = "abfgfgf_fdsfd";

                // update preferences
                setCurrentUser(user, token);

                // invoke callback
                callback.success(user, token);

            }
        }, 1000);

    }

    /**
     * creates a new user profile then logs him in
     * the result will have the user details (the ones sent)and an authentication token
     * these results will be saved in the pereferences
     */
    public void signUp(final String email, final String firstName, final String lastName, String password, final String gender, final SignUpCallback callback)
    {
        // make a delay to mock the request
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // make mock data
                User user = new User();
                user.setUserId("42");
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setProfilePictureUrl("http://graph.facebook.com/100001144443949/picture?type=square");
                user.setGender(gender);
                user.setLocationLatitude(30.0412772);
                user.setLocationAltitude(31.2658458);
                user.setPhoneNumber("0114385332");
                user.setBio("this is my bio....");

                CarDetails carDetails = new CarDetails();
                carDetails.setModel("Kia");
                carDetails.setYear("2013");
                carDetails.setConditioned(true);
                carDetails.setPlateNumber("1234");
                user.setCarDetails(carDetails);

                String token = "abfgfgf_fdsfd";

                // update preferences
                setCurrentUser(user, token);

                // invoke callback
                callback.success(user, token);

            }
        }, 1000);
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
    private void setCurrentUser(User user, String authorizationToken)
    {
        Log.e("Game", "set user " + user.getUserId());
        PreferencesUtils.clear(context);
        PreferencesUtils.setUser(context, user);
        PreferencesUtils.setAuthToken(context, authorizationToken);
    }



}
