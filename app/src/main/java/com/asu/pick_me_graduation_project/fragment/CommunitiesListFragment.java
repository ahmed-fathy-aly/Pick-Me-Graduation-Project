package com.asu.pick_me_graduation_project.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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

    /* fields */
    private CommunitiesAdapter adapterCommunities;

    public CommunitiesListFragment()
    {
        // Required empty public constructor
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
    public void onJoinClicked(int position, Community community)
    {

        // open the join request dialog
        JoinCommunityRequestFragment joinCommunityRequestFragment = new JoinCommunityRequestFragment();
        Bundle arguments = new Bundle();
        arguments.putString(Constants.COMMUNITY_ID, community.getId());
        arguments.putString(Constants.COMMUNITY_NAME, community.getName());
        joinCommunityRequestFragment.setArguments(arguments);
        joinCommunityRequestFragment.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), getString(R.string.title_request_to_join));
    }

    @Override
    public void onCommunityClicked(int position, Community community, View v)
    {
        Intent intent = new Intent(getContext(), CommunityProfileActivity.class);
        intent.putExtra(Constants.COMMUNITY_ID, community.getId());
        startActivity(intent);
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
