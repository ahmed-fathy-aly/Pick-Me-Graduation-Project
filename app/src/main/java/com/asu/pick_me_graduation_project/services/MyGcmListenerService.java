package com.asu.pick_me_graduation_project.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.ChatActivity;
import com.asu.pick_me_graduation_project.activity.MyApplication;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.events.NewMessageEvent;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.google.android.gms.gcm.GcmListenerService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by ahmed on 6/17/2016.
 */
public class MyGcmListenerService extends GcmListenerService
{
    @Override
    public void onMessageReceived(String s, Bundle bundle)
    {
        Log.e("Game", "Notification !!");
        sendNotification(bundle);

    }

    /**
     * posts a notification with an action that depends on the notification type
     *
     * @param bundle
     */
    private void sendNotification(Bundle bundle)
    {
        // get data
        String type = bundle.getString("type");
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String data = bundle.getString("data");

        // build the notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setAutoCancel(true);

        // check action based on the notification type
        int notificationId = new Random().nextInt(10000);
        boolean showNotification = true;
        switch (type)
        {
            case "chat":
                try
                {
                    // get the message
                    JSONObject chatJson = new JSONObject(data);
                    ChatMessage chatMessage = ChatMessage.fromJson(chatJson);

                    // notify the chat activity
                    String myId = new AuthenticationAPIController(this).getCurrentUser().getUserId();
                    String otherUserid = myId.equals(chatMessage.getFrom().getUserId()) ?
                            chatMessage.getTo().getUserId() : chatMessage.getFrom().getUserId();
                    EventBus.getDefault().post(new NewMessageEvent(chatMessage, otherUserid));

                    // set the pending intent
                    Intent chatIntent = new Intent(this, ChatActivity.class);
                    chatIntent.putExtra(Constants.USER_ID, otherUserid);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), chatIntent, 0);
                    mBuilder.setContentIntent(pendingIntent);

                    // unique notification
                    notificationId = Constants.getNotificationId(otherUserid);

                    // check if the chat activity is already on then don't post the notification
                    String currentChatUser = ((MyApplication) getApplication()).currentChat;
                    if (currentChatUser != null && currentChatUser.equals(otherUserid))
                        showNotification = false;
                } catch (JSONException e)
                {
                    Log.e("Game", "error " + e.getMessage());
                    e.printStackTrace();
                }
                break;
        }

        // post notification
        if(showNotification)
        {
            NotificationManager mNotificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationId, mBuilder.build());
        }
        }


}
