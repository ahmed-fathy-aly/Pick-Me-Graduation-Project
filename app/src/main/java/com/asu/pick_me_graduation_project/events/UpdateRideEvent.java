package com.asu.pick_me_graduation_project.events;

/**
 * Created by ahmed on 7/12/2016.
 */
public class UpdateRideEvent
{
    private String rideId;

    public UpdateRideEvent(String rideId)
    {
        this.rideId = rideId;
    }

    public String getRideId()
    {
        return rideId;
    }
}
