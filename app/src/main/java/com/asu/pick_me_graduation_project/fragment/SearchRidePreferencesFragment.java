package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.view.CarDetailsView;
import com.asu.pick_me_graduation_project.view.CommunitiesChooserView;
import com.asu.pick_me_graduation_project.view.TimeChooserView;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRidePreferencesFragment extends Fragment
{
        /* UI */
    @Bind(R.id.timeChooserView)
    TimeChooserView timeChooserView;
    @Bind(R.id.communitiesChooserView)
    CommunitiesChooserView communitiesChooserView;
    @Bind(R.id.linearLayoutParent)
    LinearLayout linearLayoutParent;



    public SearchRidePreferencesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_ride_preferences, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /**
     * checks the data entered is valid
     */
    public boolean checkDataEntered()
    {
        boolean valid = true;

        // time and date
        if (!timeChooserView.checkDataEntered())
            valid = false;

        return valid;
    }

    public List<Community> getFilteredCommunities()
    {
        return communitiesChooserView.getCheckedCommunities();
    }

    public Calendar getChpsemTime()
    {
        return timeChooserView.getSelectedTime();
    }
}
