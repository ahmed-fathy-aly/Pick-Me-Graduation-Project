package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.model.RideAnnouncment;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface CreateAnnouncementCallback
{
    void success(RideAnnouncment announcment);

    void fail(String message);
}
