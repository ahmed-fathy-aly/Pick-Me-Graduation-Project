package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.callback.SendMessageCallback;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
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
        String url = Constants.HOST + "Message/get_recent_chats" + "?count=-1";
        Ion.with(context)
                .load("GET", url)
                .addHeader("Authorization", "Bearer " + token)
                .asString().setCallback(new FutureCallback<String>()
        {
            @Override
            public void onCompleted(Exception e, String result)
            {
                //check failed
                if (e != null)
                {
                    Log.e("Game", "error " + e.getMessage());
                    callback.fail(e.getMessage());
                    return;
                }
                //parse el response
                try
                {


                    JSONObject response = new JSONObject(result);
                    int status = response.getInt("status");
                    if (status == 0)
                    {
                        String message = response.getString("message");
                        callback.fail(message);
                        return;
                    }
                    //parse
                    JSONArray chatsJson = response.getJSONArray("chats");
                    List<ChatMessage> messages2 = new ArrayList<ChatMessage>();
                    for (int i = 0; i < chatsJson.length(); i++)
                    {
                        JSONObject chatJson = chatsJson.getJSONObject(i);
                        ChatMessage chat = ChatMessage.fromJson(chatJson);
                        messages2.add(chat);

                    }
                    Collections.sort(messages2, new Comparator<ChatMessage>()
                    {
                        public int compare(ChatMessage M1, ChatMessage M2)
                        {
                            return M2.getDate().compareTo(M1.getDate());
                        }
                    });
                    callback.success(messages2);
                } catch (Exception e2)
                {
                    callback.fail(e2.getMessage());
                    return;
                }


            }
        });

    }

    public void getChatMessages(String tokken, String myUserId, String otherUserId, final GetMessagesCallback callback)
    {
        //from backend
        String url = Constants.HOST + "Message/get_a_conversation" + "?count=-1" + "&userId=" + otherUserId;
        Ion.with(context)
                .load("GET", url).
                addHeader("Authorization", "Bearer " + tokken)
                .asString().setCallback(new FutureCallback<String>()
        {
            @Override
            public void onCompleted(Exception e, String result)
            {
                //check el fail
                if (e != null)
                {
                    Log.e("Game", "error " + e.getMessage());
                    callback.fail(e.getMessage());
                    return;
                }
                //parse el response
                try
                {
                    Log.e("Game", "chat response  " +result );

                    JSONObject response = new JSONObject(result);
                    int status = response.getInt("status");
                    if (status == 0)
                    {
                        String message = response.getString("message");
                        callback.fail(message);
                        return;
                    }
                    JSONArray messegesJson = response.getJSONArray("messages");
                    List<ChatMessage> messages3 = new ArrayList<ChatMessage>();
                    for (int i = 0; i < messegesJson.length(); i++)
                    {
                        JSONObject messageJson = messegesJson.getJSONObject(i);
                        ChatMessage message = ChatMessage.fromJson(messageJson);
                        messages3.add(message);
                    }
                    //sort
                    Collections.sort(messages3, new Comparator<ChatMessage>()
                    {
                        public int compare(ChatMessage M1, ChatMessage M2)
                        {
                            return M1.getDate().compareTo(M2.getDate());
                        }
                    });
                    callback.success(messages3);

                } catch (Exception e2)
                {
                    callback.fail(e2.getMessage());
                    return;
                }


            }

        });


    }

    public void sendMessage(String content, String extras, String userId, String token, final SendMessageCallback callback)
    {
        String url = Constants.HOST + "Message/send_message";

        JsonObject json = new JsonObject();
        json.addProperty("content", content);
        json.addProperty("userId", userId);
        json.addProperty("extras", extras);
        String body = json.toString();

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        Fuel.post(url).header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
                        Log.e("Game", "send message  request" + request.toString());
                        Log.e("Game", "send message  response" + r.toString());


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
                            JSONObject contentJson = response.getJSONObject("newMessage");
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

    }
}
