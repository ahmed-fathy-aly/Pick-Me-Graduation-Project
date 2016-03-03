package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunitiesActivity extends BaseActivity
{

    /* UI */
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communities);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @OnClick(R.id.fab)
    public void onClick()
    {
        // open the create community fragment
        CreateCommunityFragment createCommunityFragment = new CreateCommunityFragment();
        createCommunityFragment.show(getSupportFragmentManager(), getString(R.string.title_create_community));
    }
}