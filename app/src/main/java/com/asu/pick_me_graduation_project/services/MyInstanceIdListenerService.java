package com.asu.pick_me_graduation_project.services;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by ahmed on 6/17/2016.
 */
public class MyInstanceIdListenerService extends InstanceIDListenerService
{
    @Override
    public void onTokenRefresh()
    {
        startService(new Intent(this, RegisterGcmIntentService.class));
    }
}
