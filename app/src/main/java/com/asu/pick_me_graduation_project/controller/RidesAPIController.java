package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetRideCallback;
import com.asu.pick_me_graduation_project.callback.GetRideJoinRequestsCallback;
import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.JoinRideRequest;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.SearchRideParams;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void getMyRides(String token, String userId, final GetRidesCallback callback)
    {
        String url = Constants.HOST + "/ride/get_my_rides?postUserId=" + userId;
        Ion.with(context)
                .load("GET", url)
                .addHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check failed
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }

                        // parse the response
                        try
                        {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse rides
                            JSONArray ridesJson = response.getJSONArray("rides");
                            List<Ride> rides = new ArrayList<Ride>();
                            for (int i = 0; i < ridesJson.length(); i++)
                                rides.add(Ride.fromJson(ridesJson.getJSONObject(i)));

                            // sort by data
                            Collections.sort(rides, new Comparator<Ride>()
                            {
                                @Override
                                public int compare(Ride lhs, Ride rhs)
                                {
                                    return rhs.getTime().compareTo(lhs.getTime());
                                }
                            });
                            callback.success(rides);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });


    }

    public void searchRides(String token, final GetRidesCallback callback)
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


                    ride.setCanRequestToJoin(true);
                    rideList.add(ride);
                }
                callback.success(rideList);
            }
        }, 2000);

    }

    /**
     * posts a new ride
     */
    public void postRide(String token, Ride ride, final GenericSuccessCallback callback)
    {
        String url = Constants.HOST + "/ride/post_ride";

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");


        JSONObject json = new JSONObject();
        try
        {
            json.put("notes", ride.getNotes());
            json.put("description", ride.getDescription());
            json.put("time", TimeUtils.convertToBackendTime(ride.getTime()));

            JSONObject source = new JSONObject();
            source.put("latitude", ride.getLocations().get(0).getLatitude());
            source.put("longitude", ride.getLocations().get(0).getLongitude());
            json.put("source", source);

            JSONObject destination = new JSONObject();
            destination.put("latitude", ride.getLocations().get(1).getLatitude());
            destination.put("longitude", ride.getLocations().get(1).getLongitude());
            json.put("destination", destination);

            JSONArray communities = new JSONArray();
            if (ride.getRideDetails().getFilteredCommunities() != null)
                for (Community community : ride.getRideDetails().getFilteredCommunities())
                    communities.put(community.getId());
            json.put("communities", communities);

            json.put("numberOfFreeSeats", ride.getRideDetails().getNumberOfFreeSeats());
            json.put("ladiesOnly", ride.getRideDetails().isLadiesOnly());
            json.put("noSmoking", ride.getRideDetails().isNoSmoking());
            json.put("ac", ride.getRideDetails().getCarDetails().isConditioned());
            json.put("carModel", ride.getRideDetails().getCarDetails().getModel());
            json.put("caryear", ride.getRideDetails().getCarDetails().getYear());
            json.put("carPlateNumber", ride.getRideDetails().getCarDetails().getPlateNumber());

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String body = json.toString();
        Log.e("Game", "body " + body);

        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
                        Log.e("Game", "success " + s);
                        try
                        {
                            // check status
                            JSONObject response = new JSONObject(s);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }


                            // invoke callback
                            callback.success();
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response resp, FuelError fuelError)
                    {
                        Log.e("Game", "error " + fuelError.getMessage());
                        Log.e("Game", "request " + request.toString());
                        Log.e("Game", "response" + resp.toString());

                        callback.fail(fuelError.getMessage());
                    }
                });
    }

    /**
     * downloads the details of a ride
     */
    public void getRideDetails(String token, String rideId, final GetRideCallback callback)
    {
        String url = Constants.HOST + "ride/get_ride_details?rideId=" + rideId;
        Ion.with(context)
                .load("GET", url)
                .setHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check failed
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "get ride result = " + result);

                        // parse the response
                        try
                        {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse ride
                            Ride ride = Ride.fromJson(response.getJSONObject("rideDetails"));
                            callback.success(ride);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    public void requestToJoinRide(String tokken, SearchRideParams searchRideParams, String id, final GenericSuccessCallback callback)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                callback.success();
            }
        }, 2000);

    }

    public void getRideJoinRequest(String tokken, String rideId, final GetRideJoinRequestsCallback callback)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO now it's dummy data
                List<JoinRideRequest> requests = new ArrayList<>();
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

                    JoinRideRequest request = new JoinRideRequest();
                    request.setUser(user);
                    request.setMessage("Please take me " + i);
                    request.setLocationList(Arrays.asList(l1, l2));
                    requests.add(request);

                }

                callback.success(requests);
            }
        }, 2000);
    }
}