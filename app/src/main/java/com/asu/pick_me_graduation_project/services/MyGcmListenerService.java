package com.asu.pick_me_graduation_project.services;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.google.android.gms.gcm.GcmListenerService;

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
        Log.e("Game", "message = " + bundle.getString("message"));

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

        // build the notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setAutoCancel(true);

        /*
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ResultActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ResultActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        */

        // post notification
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(getNotificationId(), mBuilder.build());
    }

    /**
     * announcement -> new id every time
     */
    private int getNotificationId()
    {
        return new Random().nextInt(1000000);
    }


}
