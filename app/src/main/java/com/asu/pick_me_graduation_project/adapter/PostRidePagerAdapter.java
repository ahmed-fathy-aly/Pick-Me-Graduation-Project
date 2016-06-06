package com.asu.pick_me_graduation_project.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asu.pick_me_graduation_project.fragment.ChooseRouteFragment;
import com.asu.pick_me_graduation_project.fragment.PostRidePreferencesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ahmed on 3/15/2016.
 */
public class PostRidePagerAdapter extends FragmentPagerAdapter
{

    Fragment chooseRouteFragment;
    Fragment editRidePreferencesFragment;
    private List<Fragment> mFragmentList = new ArrayList<>();

    public PostRidePagerAdapter(FragmentManager fm)
    {
        super(fm);

        chooseRouteFragment = new ChooseRouteFragment();
        editRidePreferencesFragment = new PostRidePreferencesFragment();
        mFragmentList = Arrays.asList(chooseRouteFragment, editRidePreferencesFragment);
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount()
    {
        return mFragmentList.size();
    }

    public ChooseRouteFragment getChooseRouteFragment()
    {
        return (ChooseRouteFragment) chooseRouteFragment;
    }

    public PostRidePreferencesFragment getPostRidePreferencesFragment()
    {
        return (PostRidePreferencesFragment) editRidePreferencesFragment;
    }
}
