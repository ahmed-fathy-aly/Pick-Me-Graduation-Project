package com.asu.pick_me_graduation_project.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.database.DatabaseHelper;
import com.asu.pick_me_graduation_project.events.MyRidesUpdatedEvent;
import com.asu.pick_me_graduation_project.model.Ride;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by ahmed on 6/26/2016.
 * A service to download my rides and saved in the database
 */
public class GetMyRidesService extends Service
{


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        RidesAPIController controller = new RidesAPIController(this);
        controller.getMyRides(
                new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , new GetRidesCallback()
                {
                    @Override
                    public void success(final List<Ride> rides)
                    {
                        new Handler().post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // save my rides in the database
                                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                                databaseHelper.clearTables();
                                databaseHelper.insertRides(rides);

                                // notify if the MyRidesActivity is open
                                EventBus.getDefault().post(new MyRidesUpdatedEvent());
                                stopSelf();
                            }
                        });

                    }

                    @Override
                    public void fail(String error)
                    {
                        stopSelf();
                    }
                }
        );


        return START_STICKY;
    }
}
