package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ahmed on 5/2/2016.
 */
public class Ride implements Serializable
{
    /* fields */
    String id;
    String description;
    User driver;
    Calendar time;
    List<Location> locations;
    RideDetails rideDetails;
    String notes;
    boolean canRequestToJoin;

    /* constructor */
    public Ride()
    {
        this.rideDetails = new RideDetails();
    }

    /* setters and getters */

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public User getDriver()
    {
        return driver;
    }

    public void setDriver(User rider)
    {
        this.driver = rider;
    }

    public Calendar getTime()
    {
        return time;
    }

    public void setTime(Calendar time)
    {
        this.time = time;
    }

    public List<Location> getLocations()
    {
        return locations;
    }

    public void setLocations(List<Location> locations)
    {
        this.locations = locations;
    }

    public RideDetails getRideDetails()
    {
        return rideDetails;
    }

    public void setRideDetails(RideDetails rideDetails)
    {
        this.rideDetails = rideDetails;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }


    public boolean isCanRequestToJoin()
    {
        return canRequestToJoin;
    }

    public void setCanRequestToJoin(boolean canRequestToJoin)
    {
        this.canRequestToJoin = canRequestToJoin;
    }

    /* methods */

    /**
     * gathers all the users that have locations in the ride
     */
    public List<User> getMembers()
    {
        HashSet<String> ids = new HashSet<>();
        List<User> memberList = new ArrayList<>();

        for (Location location : locations)
            if (!ids.contains(location.getUser().getUserId()))
            {
                ids.add(location.getUser().getUserId());
                memberList.add(location.getUser());
            }

        return memberList;
    }

    /**
     * @return whether that used id is among the members list
     */
    public boolean containsMember(String currentUserId)
    {
        List<User> members = getMembers();
        for (User member : members)
            if (member.getUserId().equals(currentUserId))
                return true;
        return false;
    }

    public static Ride fromJson(JSONObject jsonObject)
    {
        Ride ride = new Ride();

        try
        {
            // basic info
            ride.setId(jsonObject.getString("id"));
            ride.setDescription(jsonObject.getString("description"));
            ride.setNotes(jsonObject.getString("notes"));
            ride.setTime(TimeUtils.parseCalendar(jsonObject.getString("time")));

            // poster
            ride.setDriver(User.fromJson(jsonObject.getJSONObject("postUser")));

            // locations
            JSONArray locationsJson = jsonObject.getJSONArray("locations");
            List<Location> locations = new ArrayList<>();
            for (int i = 0; i < locationsJson.length(); i++)
                locations.add(Location.fromJson(locationsJson.getJSONObject(i)));
            Collections.sort(locations, new Comparator<Location>()
            {
                @Override
                public int compare(Location lhs, Location rhs)
                {

                    if (lhs.getOrder() != rhs.getOrder())
                        return lhs.getOrder() < rhs.getOrder() ? -1 : 1;
                    else
                        return 0;
                }
            });
            ride.setLocations(locations);

            // extra details
            if (!jsonObject.isNull("ladiesOnly"))
                ride.getRideDetails().setLadiesOnly(jsonObject.getBoolean("ladiesOnly"));
            if (!jsonObject.isNull("noSmoking"))
                ride.getRideDetails().setNoSmoking(jsonObject.getBoolean("noSmoking"));
            if (!jsonObject.isNull("disabledWelcomed"))
                ride.getRideDetails().setDisabledWelcomed(jsonObject.getBoolean("disabledWelcomed"));
            if (!jsonObject.isNull("numberOfFreeSeats"))
                ride.getRideDetails().setNumberOfFreeSeats(jsonObject.getInt("numberOfFreeSeats"));

            // car info
            CarDetails carDetails = CarDetails.fromJson(jsonObject);
            ride.getRideDetails().setCarDetails(carDetails);

            // can request
            if (!jsonObject.isNull("canRequest"))
                ride.setCanRequestToJoin(jsonObject.getBoolean("canRequest"));
        } catch (Exception e)
        {
            Log.e("Game", "parse ride exception " + e.getMessage());
        }
        return ride;
    }

}

