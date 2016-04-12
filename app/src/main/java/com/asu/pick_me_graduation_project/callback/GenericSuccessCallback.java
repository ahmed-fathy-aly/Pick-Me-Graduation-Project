package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.User;

/**
 * Created by ahmed on 04/12/2016.
 */
public interface GenericSuccessCallback
{
    void success();

    void fail(String message);
}
