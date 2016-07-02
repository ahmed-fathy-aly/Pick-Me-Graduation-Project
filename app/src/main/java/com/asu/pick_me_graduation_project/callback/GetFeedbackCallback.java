package com.asu.pick_me_graduation_project.callback;

import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.Feedback;

import java.util.List;

/**
 * Created by ahmed on 3/8/2016.
 */
public interface GetFeedbackCallback
{
    void success(List<Feedback> feedbackList);

    void fail(String errorMessage);
}
