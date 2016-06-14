package com.asu.pick_me_graduation_project.callback;
import  com.asu.pick_me_graduation_project.model.ChatMessage;

/**
 * Created by ARaafat on 3/25/2016.
 */
public interface SendMessageCallback {
    void success (ChatMessage chatMessage);
    void fail (String message);
}
