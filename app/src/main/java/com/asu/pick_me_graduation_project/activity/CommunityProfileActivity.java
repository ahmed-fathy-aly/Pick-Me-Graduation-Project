package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.CommunityPagerAdapter;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

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
    @Bind(R.id.content)
    View content;

    /* Fields */
    private String communityId;
    private CommunityAPIController contoller;
    private CommunityPagerAdapter communityPagerAdapter;

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
        contoller = new CommunityAPIController(this);

        // setup view pager
        communityPagerAdapter = new CommunityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(communityPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // load data
        loadMembers();

    }

    private void loadMembers()
    {
        contoller.getCommunityMembers(
                new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , communityId, new GetUsersCallback()
                {

                    @Override
                    public void success(List<User> users)
                    {
                        communityPagerAdapter.getMembersListFragment().setMembers(users);
                    }

                    @Override
                    public void fail(String message)
                    {
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}
