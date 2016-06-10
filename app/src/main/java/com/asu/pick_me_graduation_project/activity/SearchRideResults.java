package com.asu.pick_me_graduation_project.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.BuildConfig;
import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.fragment.RideListFragment;
import com.asu.pick_me_graduation_project.adapter.RidesAdapter;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.SearchRideParams;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchRideResults extends AppCompatActivity implements RideListFragment.Listener
{

    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fragmentContainer)
    FrameLayout fragmentContainer;
    @Bind(R.id.content)
    LinearLayout content;

    /* fields */
    SearchRideParams searchRideParams;
    private RideListFragment rideListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ride_results);
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_search_result));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);

        // receive the search rides params
        searchRideParams = (SearchRideParams) getIntent().getExtras().getSerializable(Constants.SEARCH_RIDE_PARAMS);

        // setup fragment
        rideListFragment = new RideListFragment(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, rideListFragment)
                .commit();

        // set data
        rideListFragment.setData(searchRideParams.getResult());

        // load interstitial ad (currently disabled)
        if (false)
            loadAd();
    }


    @Override
    public void onRequestToJoin(int position, final Ride ride)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.request_to_join));

        // Set up the input
        final EditText editTextMessage = new EditText(this);
        editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextMessage.setPadding(8, 8, 8, 8);
        editTextMessage.setHint(getString(R.string.message));
        builder.setView(editTextMessage);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                requestToJoinRide(editTextMessage.getText().toString(), ride);
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

    public void requestToJoinRide(String message, final Ride ride)
    {
        // make a request
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.sending));
        final RidesAPIController controller = new RidesAPIController(this);
        controller.requestToJoinRide(
                new AuthenticationAPIController(this).getTokken()
                , searchRideParams
                , ride.getId()
                , new GenericSuccessCallback()
                {

                    @Override
                    public void success()
                    {
                        progressDialog.dismiss();
                        Snackbar.make(content, getString(R.string.request_sent), Snackbar.LENGTH_SHORT).show();
                        rideListFragment.setCanRequestToJoin(ride.getId(), false);
                    }

                    @Override
                    public void fail(String message)
                    {
                        progressDialog.dismiss();
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * loads an interstitial ad
     */
    private void loadAd()
    {
        // setup interstitial ad
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ads_id));
        interstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                if (interstitialAd.isLoaded())
                    interstitialAd.show();
            }
        });

        // load the ad
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

    }
}
