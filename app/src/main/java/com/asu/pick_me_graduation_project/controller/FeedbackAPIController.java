package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetFeedbackCallback;
import com.asu.pick_me_graduation_project.callback.GetFeedbackFormCallback;
import com.asu.pick_me_graduation_project.model.DrivingFeedback;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.RoadFeedback;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    ;
    /* methods */

    /**
     * gets the list of users who are on the ride and which one is the rider
     */
    public void getFeedbackForm(String token, String rideId, final GetFeedbackFormCallback callback)
    {
        String url = Constants.HOST + "ride/get_feedback_from?rideId=" + rideId;
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

                        Log.e("Game", "get form = " + result);

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

                            // parse the passengers
                            JSONObject feedbackJson = response.getJSONObject("feedback");
                            JSONArray passengersJson = feedbackJson.getJSONArray("passengerId");
                            List<User> passengers = new ArrayList<>();
                            for (int i = 0; i < passengersJson.length(); i++)
                            {
                                JSONObject passengerJson = passengersJson.getJSONObject(i);
                                User passenger = User.fromJson(passengerJson);
                                passengers.add(passenger);
                            }

                            // parse the driver
                            JSONObject driverJson = feedbackJson.getJSONObject("driverId");
                            User driver = User.fromJson(driverJson);

                            // invoke callback
                            callback.success(passengers, driver);
                        } catch (Exception e2)
                        {
                            callback.fail(e2.getMessage());
                        }

                    }
                });


    }

    /**
     * posts feedbacks for each user in the ride
     * the feedback object will contain specificDriverFeedback only for the driver
     */
    public void postFeedback(String token,
                             String userId, String rideId,
                             List<Feedback> feedbackList, DrivingFeedback driverfeedback, RoadFeedback roadFeedback,
                             final GenericSuccessCallback callback)
    {
        // request headers
        String url = Constants.HOST + "ride/post_feedback";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        // request body
        String body = "";
        try
        {

            JSONObject json = new JSONObject();

            // ride id
            json.put("rideId", rideId);

            // driving feedback
            if (driverfeedback != null)
            {
                JSONObject divingFeedbackJson = new JSONObject();
                divingFeedbackJson.put("toUserId", driverfeedback.getUserId());
                divingFeedbackJson.put("sameAC", driverfeedback.isSameAc());
                divingFeedbackJson.put("sameModel", driverfeedback.isSameModel());
                divingFeedbackJson.put("samePlateNumber", driverfeedback.isSamePlate());
                divingFeedbackJson.put("driving", driverfeedback.getDriving());
                json.put("driverFeedback", divingFeedbackJson);
            }

            // road feedback
            JSONObject roadFeedbackJson = new JSONObject();
            roadFeedbackJson.put("route", roadFeedback.getRouteSmoothness());
            roadFeedbackJson.put("traffic", roadFeedback.getTrafficGoodness());
            json.put("roadFeedback", roadFeedbackJson);

            // user feedback
            JSONArray userFeedbackJsonArray = new JSONArray();
            for (int i = 0; i < feedbackList.size(); i++)
            {
                JSONObject userFeedbackJson = new JSONObject();
                Feedback feedback = feedbackList.get(i);

                userFeedbackJson.put("toUserId", feedback.getUserId());
                userFeedbackJson.put("Punctuality", feedback.getPunctuality());
                userFeedbackJson.put("attitude", feedback.getAttitude());
                userFeedbackJson.put("comment", feedback.getComment());

                userFeedbackJsonArray.put(i, userFeedbackJson);
            }
            json.put("userFeedback", userFeedbackJsonArray);
            body = json.toString();

        } catch (JSONException e)
        {
            callback.fail(e.getMessage());
        }


        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response response, String s)
                    {
                        try
                        {
                            // check status
                            JSONObject responses = new JSONObject(s);
                            int status = responses.getInt("status");
                            if (status == 0)
                            {
                                String message = responses.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // invoke callback
                            callback.success();
                        } catch (JSONException e2)
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
     * downloads the feedback for a specific user
     */
    public void getUserFeedback(String token, String userId, final GetFeedbackCallback callback)
    {
        String url = Constants.HOST + "ride/get_feedback?userId=" + userId;

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

                        Log.e("Game", "get feedback= " + result);

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

                            // parse the feedback list
                            JSONObject feedbackJson = response.getJSONObject("feedback");
                            HashMap<String , Feedback> userFeedbackMap = new HashMap<String, Feedback>();
                            List<Feedback> feedbackList = new ArrayList<Feedback>();

                            // user feedback
                            JSONArray userFeedbackJson = feedbackJson.getJSONArray("userFeedback");
                            for (int i = 0; i < userFeedbackJson.length(); i++)
                            {
                                Feedback feedback = Feedback.fromJson(userFeedbackJson.getJSONObject(i));
                                String key = feedback.getRideId() + "." + feedback.getFromUser().getUserId();
                                userFeedbackMap.put(key,feedback);
                                feedbackList.add(feedback);
                            }
                            
                            // driving feedback
                            JSONArray drivingFeedbackJson = feedbackJson.getJSONArray("driverFeedback");
                            for (int i = 0; i < drivingFeedbackJson.length(); i++)
                            {
                                DrivingFeedback drivingFeedback = DrivingFeedback.fromJson(drivingFeedbackJson.getJSONObject(i));
                                String key = drivingFeedback.getRideId() + "." + drivingFeedback.getFromUser().getUserId();
                                if (userFeedbackMap.containsKey(key))
                                    userFeedbackMap.get(key).setDrivingFeedback(drivingFeedback);
                            }

                            callback.success(feedbackList);
                        } catch (Exception e2)
                        {
                            Log.e("Game", "error parsing feedback response " + e2.getMessage());
                            callback.fail(e2.getMessage());
                        }

                    }
                });
    }
}
