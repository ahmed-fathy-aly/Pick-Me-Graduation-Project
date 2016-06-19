package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetFeedbackFormCallback;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 6/14/2016.
 */
public class FeedbackAPIController
{
    /* fields */
    Context context;

    /* constructor */
    public FeedbackAPIController(Context context)
    {
        this.context = context;
    }

    /* methods */

    /**
     * gets the list of users who are on the ride and which one is the rider
     */
    public void getFeedbackForm(String token, String rideId, GetFeedbackFormCallback callback)
    {
        // TODO invoke callback on mock data
        User user1=new User();  user1.setUserId("1");user1.setFirstName("Ahmed");user1.setLastName("Ali");
        user1.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        User user2=new User();user2.setUserId("2");user2.setFirstName("Mohamed");user2.setLastName("Hussien");
        user2.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        User user3=new User();user3.setUserId("3");user3.setFirstName("Nadeen");user3.setLastName("Bora3i");
        user3.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        Ride ride=new Ride();
        ride.setId(rideId);
        User user4;
        user4=ride.getRider();user4.setFirstName("Abo Bakr");user4.setLastName("Walid ");
        user4.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
       String driverid = user4.getUserId();
        List<User> passengers = new ArrayList<>();
        passengers.add(user1);
        passengers.add(user2);
        passengers.add(user3);
        passengers.add(user4);

        callback.success(passengers,driverid);

    }

    /**
     * posts feedbacks for each user in the ride
     * the feedback object will contain specificDriverFeedback only for the driver
     */
    public void postFeedback(String token, String rideId, List<Feedback> feedbackList, GenericSuccessCallback callback)
    {
        // TODO invoke callback on mock data
    }
}
