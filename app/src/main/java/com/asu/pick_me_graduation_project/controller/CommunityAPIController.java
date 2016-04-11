package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
import com.asu.pick_me_graduation_project.callback.CreatePostCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunityCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunityPostsCalback;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ahmed on 3/3/2016.
 */
public class CommunityAPIController {
    /* fields */
    Context context;
    private ResponseFuture<String> searchCommunitiesRequest;

    public CommunityAPIController(Context context) {
        this.context = context;
    }

    /**
     * makes a new community with that user as admin
     */
    public void createCommunity(String token, final String name, final String description, final CreateCommunityCallback callback) {

        // TODO - make a post to the back end
        JsonObject json = new JsonObject();
        json.addProperty("communityName", name);
        //json.addProperty("description", description);

        Ion.with(context)
                .load("http://pickmeasu.azurewebsites.net/api/Create_Community")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Log.e("Game", "error " + e.getMessage());
                        } else
                            Log.e("Game", "test create community result " + result);
                    }
                });


    }

    public void getMyCommunities(String token, final GetCommunitiesCallback callback) {
        Ion.with(context)
                .load("GET", Constants.HOST + "get_my_communities")
                .setHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        // check failed
                        if (e != null) {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "my communities result = " + result);
                        // parse the response
                        try {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse communities
                            JSONArray usersJson = response.getJSONArray("communities");
                            List<Community> communityList = new ArrayList<Community>();
                            for (int i = 0; i < usersJson.length(); i++) {
                                JSONObject communityJson = usersJson.getJSONObject(i);
                                Community community = Community.fromJson(communityJson);
                                community.setIsMember(true);
                                communityList.add(community);
                            }

                            // invoke callback
                            callback.success(communityList);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    public void searchCommunities(String token, final String searchString, final GetCommunitiesCallback callback) {
        // cancel any previous request
        if (searchCommunitiesRequest != null)
            searchCommunitiesRequest.cancel();

        // make the request
        String url = Constants.HOST + "search_communities?searchString=" + searchString;
        searchCommunitiesRequest = Ion.with(context)
                .load("GET", url)
                .setHeader("Authorization", "Bearer " + token)
                .asString();
        searchCommunitiesRequest
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        // check failed
                        if (e != null) {
                            if (!searchCommunitiesRequest.isCancelled())
                                callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "search communities result = " + result);

                        // parse the response
                        try {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse communities
                            JSONArray communities = response.getJSONArray("communities");
                            List<Community> communityList = new ArrayList<Community>();
                            for (int i = 0; i < communities.length(); i++) {
                                JSONObject communityJson = communities.getJSONObject(i);
                                Community community = Community.fromJson(communityJson);
                                communityList.add(community);
                            }

                            // invoke callback
                            callback.success(communityList);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }


    /**
     * gets the members who are in this community
     */
    public void getCommunityMembers(String tokken, String userId, String communityId, final GetUsersCallback callback) {
        // TODO now it just gets all users who have the character m

        String url = "http://pickmeasu.azurewebsites.net/api/search_for_user"
                + "?searchString=" + "m"
                + "&count=-1";
        Log.e("Game", "searching for " + url);
        Ion.with(context)
                .load("GET", url)
                .setHeader("Content-Type", "application/json")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // check failed
                        if (e != null) {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "search result = " + result);

                        // parse the response
                        try {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse user
                            JSONArray usersJson = response.getJSONArray("users");
                            List<User> usersList = new ArrayList<User>();
                            for (int i = 0; i < usersJson.length(); i++) {
                                JSONObject userJson = usersJson.getJSONObject(i).getJSONObject("user");
                                User user = User.fromJson(userJson);
                                usersList.add(user);

                            }

                            // invoke callback
                            callback.success(usersList);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    /**
     * downloads the community profile
     */
    public void getCommunityProfile(String token, String communityId, final GetCommunityCallback callback) {
        String url = Constants.HOST + "get_community_profile?communityID=" + communityId;
        Ion.with(context)
                .load("GET", url)
                .setHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        // check failed
                        if (e != null) {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "community profile result = " + result);

                        // parse the response
                        try {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse the community
                            Community community = Community.fromJson(response.getJSONObject("community"));
                            callback.success(community);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    /**
     * downloads the community's posts
     */
    public void getCommunityPosts(String token, String communityId, final GetCommunityPostsCalback callback) {
        String url = Constants.HOST + "get_community_posts?communityID=" + communityId;
        Ion.with(context)
                .load("GET", url)
                .setHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        // check failed
                        if (e != null) {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "community profile result = " + result);

                        // parse the response
                        try {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse the posts
                            List<CommunityPost> posts = new ArrayList<CommunityPost>();
                            JSONArray postsJson = response.getJSONArray("posts");
                            for (int i = 0; i < postsJson.length(); i++)
                                posts.add(CommunityPost.parseFromJson(postsJson.getJSONObject(i)));
                            callback.success(posts);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }

    /**
     * creates a new post to a community
     *
     * @param tokken
     * @param conentText
     */
    public void createPost(String tokken, String communityId, String conentText, final CreatePostCallback callback) {
        String url = Constants.HOST + "make_community_post";
        JsonObject json = new JsonObject();
        json.addProperty("communityId", communityId);
        json.addProperty("content", conentText);


        Ion.with(context)
                .load("http://pickmeasu.azurewebsites.net/api/Create_Community")
                .addHeader("Authorization", "Bearer " + tokken)
                .addHeader("Content-Type", "application/json")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // check failed
                        if (e != null) {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "create post result = " + result);

                        // parse the response
                        try {
                            // check status
                            JSONObject response = new JSONObject(result);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse the post
                            JSONObject postJson = response.getJSONObject("newPost");
                            CommunityPost post = CommunityPost.parseFromJson(postJson);
                            callback.success(post);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }
}
