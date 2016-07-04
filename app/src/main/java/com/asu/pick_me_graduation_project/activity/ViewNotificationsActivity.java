package com.asu.pick_me_graduation_project.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.NotificationsAdapter;
import com.asu.pick_me_graduation_project.callback.GetNotificationsCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.NotificationsAPIController;
import com.asu.pick_me_graduation_project.events.NewMessageEvent;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.Notification;
import com.asu.pick_me_graduation_project.services.GetMyRidesService;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.view.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.SortedMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewNotificationsActivity extends BaseActivity implements NotificationsAdapter.Listener
{

    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerViewNotifications)
    RecyclerView recyclerViewNotifications;
    @Bind(R.id.content)
    LinearLayout content;

    /* fields */
    NotificationsAdapter adapterNotifications;
    NotificationsAPIController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notifications));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // set fields
        controller = new NotificationsAPIController(this);

        // setup recycler view
        adapterNotifications = new NotificationsAdapter(this);
        adapterNotifications.setListener(this);
        recyclerViewNotifications.setAdapter(adapterNotifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // get data
        downloadNotifications();
    }

    /**
     * makes a GET request from the backend to get all notifications and add them to the adapter
     */
    private void downloadNotifications()
    {
        progressBar.setVisibility(View.VISIBLE);

        controller.getNotifications(
                new AuthenticationAPIController(this).getTokken()
                , new GetNotificationsCallback()
                {
                    @Override
                    public void success(List<Notification> notificationList)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        adapterNotifications.setData(notificationList);
                    }

                    @Override
                    public void fail(String message)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onNotificationClick(Notification notification)
    {

        if (notification.getType() == null)
            return;
        String data = notification.getExtras();

        switch (notification.getType())
        {
            case "announcement":
                break;

            case "sendFeedback":
                try
                {
                    // parse the data
                    JSONObject rideJson = new JSONObject(data);
                    String rideId = rideJson.getString("rideId");

                    // set the pending intent
                    Intent feedbackIntent = new Intent(this, FeedBackActivity.class);
                    feedbackIntent.putExtra(Constants.RIDE_ID, rideId);
                    startActivity(feedbackIntent);

                } catch (Exception e)
                {
                }
                break;

            case "joinRideRequest":
                try
                {
                    // parse the data
                    JSONObject rideJson = new JSONObject(data);
                    String rideId = rideJson.getString("rideId");

                    // set the  intent
                    Intent rideDetailsIntent = new Intent(this, RideDetailsActivity.class);
                    rideDetailsIntent.putExtra(Constants.RIDE_ID, rideId);
                    rideDetailsIntent.putExtra(RideDetailsActivity.SWITCH_TO_REQUEST_TAB, true);
                    startActivity(rideDetailsIntent);

                } catch (Exception e)
                {
                    Log.e("Game", "error = " + e.getMessage());
                }
                break;


            case "acceptedInRide":
                try
                {
                    // parse the data
                    JSONObject rideJson = new JSONObject(data);
                    String rideId = rideJson.getString("rideId");


                    // set the  intent
                    Intent rideDetailsIntent = new Intent(this, RideDetailsActivity.class);
                    rideDetailsIntent.putExtra(Constants.RIDE_ID, rideId);
                    startActivity(rideDetailsIntent);
                } catch (Exception e)
                {
                }
                break;

            case "communityRequestNotification":
                try
                {
                    // parse the data
                    JSONObject communityJson = new JSONObject(data);
                    String communityId = communityJson.getString("communityId");

                    // set the pending intent
                    Intent communityRequestsIntent = new Intent(this, CommunityProfileActivity.class);
                    communityRequestsIntent.putExtra(Constants.COMMUNITY_ID, communityId);
                    communityRequestsIntent.putExtra(Constants.IS_COMMUNITY_ADMIN, true);
                    communityRequestsIntent.putExtra(CommunityProfileActivity.SWITCH_TO_REQUEST_TAB, true);
                    startActivity(communityRequestsIntent);

                } catch (Exception e)
                {
                    Log.e("Game", "error " + e.getMessage());
                    e.printStackTrace();
                }
                break;

            case "acceptedInCommnityNotification":
                try
                {
                    // parse the data
                    JSONObject communityJson = new JSONObject(data);
                    String communityId = communityJson.getString("communityId");
                    boolean isAdmin = false;

                    // set the pending intent
                    Intent communityDetailsIntent = new Intent(this, CommunityProfileActivity.class);
                    communityDetailsIntent.putExtra(Constants.COMMUNITY_ID, communityId);
                    communityDetailsIntent.putExtra(Constants.IS_COMMUNITY_ADMIN, isAdmin);
                    startActivity(communityDetailsIntent);

                } catch (Exception e)
                {
                }
                break;

            default:
                Log.e("Game", "found no match " + notification.getType());
        }
    }
}
