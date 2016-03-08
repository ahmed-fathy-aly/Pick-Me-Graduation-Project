package com.asu.pick_me_graduation_project.callback;

import com.asu.pick_me_graduation_project.model.Community;

import java.util.List;

/**
 * Created by ahmed on 3/8/2016.
 */
public interface  GetCommunitiesCallback
{
    void success(List<Community> communityList);

    void fail(String errorMessage);
}
