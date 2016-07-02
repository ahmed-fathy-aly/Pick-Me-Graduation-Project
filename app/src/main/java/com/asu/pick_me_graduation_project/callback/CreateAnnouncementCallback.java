package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.RideAnnouncement;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface CreateAnnouncementCallback
{
    void success(RideAnnouncement announcment);

    void fail(String message);
}
