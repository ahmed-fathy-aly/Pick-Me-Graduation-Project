package com.asu.pick_me_graduation_project.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.utils.PreferencesUtils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by ahmed on 6/17/2016.
 */
public class RegisterGcmIntentService extends IntentService
{
    public RegisterGcmIntentService()
    {
        super("RegisterGcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        // get the token
        try
        {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e("Game", "gcm token =  " + token);
            if (!PreferencesUtils.getGcmToken(this).equals(token))
                sendTokenToBackend(token);

        } catch (IOException e)
        {
            Log.e("Game", "error getting token " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendTokenToBackend(String token)
    {

    }
}
