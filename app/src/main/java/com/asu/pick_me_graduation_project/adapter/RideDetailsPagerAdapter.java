package com.asu.pick_me_graduation_project.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.asu.pick_me_graduation_project.fragment.CommunityInfoFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityJoinRequestsFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityPostsFragment;
import com.asu.pick_me_graduation_project.fragment.JoinRideRequestsFragment;
import com.asu.pick_me_graduation_project.fragment.MembersListFragment;
import com.asu.pick_me_graduation_project.fragment.RideAnnouncmentsFragment;
import com.asu.pick_me_graduation_project.fragment.RideDetailsFragment;
import com.asu.pick_me_graduation_project.model.JoinRideRequest;
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
    JoinRideRequestsFragment joinRideRequestsFragment;
    RideAnnouncmentsFragment rideAnnouncmentsFragment;
    private List<String> mFragmentTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    public RideDetailsPagerAdapter(FragmentManager fm, String rideId, boolean isMember, boolean isAdmin)
    {
        super(fm);

        // the arguments bundle
        Bundle arguments = new Bundle();
        arguments.putString(Constants.RIDE_ID, rideId);

        rideDetailsFragment = new RideDetailsFragment();
        rideDetailsFragment.setArguments(arguments);
        mFragmentTitleList.add("Details");
        mFragmentList.add(rideDetailsFragment);

        membersListFragment = new MembersListFragment();
        membersListFragment.setArguments(arguments);
        mFragmentTitleList.add("Members");
        mFragmentList.add(membersListFragment);

        if (isMember)
        {
            rideAnnouncmentsFragment = new RideAnnouncmentsFragment();
            rideAnnouncmentsFragment.setArguments(arguments);
            mFragmentTitleList.add("Announcements");
            mFragmentList.add(rideAnnouncmentsFragment);
        }

        if (isAdmin)
        {
            joinRideRequestsFragment = new JoinRideRequestsFragment();
            joinRideRequestsFragment.setArguments(arguments);
            mFragmentTitleList.add("Requests");
            mFragmentList.add(joinRideRequestsFragment);
        }
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

    public RideDetailsFragment getRideDetailsFragment()
    {
        return rideDetailsFragment;
    }

    public RideAnnouncmentsFragment getRideAnnouncmentsFragment()
    {
        return rideAnnouncmentsFragment;
    }
}
