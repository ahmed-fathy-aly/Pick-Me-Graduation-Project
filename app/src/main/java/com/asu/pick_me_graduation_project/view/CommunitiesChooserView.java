package com.asu.pick_me_graduation_project.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 6/6/2016.
 */
public class CommunitiesChooserView extends LinearLayout
{
    /* UI */
    @Bind(R.id.radioGroupVisibility)
    RadioGroup radioGroupVisibility;
    @Bind(R.id.linearLayoutCommunities)
    LinearLayout linearLayoutCommunities;

    /* fields */
    HashSet<Community> filteredCommunities;

    /* Constructors */
    public CommunitiesChooserView(Context context)
    {
        super(context);
        init();
    }

    public CommunitiesChooserView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public CommunitiesChooserView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        // inflate layout
        inflate(getContext(), R.layout.view_communities_chooser, this);
        ButterKnife.bind(this);

        // setup communities radio group
        radioGroupVisibility.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.radioButtonAll)
                {
                    linearLayoutCommunities.setVisibility(View.GONE);
                } else
                {
                    linearLayoutCommunities.setVisibility(View.VISIBLE);
                }
            }
        });

        // download communities
        getMyCommunities();
    }

    /* methods */

    /**
     * downloads my communities to put them in the list(Linear layout)
     */
    private void getMyCommunities()
    {
        CommunityAPIController controller = new CommunityAPIController(getContext());
        controller.getMyCommunities(
                new AuthenticationAPIController(getContext()).getTokken()
                , new GetCommunitiesCallback()
                {
                    @Override
                    public void success(List<Community> communityList)
                    {
                        inflateCommunities(communityList);
                    }

                    @Override
                    public void fail(String errorMessage)
                    {
                        Log.e("Game", "error getting communities " + errorMessage);
                    }
                }
        );
    }

    /**
     * adds the communities to the linear layout
     */
    private void inflateCommunities(List<Community> communityList)
    {
        // clear previous views
        linearLayoutCommunities.removeAllViews();
        filteredCommunities = new HashSet<>();

        // add each community as a row in the linear layout
        for (final Community community : communityList)
        {
            // reference views
            View view = LayoutInflater.from(getContext()).inflate(R.layout.row_community_check, null);
            TextView textViewCommunityName = (TextView) view.findViewById(R.id.textViewCommunityName);
            CheckBox checkBoxCommunityChecked = (CheckBox) view.findViewById(R.id.checkBoxCommunityChecked);
            ImageView imageViewPP = (ImageView) view.findViewById(R.id.imageViewPP);

            // load data
            textViewCommunityName.setText(community.getName());
            if (ValidationUtils.notEmpty(community.getProfilePictureUrl()))
                Picasso.with(getContext()).
                        load(community.getProfilePictureUrl())
                        .placeholder(R.drawable.ic_group_black_48dp)
                        .into(imageViewPP);

            // add listener
            filteredCommunities.add(community);
            checkBoxCommunityChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                        filteredCommunities.add(community);
                    else
                        filteredCommunities.remove(community);
                }
            });

            // add to layout
            linearLayoutCommunities.addView(view);
        }
    }

    /**
     * returns the communities checked or null if the user choosed "All"
     */
    public List<Community> getCheckedCommunities()
    {
        if (radioGroupVisibility.getCheckedRadioButtonId() == R.id.radioButtonAll)
            return null;
        else
            return new ArrayList<>(filteredCommunities);
    }
}
