package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.CommunityPagerAdapter;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommunityProfileActivity extends BaseActivity
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

    /* Fields */
    private String communityId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_profile);

        // get community  id
        communityId = getIntent().getExtras().getString(Constants.COMMUNITY_ID);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // setup view pager
        CommunityPagerAdapter adapter = new CommunityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
