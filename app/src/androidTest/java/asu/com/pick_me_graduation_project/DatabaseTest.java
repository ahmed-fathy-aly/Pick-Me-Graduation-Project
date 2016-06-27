package asu.com.pick_me_graduation_project;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.asu.pick_me_graduation_project.database.DatabaseHelper;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.RideDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DatabaseTest extends ApplicationTestCase<Application>
{
    public DatabaseTest()
    {
        super(Application.class);
    }


    public void testMyRides1()
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

        // clear database
        databaseHelper.clearTables();

        // insert rides
        databaseHelper.insertRides(Arrays.asList(ride1, ride2));

        // get 1 ride
        Ride ride = databaseHelper.getRide(1);
        assertSameRide(ride, ride1);

        // get all rides
        List<Ride> rides = databaseHelper.getAllRides();
        List<Ride> expectedRides = Arrays.asList(ride1, ride2);
        assertEquals(rides.size(), expectedRides.size());
        for (int i = 0; i < rides.size(); i++)
            assertSameRide(rides.get(i), expectedRides.get(i));

        // close the database
        databaseHelper.close();

    }

    public void testMyRides2()
    {
        // user data from the server as json strings
        String jsonString =
                " [\n" +
                        "    {\n" +
                        "      \"id\": 46,\n" +
                        "      \"postUser\": {\n" +
                        "        \"id\": 7,\n" +
                        "        \"firstName\": \"Snow\",\n" +
                        "        \"lastName\": \"White\",\n" +
                        "        \"profilePicture\": \"\"\n" +
                        "      },\n" +
                        "      \"notes\": \"bla bla blaa at bla bla\",\n" +
                        "      \"description\": \"To A new brave world\",\n" +
                        "      \"time\": \"2016-11-30T12:00:00\",\n" +
                        "      \"ac\": true,\n" +
                        "      \"ladiesOnly\": false,\n" +
                        "      \"noSmoking\": true,\n" +
                        "      \"locations\": [\n" +
                        "        {\n" +
                        "          \"id\": 74,\n" +
                        "          \"user\": {\n" +
                        "            \"id\": 7,\n" +
                        "            \"firstName\": \"Snow\",\n" +
                        "            \"lastName\": \"White\",\n" +
                        "            \"profilePicture\": \"\"\n" +
                        "          },\n" +
                        "          \"longitude\": 2.355,\n" +
                        "          \"latitude\": 48.87308,\n" +
                        "          \"type\": false,\n" +
                        "          \"order\": 1\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"id\": 75,\n" +
                        "          \"user\": {\n" +
                        "            \"id\": 7,\n" +
                        "            \"firstName\": \"Snow\",\n" +
                        "            \"lastName\": \"White\",\n" +
                        "            \"profilePicture\": \"\"\n" +
                        "          },\n" +
                        "          \"longitude\": 2.3547,\n" +
                        "          \"latitude\": 48.8776,\n" +
                        "          \"type\": true,\n" +
                        "          \"order\": 2\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 47,\n" +
                        "      \"postUser\": {\n" +
                        "        \"id\": 7,\n" +
                        "        \"firstName\": \"Snow\",\n" +
                        "        \"lastName\": \"White\",\n" +
                        "        \"profilePicture\": \"\"\n" +
                        "      },\n" +
                        "      \"notes\": \"bla bla blaa at bla bla\",\n" +
                        "      \"description\": \"To A new brave world\",\n" +
                        "      \"time\": \"2016-11-30T12:00:00\",\n" +
                        "      \"ac\": true,\n" +
                        "      \"ladiesOnly\": false,\n" +
                        "      \"noSmoking\": true,\n" +
                        "      \"locations\": [\n" +
                        "        {\n" +
                        "          \"id\": 76,\n" +
                        "          \"user\": {\n" +
                        "            \"id\": 7,\n" +
                        "            \"firstName\": \"Snow\",\n" +
                        "            \"lastName\": \"White\",\n" +
                        "            \"profilePicture\": \"\"\n" +
                        "          },\n" +
                        "          \"longitude\": 2.355,\n" +
                        "          \"latitude\": 48.87308,\n" +
                        "          \"type\": false,\n" +
                        "          \"order\": 1\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"id\": 77,\n" +
                        "          \"user\": {\n" +
                        "            \"id\": 7,\n" +
                        "            \"firstName\": \"Snow\",\n" +
                        "            \"lastName\": \"White\",\n" +
                        "            \"profilePicture\": \"\"\n" +
                        "          },\n" +
                        "          \"longitude\": 2.3547,\n" +
                        "          \"latitude\": 48.8776,\n" +
                        "          \"type\": true,\n" +
                        "          \"order\": 2\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 48,\n" +
                        "      \"postUser\": {\n" +
                        "        \"id\": 7,\n" +
                        "        \"firstName\": \"Snow\",\n" +
                        "        \"lastName\": \"White\",\n" +
                        "        \"profilePicture\": \"\"\n" +
                        "      },\n" +
                        "      \"notes\": \"bla bla blaa at bla bla\",\n" +
                        "      \"description\": \"To A new brave world\",\n" +
                        "      \"time\": \"2016-11-30T12:00:00\",\n" +
                        "      \"ac\": true,\n" +
                        "      \"ladiesOnly\": false,\n" +
                        "      \"noSmoking\": true,\n" +
                        "      \"locations\": [\n" +
                        "        {\n" +
                        "          \"id\": 78,\n" +
                        "          \"user\": {\n" +
                        "            \"id\": 7,\n" +
                        "            \"firstName\": \"Snow\",\n" +
                        "            \"lastName\": \"White\",\n" +
                        "            \"profilePicture\": \"\"\n" +
                        "          },\n" +
                        "          \"longitude\": 2.355,\n" +
                        "          \"latitude\": 48.87308,\n" +
                        "          \"type\": false,\n" +
                        "          \"order\": 1\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"id\": 79,\n" +
                        "          \"user\": {\n" +
                        "            \"id\": 7,\n" +
                        "            \"firstName\": \"Snow\",\n" +
                        "            \"lastName\": \"White\",\n" +
                        "            \"profilePicture\": \"\"\n" +
                        "          },\n" +
                        "          \"longitude\": 2.3547,\n" +
                        "          \"latitude\": 48.8776,\n" +
                        "          \"type\": true,\n" +
                        "          \"order\": 2\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }" +
                        "]";
        try
        {
            // parse the rides
            JSONArray jsonArray = new JSONArray(jsonString);
            List<Ride> expectedRides = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++)
                expectedRides.add(Ride.fromJson(jsonArray.getJSONObject(i)));

            // create database
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

            // clear database
            databaseHelper.clearTables();

            // insert rides
            databaseHelper.insertRides(expectedRides);

            // read rides
            List<Ride> resultRides = databaseHelper.getAllRides();
            assertEquals(expectedRides.size(), resultRides.size());
            for (int i = 0 ; i < expectedRides.size(); i++)
                assertSameRide(expectedRides.get(i), resultRides.get(i));

            // close the database
            databaseHelper.close();

        } catch (JSONException e)
        {

            fail("failed to parse the data");
        }


    }
    private void assertSameRide(Ride ride1, Ride ride2)
    {
        // same details
        assertEquals(ride1.getId(), ride2.getId());
        assertEquals(ride1.getDescription(), ride2.getDescription());
        assertEquals(ride1.getNotes(), ride2.getNotes());
        assertEquals(ride1.getRideDetails().getNumberOfFreeSeats(), ride2.getRideDetails().getNumberOfFreeSeats());
        assertEquals(ride1.getRideDetails().isNoSmoking(), ride2.getRideDetails().isNoSmoking());
        assertEquals(ride1.getRideDetails().isLadiesOnly(), ride2.getRideDetails().isLadiesOnly());

        // same driver
        assertSameUser(ride1.getDriver(), ride2.getDriver());

        // same locations
        assertEquals(ride1.getLocations().size(), ride2.getLocations().size());
        for (int i = 0;i< ride1.getLocations().size(); i++)
            assertSameLocation(ride1.getLocations().get(i), ride1.getLocations().get(i));


    }

    private void assertSameLocation(Location location1, Location location2)
    {
        assertEquals(location1.getId(), location2.getId());
        assertEquals(location1.getLatitude(), location2.getLatitude());
        assertEquals(location1.getLongitude(), location2.getLongitude());
        assertEquals(location1.getOrder(), location2.getOrder());
        assertEquals(location1.getType(), location2.getType());
        assertSameUser(location1.getUser(), location2.getUser());
    }

    private void assertSameUser(User user1, User user2)
    {
        assertEquals(user1.getUserId(), user2.getUserId());
        assertEquals(user1.getFirstName(), user2.getFirstName());
        assertEquals(user1.getLastName(), user2.getLastName());
        assertEquals(user1.getProfilePictureUrl(), user2.getProfilePictureUrl());
    }
}