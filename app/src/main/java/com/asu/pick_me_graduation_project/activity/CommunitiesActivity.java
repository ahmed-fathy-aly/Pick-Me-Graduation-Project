package com.asu.pick_me_graduation_project.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
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
    @Bind(R.id.fab)
    FloatingActionButton fab;

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

        // text watcher for search edit text
        editTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() >= 1)
                    searchCommunities(s.toString());
                else
                    getMyCommunities();
            }
        });

        // get data
        getMyCommunities();

        // animate fab
        fab.post(new Runnable()
        {
            @Override
            public void run()
            {
                fab.show();
            }
        });
    }

    /**
     * asks the backend to search for all communities with that substring
     */
    private void searchCommunities(String searchString)
    {
        progressBar.setVisibility(View.VISIBLE);
        controller.searchCommunities(new AuthenticationAPIController(this).getTokken()
                , searchString
                , new GetCommunitiesCallback()
        {
            @Override
            public void success(List<Community> communityList)
            {
                progressBar.setVisibility(View.GONE);

                // show the communities in the fragment
                textViewMyCommunities.setVisibility(View.GONE);
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

    /**
     * asks the backend to get my communities
     */
    private void getMyCommunities()
    {
        progressBar.setVisibility(View.VISIBLE);
        controller.getMyCommunities(new AuthenticationAPIController(this).getTokken()
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
        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_create_community));

        // Set up the input
        View view = LayoutInflater.from(this).inflate(R.layout.fragment_create_community, null);
        final EditText editTextCommunityName = (EditText) view.findViewById(R.id.editTextCommunityName);
        final EditText editTextCommunityDescription = (EditText) view.findViewById(R.id.editTextCommunityDescription);
        builder.setView(view);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String name = editTextCommunityName.getText().toString();
                String description = editTextCommunityDescription.getText().toString();
                createCommunity(name, description);
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

        // show the dialog
        builder.show();

    }

    /**
     * asks the backend to create a new community
     */
    private void createCommunity(String name, String description)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getString(R.string.creating_community));
        controller.createCommunity(
                new AuthenticationAPIController(this).getTokken(),
                name,
                description,
                new CreateCommunityCallback()
                {
                    @Override
                    public void success(Community community)
                    {
                        progressDialog.dismiss();

                        // show succes
                        Toast.makeText(CommunitiesActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();

                        // add the new community
                        communitiesListFragment.addCommunity(community);
                    }

                    @Override
                    public void fail(String message)
                    {
                        // show error
                        progressDialog.dismiss();
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

}