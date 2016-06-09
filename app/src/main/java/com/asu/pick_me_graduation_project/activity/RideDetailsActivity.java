package com.asu.pick_me_graduation_project.activity;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.CommunityPagerAdapter;
import com.asu.pick_me_graduation_project.adapter.RideDetailsPagerAdapter;
import com.asu.pick_me_graduation_project.callback.GetRideCallback;
import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RideDetailsActivity extends AppCompatActivity
{
    /* UI */
    @Bind(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.content)
    View content;

    /* Fields */
    String rideId;
    RidesAPIController contoller;
    RideDetailsPagerAdapter rideDetailsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        // TODO get ride id
        rideId = getIntent().getExtras().getString(Constants.RIDE_ID);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        contoller = new RidesAPIController(this);

        // setup view pager
        rideDetailsPagerAdapter = new RideDetailsPagerAdapter(getSupportFragmentManager(), rideId);
        viewPager.setAdapter(rideDetailsPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        // load data
        loadRide();

        rideDetailsPagerAdapter.addRideJoinRequestsFragment(rideId);

    }

    /**
     * downloads the ride info from the api
     */
    private void loadRide()
    {
        contoller.getRideDetails(new AuthenticationAPIController(this).getTokken()
                , rideId
                , new GetRideCallback()
        {
            @Override
            public void success(Ride ride)
            {
                rideDetailsPagerAdapter.getRideDetailsFragment().setData(ride);

                // check if we should show the ride join request
                String currentUserId = new AuthenticationAPIController(RideDetailsActivity.this).getCurrentUser().getUserId();
                if (currentUserId.equals(ride.getRider().getUserId()))
                    rideDetailsPagerAdapter.addRideJoinRequestsFragment(rideId);
            }

            @Override
            public void fail(String error)
            {
                Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
