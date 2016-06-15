package com.asu.pick_me_graduation_project.callback;

import com.asu.pick_me_graduation_project.model.JoinRideRequest;
import com.asu.pick_me_graduation_project.model.Ride;

import java.util.List;

/**
 * Created by ahmed on 5/2/2016.
 */
public interface GetRideJoinRequestsCallback
{
    void success(List<JoinRideRequest> requests);

    void fail(String error);
}
