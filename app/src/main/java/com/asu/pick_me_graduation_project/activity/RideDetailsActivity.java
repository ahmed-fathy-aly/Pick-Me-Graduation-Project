package com.asu.pick_me_graduation_project.activity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.asu.pick_me_graduation_project.database.DatabaseHelper;
import com.asu.pick_me_graduation_project.events.NewMessageEvent;
import com.asu.pick_me_graduation_project.events.UpdateRideEvent;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.view.OnPageSelectedListener;
import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RideDetailsActivity extends BaseActivity
{
    /* constants */
    public static final String SWITCH_TO_REQUEST_TAB = "switchToRequestsTab";
    public static final String SWITCH_TO_ANNOUNCEMENTS_TAB = "switchToAnnouncementsTab";

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

        // get ride id
        rideId = getIntent().getExtras().getString(Constants.RIDE_ID);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        contoller = new RidesAPIController(this);

        // load data
        readRideFromDatabase();

        // add listener to view pager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                try
                {
                    OnPageSelectedListener child = (OnPageSelectedListener) rideDetailsPagerAdapter.getItem(position);
                    child.onSelected();
                } catch (Exception e)
                {

                }
            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        // register for update event
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
     * reads the ride from the database (details, members) without requests
     * then load it from backend
     */
    private void readRideFromDatabase()
    {
        progressBar.setVisibility(View.VISIBLE);

        Loader<Ride> loader = getLoaderManager().initLoader(54, null, new LoaderManager.LoaderCallbacks<Ride>()
        {
            @Override
            public Loader<Ride> onCreateLoader(int id, Bundle args)
            {
                return new AsyncTaskLoader<Ride>(RideDetailsActivity.this)
                {
                    @Override
                    public Ride loadInBackground()
                    {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        Ride ride = databaseHelper.getRide(Integer.parseInt(rideId));
                        databaseHelper.close();
                        return ride;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Ride> loader, Ride data)
            {
                // if the ride is not there then let if go and load from backend
                if (data == null)
                {
                    loadRideFromBackend();
                    return;
                }

                onRideLoaded(data);

                // now load from backend
                loadRideFromBackend();
            }

            @Override
            public void onLoaderReset(Loader<Ride> loader)
            {
            }

        });
        loader.forceLoad();
    }


    /**
     * downloads the ride info from the api
     */
    private void loadRideFromBackend()
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
                        onRideLoaded(ride);
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
     * called after the ride was loaded from either the backend or database
     */
    private void onRideLoaded(Ride ride)
    {
        this.ride = ride;

        // check if we should show the ride join request
        String currentUserId = new AuthenticationAPIController(RideDetailsActivity.this).getCurrentUser().getUserId();
        boolean isDriver = currentUserId.equals(ride.getDriver().getUserId());
        boolean isMember = ride.containsMember(currentUserId);

        // setup view pager
        if (rideDetailsPagerAdapter == null)
        {
            rideDetailsPagerAdapter = new RideDetailsPagerAdapter(getSupportFragmentManager(), rideId, isMember, isDriver);
            viewPager.setAdapter(rideDetailsPagerAdapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);
        }

        // set the details
        rideDetailsPagerAdapter.getRideDetailsFragment().setData(ride);

        // add the members to the fragment
        rideDetailsPagerAdapter.getMembersListFragment().setMembers(ride.getMembers());

        // check if should open a specific tab
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(SWITCH_TO_REQUEST_TAB, false))
            if (viewPager.getChildCount() >= 4)
                viewPager.setCurrentItem(3, true);
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(SWITCH_TO_ANNOUNCEMENTS_TAB, false))
            if (viewPager.getChildCount() >= 3)
                viewPager.setCurrentItem(2, true);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateRideEvent updateRideEvent)
    {
        // check it's for this ride
        if (!updateRideEvent.getRideId().equals(rideId))
            return;

        loadRideFromBackend();
    }

}

