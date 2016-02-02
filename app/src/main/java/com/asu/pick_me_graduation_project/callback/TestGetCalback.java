package com.asu.pick_me_graduation_project.callback;

import com.asu.pick_me_graduation_project.model.Contact;

import java.util.List;

/**
 * Created by ahmed on 12/17/2015.
 */
public interface TestGetCalback
{
    void success(List<Contact> books);
    void fail(String message);
}
