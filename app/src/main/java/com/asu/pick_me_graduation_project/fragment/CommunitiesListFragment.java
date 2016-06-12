package com.asu.pick_me_graduation_project.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.CommunityProfileActivity;
import com.asu.pick_me_graduation_project.activity.CreateCommunityFragment;
import com.asu.pick_me_graduation_project.adapter.CommunitiesAdapter;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunitiesListFragment extends android.support.v4.app.Fragment implements CommunitiesAdapter.Listener
{

    /* UI */
    @Bind(R.id.listViewCommunities)
    ListView listViewCommunities;
    @Bind(R.id.content)
    View content;

    /* fields */
    private CommunitiesAdapter adapterCommunities;
    private CommunityAPIController controller;

    public CommunitiesListFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new CommunityAPIController(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_communities_list, container, false);
        ButterKnife.bind(this, view);

        // setup adapter
        adapterCommunities = new CommunitiesAdapter(getContext());
        adapterCommunities.setListener(this);
        listViewCommunities.setAdapter(adapterCommunities);

        return view;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /* listeners */
    @Override
    public void onJoinClicked(int position, final Community community)
    {
        // make a request to join
        adapterCommunities.markRequestSending(community.getId());
        controller.requestToJoinCommunity(
                new AuthenticationAPIController(getContext()).getTokken()
                , community.getId()
                , new GenericSuccessCallback() {

                    @Override
                    public void success() {
                        Snackbar.make(content, getString(R.string.sent), Snackbar.LENGTH_SHORT).show();
                        adapterCommunities.markRequestSent(community.getId());
                    }

                    @Override
                    public void fail(String message) {
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );

    }

    @Override
    public void onCommunityClicked(int position, Community community, View v)
    {
        if (community.isMember())
        {
            Intent intent = new Intent(getContext(), CommunityProfileActivity.class);
            intent.putExtra(Constants.COMMUNITY_ID, community.getId());
            intent.putExtra(Constants.IS_COMMUNITY_ADMIN, community.isAdmin());
            startActivity(intent);
        }
    }

    /* methods */

    /**
     * shows that data in the list
     */
    public void setData(List<Community> communityList)
    {
        adapterCommunities.clear();
        adapterCommunities.addAll(communityList);
    }

}
