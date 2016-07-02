package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.RideAnnouncement;

import java.util.List;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface GetAnnouncementsCallback
{
    void success(List<RideAnnouncement> announcmentList);

    void fail(String message);
}
