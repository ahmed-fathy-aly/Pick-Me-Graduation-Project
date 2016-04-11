package com.asu.pick_me_graduation_project.callback;

import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.CommunityPost;

import java.util.List;

/**
 * Created by ahmed on 10/4/2016.
 */
public interface GetCommunityPostsCalback
{
    void success(List<CommunityPost> posts);

    void fail(String error);
}
