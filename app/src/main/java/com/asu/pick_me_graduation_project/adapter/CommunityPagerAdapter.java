package com.asu.pick_me_graduation_project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.asu.pick_me_graduation_project.fragment.CommunitiesListFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityInfoFragment;
import com.asu.pick_me_graduation_project.fragment.MembersListFragment;

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
    private List<String> mFragmentTitleList = Arrays.asList("Info", "Members");
    private List<Fragment> mFragmentList = new ArrayList<>();

    public CommunityPagerAdapter(FragmentManager fm)
    {
        super(fm);
        communityInfoFragment = new CommunityInfoFragment();
        membersListFragment = new MembersListFragment();
        mFragmentList = Arrays.asList(communityInfoFragment, membersListFragment);
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
}
