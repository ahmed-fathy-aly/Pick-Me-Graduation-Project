package com.asu.pick_me_graduation_project.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.PostRidePagerAdapter;
import com.asu.pick_me_graduation_project.adapter.SearchRidePagerAdapter;
import com.asu.pick_me_graduation_project.callback.GetRidesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.SearchRideParams;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchRideActivity extends AppCompatActivity
{

    /* UI */
    @Bind(R.id.content)
    View content;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indicator)
    InkPageIndicator indicator;
    @Bind(R.id.buttonNextOrSubmit)
    Button buttonNextOrSubmit;

    /* fields */
    SearchRidePagerAdapter searchRidePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride);
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.search_ride));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // setup view pager
        searchRidePagerAdapter = new SearchRidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(searchRidePagerAdapter);
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                buttonNextOrSubmit.setText(getString(position == 0 ? R.string.next : R.string.submit));
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        // TODO test
        Ion.with(this)
                .load("GET", "http://pick-me.azurewebsites.net/api/test_by_get?value=blabla")
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        Log.e("Game", "result test = " + result);
                    }
                });
    }

    @OnClick(R.id.buttonNextOrSubmit)
    void onNextOrSubmitClicked()
    {
        if (viewPager.getCurrentItem() == 0)
        {
            viewPager.setCurrentItem(1, true);
            buttonNextOrSubmit.setText(getString(R.string.submit));
        } else
        {
            if (!validateData())
                return;

            // collect data
            final SearchRideParams searchRideParams = new SearchRideParams();
            searchRideParams.setSource(searchRidePagerAdapter.getChooseRouteFragment().getLocation("Source"));
            searchRideParams.setDestination(searchRidePagerAdapter.getChooseRouteFragment().getLocation("Destination"));
            searchRideParams.setFilteredCommunities(searchRidePagerAdapter.getSearchRidePreferencesFragment().getFilteredCommunities());
            searchRideParams.setTime(searchRidePagerAdapter.getSearchRidePreferencesFragment().getChpsemTime());

            // search rides
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.searching));
            RidesAPIController controller = new RidesAPIController(this);
            controller.searchRides(new AuthenticationAPIController(this).getTokken()
                    , searchRideParams
                    , new GetRidesCallback()
            {
                @Override
                public void success(List<Ride> rides)
                {
                    progressDialog.dismiss();

                    // check if no rides found
                    if (rides.size() == 0)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.no_rides_found), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    searchRideParams.setResult(rides);

                    // open search result list activity
                    Intent intent = new Intent(SearchRideActivity.this, SearchRideResults.class);
                    intent.putExtra(Constants.SEARCH_RIDE_PARAMS, searchRideParams);
                    startActivity(intent);

                }

                @Override
                public void fail(String error)
                {
                    Log.e("Game", "error " + error);
                    progressDialog.dismiss();
                    Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * checks all required data are entered
     */
    private boolean validateData()
    {
        boolean valid = true;

        // source and destination
        if (!searchRidePagerAdapter.getChooseRouteFragment().checkDataEntered())
        {
            viewPager.setCurrentItem(0, true);
            valid = false;
        }

        // time, communities, car details
        if (!searchRidePagerAdapter.getSearchRidePreferencesFragment().checkDataEntered())
        {
            valid = false;
        }

        return valid;
    }

}
