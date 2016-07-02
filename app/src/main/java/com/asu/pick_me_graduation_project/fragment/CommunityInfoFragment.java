package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommunityInfoFragment extends Fragment
{

    @Bind(R.id.textViewDescription)
    TextView textViewDescription;

    public CommunityInfoFragment()
    {
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_info, container, false);
        ButterKnife.bind(this, view);
        String description = getDescriptionFromArguments();
        if (description != null)
            updateUi(description);
        return view;
    }

    public void setDetails(String description)
    {
        getArguments().putString(Constants.DESCRIPTION, description);
        if (isAdded())
            updateUi(description);
    }


    /**
     * @return null if no description found in arguments
     */
    private String getDescriptionFromArguments()
    {
        if (getArguments() == null || !getArguments().containsKey(Constants.DESCRIPTION))
            return null;
        return getArguments().getString(Constants.DESCRIPTION);
    }

    private void updateUi(String description)
    {
        if (description == null || description.length() == 0)
            textViewDescription.setText(R.string.no_description_added);
        else
            textViewDescription.setText(description);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
