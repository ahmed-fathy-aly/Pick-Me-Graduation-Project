package com.asu.pick_me_graduation_project.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asu.pick_me_graduation_project.fragment.CommunityInfoFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityJoinRequestsFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityPostsFragment;
import com.asu.pick_me_graduation_project.fragment.JoinRideRequestsFragment;
import com.asu.pick_me_graduation_project.fragment.MembersListFragment;
import com.asu.pick_me_graduation_project.fragment.RideDetailsFragment;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmed on 3/15/2016.
 */
public class RideDetailsPagerAdapter extends FragmentPagerAdapter
{

    RideDetailsFragment rideDetailsFragment;
    MembersListFragment membersListFragment;
    private List<String> mFragmentTitleList = Arrays.asList("Details", "Members");
    private List<Fragment> mFragmentList = new ArrayList<>();

    public RideDetailsPagerAdapter(FragmentManager fm, String rideId)
    {
        super(fm);

        // the arguments bundle
        Bundle arguments = new Bundle();
        arguments.putString(Constants.RIDE_ID, rideId);

        rideDetailsFragment = new RideDetailsFragment();
        membersListFragment = new MembersListFragment();
        membersListFragment.setArguments(arguments);
        rideDetailsFragment.setArguments(arguments);
        mFragmentList = Arrays.asList(rideDetailsFragment, membersListFragment);
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    public MembersListFragment getMembersListFragment()
    {
        return membersListFragment;
    }

    public RideDetailsFragment getRideDetailsFragment(){ return rideDetailsFragment;}

    public void addRideJoinRequestsFragment(String rideId)
    {
        // make the fragment
        JoinRideRequestsFragment joinRideRequestsFragment = new JoinRideRequestsFragment();
        Bundle arguments = new Bundle();
        arguments.putString(Constants.RIDE_ID, rideId);
        joinRideRequestsFragment.setArguments(arguments);

        // add it to the list
        mFragmentTitleList = Arrays.asList("Details", "Members", "Requests");
        mFragmentList = Arrays.asList(rideDetailsFragment, membersListFragment, joinRideRequestsFragment);
        notifyDataSetChanged();
    }
}
