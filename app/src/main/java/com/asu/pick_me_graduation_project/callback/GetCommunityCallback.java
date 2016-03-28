package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.User;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface GetCommunityCallback
{
    void success(Community community);

    void fail(String message);
}
