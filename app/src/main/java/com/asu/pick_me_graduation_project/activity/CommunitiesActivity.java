package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.fragment.CommunitiesListFragment;
import com.asu.pick_me_graduation_project.model.Community;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommunitiesActivity extends BaseActivity
{

    /* UI */
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content)
    View content;
    @Bind(R.id.fragmentCommunitiesListContent)
    View fragmentCommunitiesListContent;
    @Bind(R.id.textViewMyCommunities)
    TextView textViewMyCommunities;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    CommunitiesListFragment communitiesListFragment;
    @Bind(R.id.editTextSearch)
    EditText editTextSearch;

    /* fields */
    private CommunityAPIController controller;

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
        editTextSearch.setVisibility(View.VISIBLE);
        editTextSearch.setHint(getString(R.string.search_community));

        // setup fields
        controller = new CommunityAPIController(this);

        // setup communities list fragment
        communitiesListFragment = new CommunitiesListFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragmentCommunitiesListContent, communitiesListFragment)
                .commit();

        
        // get data
        getMyCommunities();
    }



    /**
     * asks the backend to get my communities
     */
    private void getMyCommunities()
    {
        progressBar.setVisibility(View.VISIBLE);
        controller.getMyCommunities(new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , new GetCommunitiesCallback()
        {
            @Override
            public void success(List<Community> communityList)
            {
                progressBar.setVisibility(View.GONE);

                // show the communities in the fragment
                textViewMyCommunities.setVisibility(View.VISIBLE);
                if (communitiesListFragment != null)
                    communitiesListFragment.setData(communityList);
            }

            @Override
            public void fail(String errorMessage)
            {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(content, errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.fab)
    public void onClick()
    {
        // open the create community fragment
        CreateCommunityFragment createCommunityFragment = new CreateCommunityFragment();
        createCommunityFragment.show(getSupportFragmentManager(), getString(R.string.title_create_community));
    }
}