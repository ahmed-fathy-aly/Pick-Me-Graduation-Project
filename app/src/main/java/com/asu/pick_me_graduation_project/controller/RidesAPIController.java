package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.CreateAnnouncementCallback;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetAnnouncementsCallback;
import com.asu.pick_me_graduation_project.callback.GetRideCallback;
import com.asu.pick_me_graduation_project.callback.GetRideJoinRequestsCallback;
import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.JoinRideRequest;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.RideAnnouncement;
import com.asu.pick_me_graduation_project.model.SearchRideParams;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                            // sort by date
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

    public void searchRides(String token, SearchRideParams searchRideParams, final GetRidesCallback callback)
    {

        String url = Constants.HOST +
                "/ride/search_for_ride?" +
                "time=" + TimeUtils.convertToBackendTime(searchRideParams.getTime()) +
                "&latitudeSrc=" + searchRideParams.getSource().getLatitude() +
                "&longitudeSrc=" + searchRideParams.getSource().getLongitude() +
                "&latitudeDest=" + searchRideParams.getDestination().getLatitude() +
                "&longitudeDest=" + searchRideParams.getDestination().getLongitude();
        Log.e("Game", "search url = " + url);
        if (searchRideParams.getFilteredCommunities() != null)
            for (Community community : searchRideParams.getFilteredCommunities())
                url += "&communities=" + community.getId();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        Log.e("Game", "seraching ");
        Fuel.get(url)
                .header(headers)
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response responseFuel, String result)
                    {
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
                            callback.success(rides);

                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });


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
            JSONArray communitiesIds = new JSONArray();
            if (ride.getRideDetails().getFilteredCommunities() != null)
                for (Community community : ride.getRideDetails().getFilteredCommunities())
                    communitiesIds.put(Integer.parseInt(community.getId()));
            json.put("communities", communitiesIds);

            json.put("numberOfFreeSeats", ride.getRideDetails().getNumberOfFreeSeats());
            json.put("ladiesOnly", ride.getRideDetails().isLadiesOnly());
            json.put("noSmoking", ride.getRideDetails().isNoSmoking());
            json.put("disabledWelcomed", ride.getRideDetails().isDisabledWelcomed());
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
                            Log.e("Game", "error in ride details " + e2.getMessage());
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    public void requestToJoinRide(String token, SearchRideParams searchRideParams, String rideId, String message, final GenericSuccessCallback callback)
    {
        // form the request
        String url = Constants.HOST + "ride/request_to_join_ride";

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("rideId", rideId);
            jsonObject.put("message", message);
            jsonObject.put("latitudeSrc", searchRideParams.getSource().getLatitude());
            jsonObject.put("longitudeSrc", searchRideParams.getSource().getLongitude());
            jsonObject.put("latitudeDest", searchRideParams.getDestination().getLatitude());
            jsonObject.put("longitudeDest", searchRideParams.getDestination().getLongitude());
            JSONArray communitiesIds = new JSONArray();
            if (searchRideParams.getFilteredCommunities() != null)
                for (Community community : searchRideParams.getFilteredCommunities())
                    communitiesIds.put(Integer.parseInt(community.getId()));
            jsonObject.put("communities", communitiesIds);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String body = jsonObject.toString();

        // make the post
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
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

                            // success
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
                        callback.fail(fuelError.getMessage());
                    }
                });


    }

    public void getRideJoinRequest(String token, String rideId, final GetRideJoinRequestsCallback callback)
    {
        String url = Constants.HOST + "ride/get_ride_join_requests?rideId=" + rideId;

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);


        Fuel.get(url)
                .header(headers)
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response responseFuel, String result)
                    {
                        try
                        {
                            Log.e("Game", "get join ride requests = " + result);

                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse requests
                            JSONArray requestsJson = response.getJSONArray("joinRideRequests");
                            List<JoinRideRequest> requests = new ArrayList<JoinRideRequest>();
                            for (int i = 0; i < requestsJson.length(); i++)
                                requests.add(JoinRideRequest.fromJson(requestsJson.getJSONObject(i)));
                            callback.success(requests);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });


    }

    public void respondToJoinRideRequest(String token, String rideId, String userId, boolean accept, final GenericSuccessCallback callback)
    {
        // form the request
        String url = Constants.HOST + "ride/answer_join_ride_request";

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("rideId", rideId);
            jsonObject.put("userId", userId);
            jsonObject.put("isAccepted", accept);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String body = jsonObject.toString();
        Log.e("Game", "respond to join ride body = " + body);

        // make the post
        Fuel.put(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
                        Log.e("Game", "respond to join ride result = " + s);
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

                            // success
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
                        callback.fail(fuelError.getMessage());
                    }
                });


    }

    /**
     * makes a post request to make an announcement
     */
    public void postAnnouncement(String token, String rideId, String content, final CreateAnnouncementCallback callback)
    {
        // form the request
        String url = Constants.HOST + "ride/post_ride_announcement";

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("rideId", rideId);
            jsonObject.put("content", content);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        String body = jsonObject.toString();

        // make the post
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
                        Log.e("Game", "request " + request.toString());
                        Log.e("Game", "response" + r.toString());

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

                            // parse the announcement
                            JSONObject announcementJson = response.getJSONObject("newAnnouncement");
                            RideAnnouncement announcement = RideAnnouncement.fromJson(announcementJson);
                            callback.success(announcement);
                        } catch (Exception e2)
                        {
                            Log.e("Game", "post announcement error " + e2.getMessage());
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response resp, FuelError fuelError)
                    {
                        callback.fail(fuelError.getMessage());
                    }
                });
    }

    /**
     * makes a get request to download the ride announcements sorted newest first
     */
    public void getRideAnnouncements(String token, String rideId, final GetAnnouncementsCallback callback)
    {
        String url = Constants.HOST + "ride/get_ride_announcements?rideId=" + rideId;

        Ion.with(context)
                .load("GET", url)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {

                        // check errors
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }

                        Log.e("Game", "get announcements = " + result);

                        //parse the  response
                        try
                        {

                            // check the status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("feedback");
                                callback.fail(message);
                                return;
                            }

                            // parse the announcements
                            JSONArray announcementsJson = response.getJSONArray("announcements");
                            List<RideAnnouncement> announcmentList = new ArrayList<RideAnnouncement>();
                            for (int i = 0; i < announcementsJson.length(); i++)
                                announcmentList.add(RideAnnouncement.fromJson(announcementsJson.getJSONObject(i)));

                            // sort by date
                            Collections.sort(announcmentList, new Comparator<RideAnnouncement>()
                            {
                                @Override
                                public int compare(RideAnnouncement lhs, RideAnnouncement rhs)
                                {
                                    return rhs.getDate().compareTo(lhs.getDate());
                                }
                            });

                            // invoke callback
                            callback.success(announcmentList);
                        } catch (Exception e2)
                        {
                            Log.e("Game", "error parsing announcements " + e2.getMessage());
                            callback.fail(e2.getMessage());
                        }

                    }
                });

    }
}