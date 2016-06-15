package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.User;

import java.util.List;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface GetFeedbackFormCallback
{
    void success(List<User> users, String riderId);

    void fail(String message);
}
