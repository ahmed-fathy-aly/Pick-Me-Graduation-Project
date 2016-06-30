package com.asu.pick_me_graduation_project.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.Calendar;

/**
 * Created by ahmed on 6/22/2016.
 */
public class DataPickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            int monthy= month +1;
            String day_ = Integer.toString(day);
            String month_ = Integer.toString(monthy);
            String year_ = Integer.toString(year);

            Constants.dob = month_ + "-" + day_ + "-" + year_;
            Log.d("ADebugTag", "Value: " + Constants.dob);


        }

    }

