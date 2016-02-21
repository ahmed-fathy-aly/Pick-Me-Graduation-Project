package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.User;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface SetProfileCallback
{
    void success(User user);

    void fail(String message);
}
