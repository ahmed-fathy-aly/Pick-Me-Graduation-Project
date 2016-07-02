package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.NotificationsAdapter;
import com.asu.pick_me_graduation_project.callback.GetNotificationsCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.NotificationsAPIController;
import com.asu.pick_me_graduation_project.model.Notification;

import java.util.List;
import java.util.SortedMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewNotificationsActivity extends AppCompatActivity
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
        recyclerViewNotifications.setAdapter(adapterNotifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));

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
}
