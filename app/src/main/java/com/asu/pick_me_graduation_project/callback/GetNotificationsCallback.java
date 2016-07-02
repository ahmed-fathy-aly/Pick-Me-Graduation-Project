package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.Notification;

import java.util.List;

/**
 * Created by ahmed on 7/2/2015.
 */
public interface GetNotificationsCallback
{
    void success(List<Notification> notificationList);

    void fail(String message);
}
