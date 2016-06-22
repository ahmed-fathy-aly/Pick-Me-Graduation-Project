package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.RideDetailsPagerAdapter;
import com.asu.pick_me_graduation_project.callback.GetRideCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.Ride;
import com.google.android.gms.maps.GoogleMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RideDetailsActivity extends AppCompatActivity
{
    /* constants */
    public static final String SWITCH_TO_REQUEST_TAB = "switchToRequestsTab";

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
    Ride ride;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        // TODO get ride id
        //rideId = getIntent().getExtras().getString(Constants.RIDE_ID);
        rideId = "60";

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        contoller = new RidesAPIController(this);


        // load data
        loadRide();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_ride_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_item_share)
            shareRidePhoto();
        return super.onOptionsItemSelected(item);

    }


    /**
     * downloads the ride info from the api
     */
    private void loadRide()
    {
        progressBar.setVisibility(View.VISIBLE);

        contoller.getRideDetails(new AuthenticationAPIController(this).getTokken()
                , rideId
                , new GetRideCallback()
        {
            @Override
            public void success(Ride ride)
            {
                progressBar.setVisibility(View.INVISIBLE);
                RideDetailsActivity.this.ride  = ride;

                // check if we should show the ride join request
                String currentUserId = new AuthenticationAPIController(RideDetailsActivity.this).getCurrentUser().getUserId();
                boolean showRideRequests = currentUserId.equals(ride.getRider().getUserId());

                // setup view pager
                rideDetailsPagerAdapter = new RideDetailsPagerAdapter(getSupportFragmentManager(), rideId, showRideRequests);
                viewPager.setAdapter(rideDetailsPagerAdapter);
                viewPager.setOffscreenPageLimit(3);
                tabLayout.setupWithViewPager(viewPager);

                // set the details
                rideDetailsPagerAdapter.getRideDetailsFragment().setData(ride);

                // add the members to the fragment
                rideDetailsPagerAdapter.getMembersListFragment().setMembers(ride.getMembers());

                // check if should open the requests tab
                if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(SWITCH_TO_REQUEST_TAB, false))
                    if (viewPager.getChildCount() == 3)
                        viewPager.setCurrentItem(2, true);

            }

            @Override
            public void fail(String error)
            {
                progressBar.setVisibility(View.INVISIBLE);

                Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * creates an image of the ride map and details and share it
     */
    private void shareRidePhoto()
    {
        // get the bitmap to be drawn
        rideDetailsPagerAdapter.getRideDetailsFragment().getMapsView().requestBitmap(new GoogleMap.SnapshotReadyCallback()
        {
            @Override
            public void onSnapshotReady(Bitmap bitmap)
            {

                try
                {
                    // store to a file
                    File file = new File(Environment.getExternalStorageDirectory(), "SharedRide" + new Random().nextInt(100000) + ".jpeg");
                    OutputStream output = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                    output.flush();
                    output.close();
                    bitmap.recycle();

                    // make the share intent
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpeg");
                    Uri uri = Uri.fromFile(file);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(shareIntent, "Share Ride"));

                } catch (Exception e)
                {
                }

            }
        });

    }
}
