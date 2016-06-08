package com.asu.pick_me_graduation_project.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.PostRidePagerAdapter;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.Ride;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostRideActivity extends BaseActivity
{


    /* UI */
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indicator)
    InkPageIndicator indicator;
    @Bind(R.id.buttonNextOrSubmit)
    Button buttonNextOrSubmit;

    /* fields */
    private PostRidePagerAdapter postRidePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ride);
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.post_ride));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // setup view pager
        postRidePagerAdapter = new PostRidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(postRidePagerAdapter);
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
                buttonNextOrSubmit.setText(getString(position == 0 ? R.string.next :  R.string.submit));
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

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
        }
        else
        {
            // check valid data entered
            if (!validateData())
                return;

            // collect the ride details
            Ride ride = new Ride();
            postRidePagerAdapter.getChooseRouteFragment().fillRideInfo(ride);
            postRidePagerAdapter.getPostRidePreferencesFragment().fillRideInfo(ride);

            // post
            final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.posting));
            RidesAPIController controller = new RidesAPIController(this);
            controller.postRide(new AuthenticationAPIController(this).getTokken()
            ,ride
            , new GenericSuccessCallback()
            {
                @Override
                public void success()
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT);
                    finish();
                }

                @Override
                public void fail(String message)
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

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
        if (!postRidePagerAdapter.getChooseRouteFragment().checkDataEntered())
        {
            viewPager.setCurrentItem(0, true);
            valid = false;
        }

        // time, communities, car details
        if (!postRidePagerAdapter.getPostRidePreferencesFragment().checkDataEntered())
        {
            valid = false;
        }

        return valid;
    }

}
