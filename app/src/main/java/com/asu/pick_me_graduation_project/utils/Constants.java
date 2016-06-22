package com.asu.pick_me_graduation_project.utils;

import android.util.Log;

/**
 * Created by ahmed on 2/7/2016.
 */
public class Constants
{

    /* API constants */
    public static String HOST = "http://pick-me.azurewebsites.net/api/";

    /* user constants */
    public static final String GENDER_MALE = "Male";
    public static final String USER_ID = "userId";
    public static final String GENDER_FEMALE_ = "Female";

    /* image constants */
    public static final int IMAGE_LOAD_ANIMATION_DURATION = 1000;

    /* community constants */
    public static final java.lang.String COMMUNITY_ID = "communityId";
    public static final java.lang.String COMMUNITY_NAME = "communityName";
    public static final java.lang.String IS_COMMUNITY_ADMIN = "isCommunityAdmin";

    /* ride constants */
    public static final String RIDE_ID = "rideId";
    public static final String SEARCH_RIDE_PARAMS = "searchRideParams";
    public static final String RIDES_LIST = "ridesList";

    /* notifications */
    public static final int NOTIFICATION_CHAT_ID_SUGGESTED_BY_OSSAMA_7ABIBY = 42;
    public static final int NOTIFICATION_JOIN_RIDE_ID= 7;
    public static final int NOTIFICATION_ACCEPTED_IN_RIDE = 11;
    public static final int NOTIFICATION_COMMUNITY_REQUESTS = 13;
    public static final int NOTIFICATION_COMMUNITY_UPDATE = 15;

    /**
     * uses the Noob multiplier to get a unique notification for each type and each object of that type
     * ex : 1 notification id for chat with specific users
     */
    public static int getNotificationId(int notificationTypeId, String objectId)
    {
        return objectId.hashCode() * notificationTypeId;
    }
}
