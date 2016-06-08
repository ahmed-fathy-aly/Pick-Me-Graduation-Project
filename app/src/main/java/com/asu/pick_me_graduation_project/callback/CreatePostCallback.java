package com.asu.pick_me_graduation_project.callback;


import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.CommunityPost;

/**
 * Created by ahmed on 12/18/2015.
 */
public interface CreatePostCallback
{
    void success(CommunityPost post);

    void fail(String message);
}
