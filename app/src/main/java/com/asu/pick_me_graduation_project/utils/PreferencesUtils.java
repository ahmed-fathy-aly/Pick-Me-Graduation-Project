package com.asu.pick_me_graduation_project.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.asu.pick_me_graduation_project.model.User;

/**
 * Created by ahmed on 2/7/2016.
 */
public class PreferencesUtils
{
    private static final String KEY_PREFERENCES_NAME = "myPreferences";

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
        editor.putString("user_name", user.getUserName());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_profile_picture_url", user.getProfilePictureUrl());
        editor.putString("user_full_name", user.getFullName());
        editor.putString("user_gender", user.getGender());
        editor.putFloat("user_location_lat", (float) user.getLocationLatitude());
        editor.putFloat("user_location_alt", (float) user.getLocationAltitude());

        editor.commit();

    }

    public static void setAuthToken(Context context, String authorizationToken)
    {
        SharedPreferences pref = context.getSharedPreferences(KEY_PREFERENCES_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("auth_token", authorizationToken);

        editor.commit();
    }
}
