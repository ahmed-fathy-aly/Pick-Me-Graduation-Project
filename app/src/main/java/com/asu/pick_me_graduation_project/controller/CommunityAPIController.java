package com.asu.pick_me_graduation_project.controller;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
import com.asu.pick_me_graduation_project.callback.CreatePostCallback;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunityCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunityPostsCalback;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.future.ResponseFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        String url = Constants.HOST + "/create_community";

        JsonObject json = new JsonObject();
        json.addProperty("communityName", name);
        String body = json.toString();
        Log.e("Game", "body = " + body);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        headers.put("Content-Type", "application/json");

        Fuel.post(url)
                .header(headers)
                .body(body, Charset.defaultCharset())
                .responseString(new com.github.kittinunf.fuel.core.Handler<String>()
                {
                    @Override
                    public void success(Request request, Response r, String s)
                    {
                        Log.e("Game", "success " + s);
                        try {
                            // check status
                            JSONObject response = new JSONObject(s);
                            int status = response.getInt("status");
                            if (status == 0) {
                                String message = response.getString("message");
                                callback.fail(message);
                                return;
                            }

                            // parse community
                            JSONObject communityJson = response.getJSONObject("newCommunity");
                            Community community = Community.fromJson(communityJson);
                            community.setIsAdmin(true);
                            community.setIsMember(true);
                            // invoke callback
                            callback.success(community);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }

                    @Override
                    public void failure(Request request, Response resp, FuelError fuelError)
                    {
                        Log.e("Game", "error " + fuelError.getMessage());
                        Log.e("Game", "request " + request.toString());
                        Log.e("Game", "response" + resp.toString());

                        callback.fail(fuelError.getMessage());
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
        String url = "http://pickmeasu.azurewebsites.net/api/get_community_members?communityID=" + communityId;
        Ion.with(context)
                .load("GET", url)
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + tokken)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // check failed
                        if (e != null) {
                            callback.fail(e.getMessage());
                            return;
                        }
                        Log.e("Game", "get community members result = " + result);

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
                            JSONArray usersJson = response.getJSONArray("members");
                            List<User> usersList = new ArrayList<User>();
                            for (int i = 0; i < usersJson.length(); i++) {
                                JSONObject userJson = usersJson.getJSONObject(i);
                                User user = User.fromJson(userJson);
                                usersList.add(user);

                            }

                            // invoke callback
                            callback.success(usersList);
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            Log.e("Game", "get members error " + e2.getMessage());
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

    public void requestToJoinCommunity(String tokken, String communityId, final GenericSuccessCallback callback) {
        String url = Constants.HOST + "join_community";
        JsonObject json = new JsonObject();
        json.addProperty("communityId", communityId);


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
                        Log.e("Game", "join community result = " + result);

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

                            callback.success();
                        } catch (Exception e2) {
                            callback.fail(e2.getMessage());
                            return;
                        }
                    }
                });
    }
}
