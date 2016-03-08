package com.asu.pick_me_graduation_project.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.CommunitiesAdapter;
import com.asu.pick_me_graduation_project.model.Community;

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
        Log.e("Game", "join " + community.getName());
    }

    @Override
    public void onCommunityClicked(int position, Community community, View v)
    {
        Log.e("Game", "click " + community.getName());
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
