package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.view.CarDetailsView;
import com.asu.pick_me_graduation_project.view.CommunitiesChooserView;
import com.asu.pick_me_graduation_project.view.TimeChooserView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostRidePreferencesFragment extends Fragment
{
        /* UI */
    @Bind(R.id.timeChooserView)
    TimeChooserView timeChooserView;
    @Bind(R.id.communitiesChooserView)
    CommunitiesChooserView communitiesChooserView;
    @Bind(R.id.carDetailsView)
    CarDetailsView carDetailsView;
    @Bind(R.id.linearLayoutParent)
    LinearLayout linearLayoutParent;



    public PostRidePreferencesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_ride_preferences, container, false);
        ButterKnife.bind(this, view);

        // download car details
        carDetailsView.downloadCarDetails();
        return view;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
