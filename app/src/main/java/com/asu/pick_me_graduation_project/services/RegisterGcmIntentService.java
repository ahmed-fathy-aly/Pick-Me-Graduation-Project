package com.asu.pick_me_graduation_project.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.PreferencesUtils;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * uploads the token to the backend
     */
    private void sendTokenToBackend(String gcmToken)
    {
        // form the request
        String url = Constants.HOST + "notifications/set_notificationsToken";

        final String token = new AuthenticationAPIController(this).getTokken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("notificationsToken", gcmToken);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String body = jsonObject.toString();

        // make the request
        Fuel.put(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new Handler<String>()
                {
                    @Override
                    public void success(Request request, Response response, String s)
                    {
                        PreferencesUtils.setAuthToken(getApplicationContext(), token);
                        Log.e("Game", "token sent " + token);
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                    }
                });
    }
}
