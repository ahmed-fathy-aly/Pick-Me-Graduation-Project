package asu.com.pick_me_graduation_project;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.asu.pick_me_graduation_project.database.DatabaseHelper;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.RideDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;

import java.util.Arrays;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DatabaseTest extends ApplicationTestCase<Application>
{
    public DatabaseTest()
    {
        super(Application.class);
    }


    public void testMyRides()
    {
        // create users
        User user1 = new User();
        user1.setUserId("1");
        user1.setFirstName("first1");
        user1.setLastName("last1");
        user1.setProfilePictureUrl("picture1");

        User user2 = new User();
        user2.setUserId("2");
        user2.setFirstName("first2");
        user2.setLastName("last2");
        user2.setProfilePictureUrl("picture2");

        User user3 = new User();
        user3.setUserId("3");
        user3.setFirstName("first3");
        user3.setLastName("last3");
        user3.setProfilePictureUrl("picture3");

        // create locations
        Location location1 = new Location();
        location1.setId("1");
        location1.setLatitude(1);
        location1.setLongitude(1);
        location1.setOrder(1);
        location1.setUser(user1);
        location1.setType(Location.LocationType.SOURCE);

        Location location2 = new Location();
        location2.setId("2");
        location2.setLatitude(2);
        location2.setLongitude(2);
        location2.setOrder(1);
        location2.setUser(user2);
        location2.setType(Location.LocationType.SOURCE);

        Location location3 = new Location();
        location3.setId("3");
        location3.setLatitude(3);
        location3.setLongitude(3);
        location3.setOrder(2);
        location3.setUser(user3);
        location3.setType(Location.LocationType.SOURCE);


        Location location4 = new Location();
        location4.setId("4");
        location4.setLatitude(4);
        location4.setLongitude(4);
        location4.setOrder(2);
        location4.setUser(user1);
        location4.setType(Location.LocationType.SOURCE);


        // create rides
        Ride ride1 = new Ride();
        ride1.setId("1");
        ride1.setDriver(user1);
        ride1.setDescription("desc1");
        ride1.setNotes("notes1");
        ride1.setTime(TimeUtils.getDatabaseTime("11 06 2016 10 30 00"));
        ride1.setLocations(Arrays.asList(location1, location2));
        RideDetails rideDetails1 = new RideDetails();
        ride1.setRideDetails(rideDetails1);

        Ride ride2 = new Ride();
        ride2.setId("2");
        ride2.setDriver(user2);
        ride2.setDescription("desc2");
        ride2.setNotes("notes2");
        ride2.setTime(TimeUtils.getDatabaseTime("11 06 2016 10 30 00"));
        ride2.setLocations(Arrays.asList(location3, location4));
        RideDetails rideDetails2 = new RideDetails();
        ride2.setRideDetails(rideDetails2);

        // create database
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());


    }
}