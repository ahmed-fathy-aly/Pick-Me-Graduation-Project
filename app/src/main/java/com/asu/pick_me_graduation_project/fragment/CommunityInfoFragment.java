package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommunityInfoFragment extends Fragment
{

    @Bind(R.id.textViewDescription)
    TextView textViewDescription;

    public CommunityInfoFragment()
    {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_info, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setDetails(String description)
    {
        if (textViewDescription == null)
            return;

        if (description == null || description.length() == 0)
            textViewDescription.setText(R.string.no_description_added);
        else
            textViewDescription.setText(description);
    }
}
