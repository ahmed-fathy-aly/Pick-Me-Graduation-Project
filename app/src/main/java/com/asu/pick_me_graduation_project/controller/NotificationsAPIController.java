package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GetNotificationsCallback;
import com.asu.pick_me_graduation_project.model.DrivingFeedback;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.Notification;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ahmed on 7/2/2016.
 */
public class NotificationsAPIController
{
    /* fields */
    Context context;

    /* constructor */
    public NotificationsAPIController(Context context)
    {
        this.context = context;
    }

    /* methods */

    /**
     * gets the notifications for the current user
     */
    public void getNotifications(String token, final GetNotificationsCallback callback)
    {
        String url = Constants.HOST + "notifications/get_my_notifications";

        Ion.with(context)
                .load("GET", url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {

                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }

                        Log.e("Game", "get notifications = " + result);

                        //parse the  response
                        try
                        {
                            // check the status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("feedback");
                                callback.fail(message);
                                return;
                            }

                            // parse the notifications list
                            JSONArray notificationsJson = response.getJSONArray("myNotifications");
                            List<Notification> notificationList = new ArrayList<Notification>();
                            for (int i = 0; i < notificationsJson.length(); i++)
                                notificationList.add(Notification.fromJson(notificationsJson.getJSONObject(i)));

                            // invoke callback
                            callback.success(notificationList);
                        } catch (Exception e2)
                        {
                            Log.e("Game", "error parsing notifications " + e2.getMessage());
                            callback.fail(e2.getMessage());
                        }

                    }
                });

    }

}
