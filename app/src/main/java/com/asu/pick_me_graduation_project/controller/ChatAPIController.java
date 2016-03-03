package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public void getChats(String userId, GetMessagesCallback callback)
    {
        // TODO - get them from back end instead of mock data later

        // make mock data
        List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 10; i ++)
        {
            User user = new User();
            user.setUserId(i + "");
            user.setFirstName("name " + i);
            user.setLastName("afandem");
            user.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");

            ChatMessage message = new ChatMessage();
            message.setContent("content " + i);
            message.setDate(Calendar.getInstance());
            message.setFrom(user);
            message.setTo(user);

            messages.add(message);
        }
        callback.success(messages);
    }
}
