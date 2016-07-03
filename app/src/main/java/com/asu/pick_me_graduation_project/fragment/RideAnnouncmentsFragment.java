package com.asu.pick_me_graduation_project.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.RideAnnoucementsAdapter;
import com.asu.pick_me_graduation_project.callback.CreateAnnouncementCallback;
import com.asu.pick_me_graduation_project.callback.GetAnnouncementsCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.RideAnnouncement;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.github.kittinunf.fuel.Fuel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideAnnouncmentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    /* views */
    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.recyclerViewAnnoucnments)
    RecyclerView recyclerViewAnnoucnments;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    /* fields */
    private String rideId;
    private RidesAPIController controller;
    private RideAnnoucementsAdapter adapterAnnouncements;



    /* life cycle methods */

    public RideAnnouncmentsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.rideId = getArguments().getString(Constants.RIDE_ID);
        this.controller = new RidesAPIController(getContext());
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout and reference views
        View view = inflater.inflate(R.layout.fragment_ride_announcments, container, false);
        ButterKnife.bind(this, view);

        // setup recycler view
        adapterAnnouncements = new RideAnnoucementsAdapter(getContext());
        recyclerViewAnnoucnments.setAdapter(adapterAnnouncements);
        recyclerViewAnnoucnments.setLayoutManager(new LinearLayoutManager(getContext()));

        // setup swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this);

        // download data
        downloadAnnouncements();

        return view;
    }


    /* listener */

    @Override
    public void onRefresh()
    {
        downloadAnnouncements();
    }

    @OnClick(R.id.fab)
    void openCreateAnnouncement()
    {
        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.make_an_announcment));

        // Set up the input
        final EditText editTextMessage = new EditText(getContext());
        editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextMessage.setHint(getString(R.string.announcment));
        editTextMessage.setGravity(Gravity.CENTER_VERTICAL);
        builder.setView(editTextMessage);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String content = editTextMessage.getText().toString();
                postAnnouncement(content);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /* methods */

    /**
     * downloads the announcements from the backend
     */
    private void downloadAnnouncements()
    {
        // show refreshing
        if (!swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.post(new Runnable()
            {
                @Override
                public void run()
                {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });

        // download the announcements
        controller.getRideAnnouncements(
                new AuthenticationAPIController(getContext()).getTokken()
                , rideId
                , new GetAnnouncementsCallback()
                {
                    @Override
                    public void success(List<RideAnnouncement> announcmentList)
                    {
                        if (!isAdded())
                            return;
                        swipeRefreshLayout.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        adapterAnnouncements.setData(announcmentList);
                    }

                    @Override
                    public void fail(String message)
                    {
                        if (!isAdded())
                            return;
                        swipeRefreshLayout.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }


    /**
     * make a post request to the backend
     */
    private void postAnnouncement(final String announcementContent)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.posting));
        controller.postAnnouncement(
                new AuthenticationAPIController(getContext()).getTokken()
                , rideId
                , announcementContent
                , new CreateAnnouncementCallback()
                {
                    @Override
                    public void success(RideAnnouncement announcement)
                    {
                        progressDialog.dismiss();
                        Log.e("Game", "succes " + announcement.getContent());
                        adapterAnnouncements.addToTop(announcement);
                    }

                    @Override
                    public void fail(String message)
                    {
                        progressDialog.dismiss();
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


}
