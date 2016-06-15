package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetFeedbackFormCallback;
import com.asu.pick_me_graduation_project.model.Feedback;

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
