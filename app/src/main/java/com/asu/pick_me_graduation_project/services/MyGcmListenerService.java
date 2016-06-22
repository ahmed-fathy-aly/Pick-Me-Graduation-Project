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
import com.asu.pick_me_graduation_project.activity.FeedBackActivity;
import com.asu.pick_me_graduation_project.activity.MyApplication;
import com.asu.pick_me_graduation_project.activity.RideDetailsActivity;
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
        String toUserId = bundle.getString("toUserId");
        String currentUserId = new AuthenticationAPIController(this).getCurrentUser().getUserId();

        // check it's sent to me
        if (toUserId != null && toUserId.length() > 0)
            if (currentUserId == null || !currentUserId.equals(toUserId))
                return;

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
            case "announcement":
                break;
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
                    notificationId = Constants.getNotificationId(Constants.NOTIFICATION_CHAT_ID_SUGGESTED_BY_OSSAMA_7ABIBY, otherUserid);

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

            case "joinRideRequest":
                try
                {
                    // parse the data
                    JSONObject rideJson = new JSONObject(data);
                    String rideId = rideJson.getString("rideId");

                    // set the pending intent
                    Intent rideDetailsIntent = new Intent(this, RideDetailsActivity.class);
                    rideDetailsIntent.putExtra(Constants.RIDE_ID, rideId);
                    rideDetailsIntent.putExtra(RideDetailsActivity.SWITCH_TO_REQUEST_TAB, true);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), rideDetailsIntent, 0);
                    mBuilder.setContentIntent(pendingIntent);

                    // unique notification
                    notificationId = Constants.getNotificationId(Constants.NOTIFICATION_JOIN_RIDE_ID, rideId);
                } catch (JSONException e)
                {
                    Log.e("Game", "error " + e.getMessage());
                    e.printStackTrace();
                }
                break;

            case "takeSelfie":
                break;

            case "sendFeedback":
                try
                {
                    // parse the data
                    JSONObject rideJson =new JSONObject(data);
                    String rideId = rideJson.getString("rideId");

                    // set the pending intent
                    Intent feedbackIntent = new Intent(this, FeedBackActivity.class);
                    feedbackIntent.putExtra(Constants.RIDE_ID, rideId);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), feedbackIntent, 0);
                    mBuilder.setContentIntent(pendingIntent);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                break;

        }

        // post notification
        if (showNotification)
        {
            NotificationManager mNotificationManager =
                    (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(notificationId, mBuilder.build());
        }
    }


}
