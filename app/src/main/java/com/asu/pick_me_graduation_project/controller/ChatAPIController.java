package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    /**
     * gets my recent chats
     */
    public void getRecentChats(String token, String userId, GetMessagesCallback callback)
    {
        // TODO - get them from back end instead of mock data later

        // make mock data
        List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            User user = new User();
            user.setUserId(i + "");
            user.setFirstName("name " + i);
            user.setLastName("afandem");
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

        callback.success(messages);
    }

    public void getChatMessages(String tokken, String myUserId, String otherUserId, GetMessagesCallback callback)
    {
        // make mock data
        List<ChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < 15; i++)
        {
            User from = new User();
            from.setUserId(i + "");
            from.setFirstName("name " + i);
            from.setLastName("afandem");
            from.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");


            User to = new User();
            from.setUserId(i + "");
            from.setFirstName("name " + i);
            from.setLastName("afandem");
            from.setProfilePictureUrl("http://zblogged.com/wp-content/uploads/2015/11/17.jpg");

            ChatMessage message = new ChatMessage();
            message.setContent("content " + i);
            message.setDate(Calendar.getInstance());
            message.getDate().add(Calendar.DAY_OF_MONTH, -1);
            message.setFrom(from);
            message.setTo(from);
            if (i % 2 == 0)
                to.setUserId(new AuthenticationAPIController(context).getCurrentUser().getUserId());
            else
                from.setUserId(new AuthenticationAPIController(context).getCurrentUser().getUserId());
            messages.add(message);
            if (i >= 3 && i <= 7)
            {
                message.getDate().add(Calendar.DAY_OF_MONTH, -2);
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

        callback.success(messages);
    }
}
