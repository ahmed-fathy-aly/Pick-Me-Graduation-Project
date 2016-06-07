package com.asu.pick_me_graduation_project.model;

import android.util.Log;

import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ahmed on 5/2/2016.
 */
public class Ride
{
    /* fields */
    String id;
    String description;
    User rider;
    Calendar time;
    List<Location> locations;
    RideDetails rideDetails;
     String notes;

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

    public User getRider()
    {
        return rider;
    }

    public void setRider(User rider)
    {
        this.rider = rider;
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

        } catch (Exception e)
        {
            Log.e("Game", "parse ride exception " + e.getMessage());
        }
        return ride;
    }
}

