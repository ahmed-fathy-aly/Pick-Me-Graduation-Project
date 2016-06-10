package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

import com.asu.pick_me_graduation_project.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * blank activity :D
 */
public class MainActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setupNavigationBar(this, toolbar);
        toolbar.setTitle("Dashboard");

        // open drawer
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                drawer.openDrawer();
            }
        }, 700);

        // load ads
        loadAd();
    }

    private void loadAd()
    {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @OnClick(R.id.buttonPostRide)
    void openPostRide()
    {
        startActivity(new Intent(this, PostRideActivity.class));
    }

    @OnClick(R.id.buttonSearchRide)
    void openSearchRide()
    {
        startActivity(new Intent(this, SearchRideActivity.class));
    }

}
