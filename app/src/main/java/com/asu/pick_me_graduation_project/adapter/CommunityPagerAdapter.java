package com.asu.pick_me_graduation_project.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.asu.pick_me_graduation_project.fragment.CommunitiesListFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityInfoFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityJoinRequestsFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityPostsFragment;
import com.asu.pick_me_graduation_project.fragment.MembersListFragment;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmed on 3/15/2016.
 */
public class CommunityPagerAdapter extends FragmentPagerAdapter
{

    CommunityInfoFragment communityInfoFragment;
    MembersListFragment membersListFragment;
    CommunityPostsFragment communityPostsFragment;
    CommunityJoinRequestsFragment communityJoinRequestsFragment;
    private List<String> mFragmentTitleList = Arrays.asList("Info", "Posts", "Members", "Requests");
    private List<Fragment> mFragmentList = new ArrayList<>();

    public CommunityPagerAdapter(FragmentManager fm, String communityId)
    {
        super(fm);

        // the arguments bundle
        Bundle arguments = new Bundle();
        arguments.putString(Constants.COMMUNITY_ID, communityId);

        communityInfoFragment = new CommunityInfoFragment();
        communityInfoFragment.setArguments(arguments);
        communityPostsFragment = new CommunityPostsFragment();
        communityPostsFragment.setArguments(arguments);
        membersListFragment = new MembersListFragment();
        membersListFragment.setArguments(arguments);
        communityJoinRequestsFragment = new CommunityJoinRequestsFragment();
        communityJoinRequestsFragment.setArguments(arguments);
        mFragmentList = Arrays.asList(communityInfoFragment, communityPostsFragment, membersListFragment, communityJoinRequestsFragment);
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

    public CommunityInfoFragment getInfoFragment()
    {
        return communityInfoFragment;
    }
}
