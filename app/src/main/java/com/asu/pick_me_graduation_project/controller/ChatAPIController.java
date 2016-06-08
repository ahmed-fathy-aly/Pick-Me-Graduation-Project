package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.callback.SendMessageCallback;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ahmed on 3/2/2016.
 */
public class ChatAPIController
{
    Context context;

    public ChatAPIController(Context context)
    {
        this.context = context;
    }

    /**
     * gets my recent chats
     */
    public void getRecentChats(String token, String userId, final GetMessagesCallback callback)
    {
        // TODO - get them from back end instead of mock data later
        String url="http://pickmeasu.azurewebsites.net/api/Message/get_recent_chats"+"?count=-1";
        Ion.with(context)
                .load("GET",url)
                .addHeader("Authorization", "Bearer " + token)
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                //check failed
                if (e != null) {
                    Log.e("Game", "error " + e.getMessage());
                    callback.fail(e.getMessage());
                    return;
                }
                //parse el response
                Log.e("Game", "get recent chats result = " + result);
                try {


                    JSONObject response = new JSONObject(result);
                    int status = response.getInt("status");
                    if (status == 0) {
                        String message = response.getString("message");
                        callback.fail(message);
                        return;
                    }
                    //parse
                    JSONArray chatsJson = response.getJSONArray("chats");
                    List<ChatMessage> messages2 = new ArrayList<ChatMessage>();
                    for (int i = 0; i < chatsJson.length(); i++) {
                        JSONObject chatJson = chatsJson.getJSONObject(i);
                        ChatMessage chat = ChatMessage.fromJson(chatJson);
                        messages2.add(chat);

                    }
                    Collections.sort(messages2, new Comparator<ChatMessage>() {
                        public int compare(ChatMessage M1, ChatMessage M2) {
                            return M2.getDate().compareTo(M1.getDate());
                        }
                    });
                    callback.success(messages2);
                } catch (Exception e2) {
                    callback.fail(e2.getMessage());
                    return;
                }



            }
        });

        // make mock data
       /* List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            User user = new User();
            user.setUserId(i + "");
            user.setFirstName("hamada " + i);
            user.setLastName("mido");
            user.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");

            ChatMessage message = new ChatMessage();
            message.setContent("content " + i);
            message.setDate(Calendar.getInstance());
            message.getDate().add(Calendar.DAY_OF_MONTH, -1);
            message.setFrom(user);
            message.setTo(user);

            messages.add(message);
            if (i >= 3 && i <= 7)
            {
                message.getDate().add(Calendar.DAY_OF_MONTH, -2);
                //user.setProfilePictureUrl("http://img02.taobaocdn.com/bao/uploaded/i2/T17gyyXXxiXXbTvUg3_051422.jpg");
            }
            if (i >= 8)
            {
                message.getDate().add(Calendar.DAY_OF_MONTH, -3);
            }
        }
        Collections.sort(messages, new Comparator<ChatMessage>()
        {
            public int compare(ChatMessage M1, ChatMessage M2)
            {
                return M2.getDate().compareTo(M1.getDate());
            }
        });

        callback.success(messages);*/
    }

    public void getChatMessages(String tokken, String myUserId, String otherUserId, final GetMessagesCallback callback)
    {
        //from backend
        String url="http://pickmeasu.azurewebsites.net/api/Message/get_a_conversation"+"?count=2"+"&userId=44";
        Ion.with(context)
                .load("GET",url).
                addHeader("Authorization","Bearer "+tokken)
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                //check el fail
                if (e != null) {
                    Log.e("Game", "error " + e.getMessage());
                    callback.fail(e.getMessage());
                    return;
                }
                //parse el response
                try {


                    Log.e("Game", "get chat result = " + result);
                    JSONObject response = new JSONObject(result);
                    int status = response.getInt("status");
                    if (status == 0) {
                        String message = response.getString("message");
                        callback.fail(message);
                        return;
                    }
                    JSONArray messegesJson = response.getJSONArray("messages");
                    List<ChatMessage> messages3 = new ArrayList<ChatMessage>();
                    for (int i = 0; i < messegesJson.length(); i++) {
                        JSONObject messageJson = messegesJson.getJSONObject(i);
                        ChatMessage message = ChatMessage.fromJson(messageJson);
                        messages3.add(message);
                    }
                    //sort
                    Collections.sort(messages3, new Comparator<ChatMessage>() {
                        public int compare(ChatMessage M1, ChatMessage M2) {
                            return M1.getDate().compareTo(M2.getDate());
                        }
                    });
                    callback.success(messages3);

                } catch (Exception e2) {
                    callback.fail(e2.getMessage());
                    return;
                }


            }

        });


        // make mock data
       /* List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            User from = new User();
            from.setUserId(i + "");
            from.setFirstName("hamada  " + i);
            from.setLastName("mido ");
            from.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");


            User to = new User();
            to.setUserId(i + "");
            to.setFirstName("name " + i);
            to.setLastName("afandem");
            to.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");

            ChatMessage message = new ChatMessage();
            message.setContent("hi " + i);
            message.setDate(Calendar.getInstance());
            message.getDate().add(Calendar.DAY_OF_MONTH, -1);
            message.setFrom(from);
            message.setTo(to);
            if (i % 2 == 0)
                to.setUserId(new AuthenticationAPIController(context).getCurrentUser().getUserId());
            else
                from.setUserId(new AuthenticationAPIController(context).getCurrentUser().getUserId());
            messages.add(message);
            if (i >= 3 && i <= 7)
            {
                message.getDate().add(Calendar.DAY_OF_MONTH, -2);
                //from.setProfilePictureUrl("http://img02.taobaocdn.com/bao/uploaded/i2/T17gyyXXxiXXbTvUg3_051422.jpg");
            }
            if (i >= 8)
            {
                message.getDate().add(Calendar.DAY_OF_MONTH, -3);
            }
        }
        Collections.sort(messages, new Comparator<ChatMessage>()
        {
            public int compare(ChatMessage M1, ChatMessage M2)
            {
                return M1.getDate().compareTo(M2.getDate());
            }
        });

        callback.success(messages);*/
    }
    public void sendMessage(String content,String userId,String token,final SendMessageCallback callback)
    {
        String url="http://pickmeasu.azurewebsites.net/api/Message/send_message";
        JsonObject json = new JsonObject();
        json.addProperty("content", content);
        json.addProperty("userId", userId);
        String body = json.toString();
        Log.e("Game", "body = " + body);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization","Bearer"+token);
        headers.put("Content-Type", "application/json");
        Fuel.post(url).header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
                        Log.e("Game", "success " + s);
                        try
                        {// check status
                            JSONObject response = new JSONObject(s);
                            int status = response.getInt("status");
                            if (status == 0)
                            {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }
                            JSONObject contentJson = response.getJSONObject("content");
                            ChatMessage message = ChatMessage.fromJson(contentJson);
                            callback.success(message);
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

        // json.addProperty("Authorization", "Bearer"+token);



       /* Ion.with(context)
                .load(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization","Bearer"+token)
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                                 @Override
                                 public void onCompleted(Exception e, String result) {
                                     Log.e("Game", "error" + (e != null));
                                     if (e != null) {
                                         Log.e("Game", "error Send Message " + e.getMessage());
                                         callback.fail(e.getMessage());
                                         return;

                                     }
                                     Log.e("Game", "send message result = " + result);
                                     try {


                                         JSONObject response = new JSONObject(result);
                                         int status = response.getInt("status");
                                         if (status == 0) {
                                             String message = response.getString("message");
                                             callback.fail(message);
                                             return;
                                         }
                                         JSONObject contentJson = response.getJSONObject("content");
                                         ChatMessage message = ChatMessage.fromJson(contentJson);

                                         callback.success(message);

                                     } catch (Exception e2) {
                                         callback.fail(e2.getMessage());
                                         return;
                                     }

                                 }
                             }

                );*/

    }
}
