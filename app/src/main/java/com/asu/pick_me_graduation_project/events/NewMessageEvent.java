package com.asu.pick_me_graduation_project.events;

import com.asu.pick_me_graduation_project.model.ChatMessage;

/**
 * Created by ahmed on 6/18/2016.
 * when a notification tells the chat activity that a new message arrived
 */
public class NewMessageEvent
{
    private ChatMessage chatMessage;
    private String userId;

    public NewMessageEvent(ChatMessage chatMessage, String userId)
    {
        this.chatMessage = chatMessage;
        this.userId = userId;
    }

    public ChatMessage getChatMessage()
    {
        return chatMessage;
    }

    public String getUserId()
    {
        return userId;
    }
}
