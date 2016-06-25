package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.database.DatabaseHelper;
import com.asu.pick_me_graduation_project.fragment.RideListFragment;
import com.asu.pick_me_graduation_project.model.Ride;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyRidesActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
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
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
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

        // setup swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.accent);
        swipeRefreshLayout.setOnRefreshListener(this);

        // load data
        readRidesFromDatabase();

    }

    @Override
    public void onRefresh()
    {
        getMyRidesFromBackend();
    }

    /**
     * downloads my rides from the backend
     */
    private void getMyRidesFromBackend()
    {
        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        controller.getMyRides(new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , new GetRidesCallback()
        {
            @Override
            public void success(List<Ride> rides)
            {
                swipeRefreshLayout.setRefreshing(false);

                // set to the fragment
                for (Ride ride : rides)
                    ride.setCanRequestToJoin(false);
                rideListFragment.setData(rides);

                // save to database
                saveRidesToDatbase(rides);
            }

            @Override
            public void fail(String error)
            {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * uses an async task loaders to read the rides from the database
     */
    private void readRidesFromDatabase()
    {
        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        Loader<List<Ride>> loader = getSupportLoaderManager().initLoader(12, null
                , new LoaderManager.LoaderCallbacks<List<Ride>>()
        {
            @Override
            public Loader<List<Ride>> onCreateLoader(int id, Bundle args)
            {

                return new android.support.v4.content.AsyncTaskLoader<List<Ride>>(MyRidesActivity.this)
                {
                    @Override
                    public List<Ride> loadInBackground()
                    {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        List<Ride> rides = databaseHelper.getAllRides();
                        Log.e("Game", "found " + rides.size());
                        databaseHelper.close();
                        return rides;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Ride>> loader, List<Ride> data)
            {
                swipeRefreshLayout.setRefreshing(false);

                if (data.size() > 0)
                    rideListFragment.setData(data);
                else
                    getMyRidesFromBackend();
            }

            @Override
            public void onLoaderReset(Loader<List<Ride>> loader)
            {
            }
        });
        loader.forceLoad();
    }

    /**
     * clears the rides database and add these rides
     */
    private void saveRidesToDatbase(final List<Ride> rides)
    {
        Loader<Boolean> loader = getSupportLoaderManager().initLoader(54, null
                , new LoaderManager.LoaderCallbacks<Boolean>()
        {
            @Override
            public Loader<Boolean> onCreateLoader(int id, Bundle args)
            {

                return new AsyncTaskLoader<Boolean>(MyRidesActivity.this)
                {
                    @Override
                    public Boolean loadInBackground()
                    {
                        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                        databaseHelper.clearTables();
                        databaseHelper.insertRides(rides);
                        databaseHelper.close();
                        return true;
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Boolean> loader, Boolean data)
            {
            }

            @Override
            public void onLoaderReset(Loader<Boolean> loader)
            {
            }

        });
        loader.forceLoad();
    }


}
