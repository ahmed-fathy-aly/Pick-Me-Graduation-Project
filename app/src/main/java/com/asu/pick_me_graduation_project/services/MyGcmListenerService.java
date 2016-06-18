package com.asu.pick_me_graduation_project.services;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by ahmed on 6/17/2016.
 */
public class MyGcmListenerService extends GcmListenerService
{
    @Override
    public void onMessageReceived(String s, Bundle bundle)
    {
        Log.e("Game", "Notification !!");
        Log.e("Game", "message = " + bundle.getString("message"));


    }


}
