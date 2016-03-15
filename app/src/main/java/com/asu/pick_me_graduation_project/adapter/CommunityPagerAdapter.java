package com.asu.pick_me_graduation_project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;

import com.asu.pick_me_graduation_project.fragment.CommunitiesListFragment;
import com.asu.pick_me_graduation_project.fragment.CommunityInfoFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmed on 3/15/2016.
 */
public class CommunityPagerAdapter extends FragmentPagerAdapter
{
    private final List<String> mFragmentTitleList = Arrays.asList("Info", "Members", "Posts");

    public CommunityPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return new CommunityInfoFragment();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return mFragmentTitleList.get(position);
    }

    @Override
    public int getCount()
    {
        return mFragmentTitleList.size();
    }
}
