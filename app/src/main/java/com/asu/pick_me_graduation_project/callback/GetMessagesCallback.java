package com.asu.pick_me_graduation_project.callback;

import com.asu.pick_me_graduation_project.model.ChatMessage;

import java.util.List;

/**
 * Created by ahmed on 3/2/2016.
 */
public interface GetMessagesCallback
{
    void success(List<ChatMessage> messages);

    void fail(String error);
}
