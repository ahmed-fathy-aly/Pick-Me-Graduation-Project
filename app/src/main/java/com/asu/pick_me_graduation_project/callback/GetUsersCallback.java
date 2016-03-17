package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.User;

import java.util.List;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface GetUsersCallback
{
    void success(List<User> users);

    void fail(String message);
}
