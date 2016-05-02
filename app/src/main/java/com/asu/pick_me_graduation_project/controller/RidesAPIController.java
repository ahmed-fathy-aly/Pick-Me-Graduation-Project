package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;

import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by ahmed on 5/2/2016.
 */
public class RidesAPIController
{
    /* fields */
    Context context;

    /* constructor */
    public RidesAPIController(Context context)
    {
        this.context = context;
    }

    /* methods */
    public void getMyRides(String token, final GetRidesCallback callback)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO now it's dummy data
                List<Ride> rideList = new ArrayList<>();
                for (int i = 0; i < 10; i++)
                {
                    User user = new User();
                    user.setUserId(i + "");
                    user.setFirstName("first");
                    user.setLastName("last");
                    user.setProfilePictureUrl("https://upload.wikimedia.org/wikipedia/en/7/70/Shawn_Tok_Profile.jpg");

                    Location l1 = new Location();
                    l1.setId("1");
                    l1.setLatitude(30.02);
                    l1.setLongitude(31.02);
                    l1.setType(Location.LocationType.SOURCE);
                    l1.setUser(user);

                    Location l2 = new Location();
                    l2.setId("2");
                    double d = new Random().nextInt(10) / 100.0;
                    l2.setLatitude(30.01 + d);
                    l2.setLongitude(31.01 + d);
                    l2.setType(Location.LocationType.DESTINATION);
                    l2.setUser(user);

                    Ride ride = new Ride();
                    ride.setId(i + "");
                    ride.setRider(user);
                    ride.setDescription("This is ride " + i);
                    ride.setTime(Calendar.getInstance());
                    ride.setLocations(Arrays.asList(l1, l2));

                    rideList.add(ride);
                }
                callback.success(rideList);
            }
        }, 2000);

    }

}