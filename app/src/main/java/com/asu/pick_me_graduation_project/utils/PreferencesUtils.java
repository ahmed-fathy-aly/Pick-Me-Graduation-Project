package com.asu.pick_me_graduation_project.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.asu.pick_me_graduation_project.model.User;

/**
 * Created by ahmed on 2/7/2016.
 */
public class PreferencesUtils
{
    /* constants */
    private static final String KEY_PREFERENCES_NAME = "pickMePreferences";

    /* getters and setters */

    /**
     * clears all data in preferences
     */
    public static void clear(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }


    public static void setUser(Context context, User user)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        editor.putString("user_id", user.getUserId());
        editor.putString("user_name", user.getFirstName());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_profile_picture_url", user.getProfilePictureUrl());
        editor.putString("user_full_name", user.getLastName());
        editor.putString("user_gender", user.getGender());
        editor.putFloat("user_location_lat", (float) user.getLocationLatitude());
        editor.putFloat("user_location_alt", (float) user.getLocationAltitude());

        editor.commit();
    }

    public static User getUser(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);

        User user = new User();
        user.setUserId(pref.getString("user_id", ""));
        user.setFirstName(pref.getString("user_name", ""));
        user.setEmail(pref.getString("user_email", ""));
        user.setProfilePictureUrl(pref.getString("user_profile_picture_url", ""));
        user.setLastName(pref.getString("user_full_name", ""));
        user.setGender(pref.getString("user_gender", ""));
        user.setLocationLatitude(pref.getFloat("user_location_lat", 0));
        user.setLocationAltitude(pref.getFloat("user_location_alt", 0));


        return user;
    }


    public static void setAuthToken(Context context, String authorizationToken)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("auth_token", authorizationToken);

        editor.commit();
    }


    public static String getAuthenticationToken(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        return pref.getString("auth_token", "");

    }
}
