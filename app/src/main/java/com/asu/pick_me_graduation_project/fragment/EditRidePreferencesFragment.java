package com.asu.pick_me_graduation_project.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetCommunitiesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditRidePreferencesFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{

    /* UI */
    @Bind(R.id.textViewDate)
    TextView textViewDate;
    @Bind(R.id.imageViewCheckSource)
    ImageView imageViewCheckSource;
    @Bind(R.id.textViewTime)
    TextView textViewTime;
    @Bind(R.id.imageViewCheckDestination)
    ImageView imageViewCheckDestination;
    @Bind(R.id.radioGroupVisibility)
    RadioGroup radioGroupVisibility;
    @Bind(R.id.linearLayoutCommunities)
    LinearLayout linearLayoutCommunities;

    /* fields */
    HashSet<Community> filteredCommunities;

    public EditRidePreferencesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_ride_preferences, container, false);
        ButterKnife.bind(this, view);

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

        // load my communities
        getMyCommunities();
        return view;
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    /* listeners */

    @OnClick(R.id.layoutSelectDate)
    void selectDate()
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext()
                , this
                , now.get(Calendar.YEAR)
                , now.get(Calendar.MONTH)
                , now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();

    }

    @OnClick(R.id.layoutSelectTime)
    void selectTime()
    {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext()
                , this
                , now.get(Calendar.HOUR_OF_DAY)
                , now.get(Calendar.MINUTE)
                , false
        );
        timePickerDialog.show();

    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        try
        {
            // make a calendar
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // format the date
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            String dateStr = formatter.format(calendar.getTime());

            // show
            textViewDate.setText(dateStr);

        } catch (Exception e)
        {
            Log.e("Game", "date exception  " + e.getMessage());
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {

        try
        {
            // make a calendar
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            // format the date
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            String dateStr = formatter.format(calendar.getTime());

            // show
            textViewTime.setText(dateStr);

        } catch (Exception e)
        {
            Log.e("Game", "date exception  " + e.getMessage());
        }
    }

    /* methods */

    private void getMyCommunities()
    {
        filteredCommunities = new HashSet<>();
        //linearLayoutCommunities.removeAllViews();
        CommunityAPIController controller = new CommunityAPIController(getContext());
        controller.getMyCommunities(
                new AuthenticationAPIController(getContext()).getTokken()
                , new GetCommunitiesCallback()
                {
                    @Override
                    public void success(List<Community> communityList)
                    {
                        // add to the linear layout
                        for (final Community community : communityList)
                        {
                            // reference views
                            View view =  LayoutInflater.from(getContext()).inflate(R.layout.row_community_check, null);
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

                    @Override
                    public void fail(String errorMessage)
                    {
                        Log.e("Game", "error getting communities " + errorMessage);
                    }
                }
        );
    }
}
