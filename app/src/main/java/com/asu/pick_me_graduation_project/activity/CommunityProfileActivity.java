package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.CommunityPagerAdapter;
import com.asu.pick_me_graduation_project.callback.GetCommunityCallback;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.events.UpdateCommunityMembersEvent;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.OnPageSelectedListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommunityProfileActivity extends BaseActivity
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
    @Bind(R.id.textViewCommunityName)
    TextView textViewCommunityName;
    @Bind(R.id.imageViewCommunityPP)
    ImageView imageViewCommunityPP;

    /* Fields */
    private String communityId;
    private CommunityAPIController contoller;
    private CommunityPagerAdapter communityPagerAdapter;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_profile);

        // get community  id
        communityId = getIntent().getExtras().getString(Constants.COMMUNITY_ID);
        isAdmin = getIntent().getExtras().getBoolean(Constants.IS_COMMUNITY_ADMIN, false);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        contoller = new CommunityAPIController(this);

        // load data
        loadInfo();

        // register for community update event
        EventBus.getDefault().register(this);

        // view pager listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                try
                {
                    OnPageSelectedListener child = (OnPageSelectedListener) communityPagerAdapter.getItem(position);
                    child.onSelected();
                } catch (Exception e)
                {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * loads the community profile from backend
     */
    private void loadInfo()
    {
        progressBar.setVisibility(View.VISIBLE);

        contoller.getCommunityProfile(
                new AuthenticationAPIController(this).getTokken()
                , communityId
                , new GetCommunityCallback()
                {
                    @Override
                    public void success(Community community)
                    {
                        progressBar.setVisibility(View.GONE);

                        // popoulate views
                        textViewCommunityName.setText(community.getName());
                        if (ValidationUtils.notEmpty(community.getProfilePictureUrl()))
                            Picasso.with(getApplicationContext()).
                                    load(community.getProfilePictureUrl())
                                    .placeholder(R.drawable.ic_user_small)
                                    .into(imageViewCommunityPP);


                        // setup view pager
                        communityPagerAdapter = new CommunityPagerAdapter(getSupportFragmentManager(), communityId, isAdmin);
                        viewPager.setAdapter(communityPagerAdapter);
                        viewPager.setOffscreenPageLimit(4);
                        tabLayout.setupWithViewPager(viewPager);

                        // check if should swtich to the requests tab
                        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getBoolean(SWITCH_TO_REQUEST_TAB, false))
                            if (viewPager.getChildCount() == 4)
                                viewPager.setCurrentItem(3, true);


                        // populate the info fragment
                        communityPagerAdapter.getInfoFragment().setDetails(ValidationUtils.correct(community.getDescription()));

                        loadMembers();
                    }

                    @Override
                    public void fail(String message)
                    {
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );

    }

    /**
     * loads the community members from the backend
     */
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateCommunityMembersEvent updateCommunityMembersEvent)
    {
        loadMembers();
    }


}

