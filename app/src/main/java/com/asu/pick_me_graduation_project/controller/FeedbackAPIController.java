package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetFeedbackFormCallback;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.User;
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
public class FeedbackAPIController {
    /* fields */
    Context context;

    /* constructor */
    public FeedbackAPIController(Context context) {
        this.context = context;
    }

   ;
    /* methods */

    /**
     * gets the list of users who are on the ride and which one is the rider
     */
    public void getFeedbackForm(String token, String rideId,final GetFeedbackFormCallback callback) {
        // TODO invoke callback on mock data
       /* User user1 = new User();
        user1.setUserId("1");
        user1.setFirstName("Ahmed");
        user1.setLastName("Ali");
        user1.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        User user2 = new User();
        user2.setUserId("2");
        user2.setFirstName("Mohamed");
        user2.setLastName("Hussien");
        user2.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        User user3 = new User();
        user3.setUserId("3");
        user3.setFirstName("Nadeen");
        user3.setLastName("Bora3i");
        user3.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        Ride ride = new Ride();
        ride.setId(rideId);
        User user4;
        user4 = ride.getRider();
        user4.setFirstName("Abo Bakr");
        user4.setLastName("Walid ");
        user4.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");
        String driverid = user4.getUserId();
        List<User> passengers = new ArrayList<>();
        passengers.add(user1);
        passengers.add(user2);
        passengers.add(user3);
        passengers.add(user4);

        callback.success(passengers, driverid);*/

        String url="http://pick-me.azurewebsites.net/api/ride/get_feedback_from?rideId=59";
        Ion.with(context)
                .load("GET",url)
                .addHeader("Authorization","Bearer " + token)
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null)
                {
                    Log.e("Game", "error " + e.getMessage());
                    callback.fail(e.getMessage());
                    return;
                }
                //parse el response
                Log.e("Game", "get feedbackform result = " + result);
                try {
                    JSONObject response = new JSONObject(result);
                    int status = response.getInt("status");
                    if (status == 0)
                    {
                        String message = response.getString("feedback");
                        callback.fail(message);
                        return;
                    }
                    //parse
                    JSONArray passengers = response.getJSONArray("passengerId");
                    List<User> passengers2=new ArrayList<User>();
                    for (int i = 0; i < passengers.length(); i++)
                    {
                        JSONObject passengerJson = passengers.getJSONObject(i);
                        User passenger = User.fromJson(passengerJson);
                        passengers2.add(passenger);

                    }
                    JSONObject driverJson=response.getJSONObject("driverId");
                    User driver=User.fromJson(driverJson);
                    String driverId= driver.getUserId();
                    callback.success(passengers2,driverId);
                } catch (Exception e2 ) {
                   callback.fail(e2.getMessage());
                }

            }
        });



    }

    /**
     * posts feedbacks for each user in the ride
     * the feedback object will contain specificDriverFeedback only for the driver
     */
    public void postFeedback(String token, String userId, String rideId, List<Feedback> feedbackList, Feedback.DriverSpecificFeedback driverfeedback,Feedback.RouteFeedback roadFeedback,final GenericSuccessCallback callback) throws JSONException {
        // TODO invoke callback on mock data
        String url = "http://pick-me.azurewebsites.net/api/ride/post_feedback";
        JSONObject json = new JSONObject();
        JSONObject driverJason=new JSONObject();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");
        Ride ride = new Ride();
        ride.setId(rideId);

       // driverJason.put("fromUserId", userId);
        driverJason.put("toUserId", ride.getRider().getUserId());
       // driverJason.put("rideId", rideId);
        driverJason.put("sameAC", driverfeedback.isSameAc());
        driverJason.put("sameModel", driverfeedback.isSameModel());
        driverJason.put("samePlateNumber", driverfeedback.isSamePlate());
        driverJason.put("driving", driverfeedback.getDriving());
        json.put("driverFeedback", driverJason);

        JSONObject roadJason= new JSONObject();
        roadJason.put("route",roadFeedback.getRouteSmoothness());
        roadJason.put("traffic",roadFeedback.getTrafficGoodness());
        json.put("roadFeedback",roadJason);

        JSONArray array = new JSONArray();
        for (int i = 0; i < feedbackList.size(); i++) {

            JSONObject json2 = new JSONObject();
            Feedback feedback = feedbackList.get(i);
            json2.put("fromUserId", userId);
            try {
                json2.put("toUserId", feedback.getUserId());
               // json2.put("rideId", rideId);
                json2.put("Punctuality", feedback.getPunctuality());
                json2.put("attitude", feedback.getAttitude());
                json2.put("comment",feedback.getComment());

                array.put(i, json2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        json.put("userFeedback",array);
        String body = json.toString();
        Log.e("Game", "body " + body);
        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>() {
                    @Override
                    public void success(Request request, Response response, String s) {
                        Log.e("Game", "success " + s);
                        JSONObject responses=new JSONObject();
                        try {
                            int status=responses.getInt("status");
                            if (status == 0)
                            {
                                String message = responses.getString("message");
                                callback.fail(message);
                                return;
                            }
                            callback.success();
                        } catch (JSONException e2) {
                           // e2.printStackTrace();
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response resp, FuelError fuelError){
                        Log.e("Game", "error " + fuelError.getMessage());
                        Log.e("Game", "request " + request.toString());
                        Log.e("Game", "response" + resp.toString());

                        callback.fail(fuelError.getMessage());

                    }
                });

    }
}
