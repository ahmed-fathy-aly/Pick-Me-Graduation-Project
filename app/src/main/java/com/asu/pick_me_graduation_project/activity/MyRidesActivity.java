package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.fragment.RideListFragment;
import com.asu.pick_me_graduation_project.model.Ride;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyRidesActivity extends BaseActivity
{
    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content)
    View content;
    @Bind(R.id.fragmentContainer)
    View fragmentContainer;
    private RideListFragment rideListFragment;

    /* fields */
    private RidesAPIController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rides);
        ButterKnife.bind(this);

        // setup common views
        toolbar.setTitle(getString(R.string.title_my_rides));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_my_rides));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);

        // setup fields
        controller = new RidesAPIController(this);

        // setup fragment
        rideListFragment = new RideListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, rideListFragment)
                .commit();

        // load data
        getMyRides();
    }

    /**
     * downloads my rides from the backend
     */
    private void getMyRides()
    {
        progressBar.setVisibility(View.VISIBLE);
        controller.getMyRides(new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , new GetRidesCallback()
        {
            @Override
            public void success(List<Ride> rides)
            {
                progressBar.setVisibility(View.INVISIBLE);

                // TODO set the user to the rides
                for (Ride ride : rides)
                {
                    ride.setRider(new AuthenticationAPIController(getApplicationContext()).getCurrentUser());
                    ride.setCanRequestToJoin(false);
                }
                rideListFragment.setData(rides);
            }

            @Override
            public void fail(String error)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
