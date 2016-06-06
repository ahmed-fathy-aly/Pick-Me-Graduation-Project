package com.asu.pick_me_graduation_project.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Ride;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 6/6/2016.
 */
public class ExtraRideDetailsView extends LinearLayout
{
    /* UI */
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.editTextFreeSeats)
    EditText editTextFreeSeats;
    @Bind(R.id.checkBoxNoSmoking)
    CheckBox checkBoxNoSmoking;
    @Bind(R.id.checkBoxLadiesOnly)
    CheckBox checkBoxLadiesOnly;
    @Bind(R.id.editTextNotes)
    EditText editTextNotes;

    /* Constructors */

    public ExtraRideDetailsView(Context context)
    {
        super(context);
        init();
    }

    public ExtraRideDetailsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ExtraRideDetailsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        // inflate layout
        inflate(getContext(), R.layout.view_extra_ride_details, this);
        ButterKnife.bind(this);
    }

    /* methods */

    /**
     * collects the data from the UI
     */
    public void fillRideInfo(Ride ride)
    {
        ride.setDescription(editTextDescription.getText().toString());
        ride.setNotes(editTextNotes.getText().toString());
        try
        {
            ride.getRideDetails().setNumberOfFreeSeats(Integer.parseInt(editTextFreeSeats.getText().toString()));
        } catch (Exception e)
        {
        }
        ride.getRideDetails().setNoSmoking(checkBoxNoSmoking.isChecked());
        ride.getRideDetails().setLadiesOnly(checkBoxLadiesOnly.isChecked());
    }

    /**
     * checks the data entered is valid
     */
    public boolean checkDataEntered()
    {
        boolean valid = true;

        EditText[] editTexts = {editTextDescription, editTextFreeSeats, editTextNotes};
        for (EditText editText : editTexts)
            if (editText.getText().toString().trim().length() == 0)
            {
                editText.setError("");
                valid = false;
            }

        return valid;
    }
}
