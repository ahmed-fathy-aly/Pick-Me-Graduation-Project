package com.asu.pick_me_graduation_project.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.asu.pick_me_graduation_project.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ahmed on 6/6/2016.
 * A view to select and show Time and Date
 */
public class TimeChooserView extends LinearLayout implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    /* UI */
    @Bind(R.id.textViewDate)
    TextView textViewDate;
    @Bind(R.id.imageViewCheckSource)
    ImageView imageViewCheckSource;
    @Bind(R.id.layoutSelectDate)
    LinearLayout layoutSelectDate;
    @Bind(R.id.textViewTime)
    TextView textViewTime;
    @Bind(R.id.imageViewCheckDestination)
    ImageView imageViewCheckDestination;
    @Bind(R.id.layoutSelectTime)
    LinearLayout layoutSelectTime;

    /* fields */
    Calendar calendar;

    /* constructors */

    public TimeChooserView(Context context)
    {
        super(context);
        init();
    }

    public TimeChooserView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TimeChooserView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init()
    {
        // inflate layout
        inflate(getContext(), R.layout.view_time_chooser, this);
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
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
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // format the date
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            String dateStr = formatter.format(calendar.getTime());

            // show
            textViewDate.setText(dateStr);
            textViewDate.setError(null);

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
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);

            // format the date
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            String dateStr = formatter.format(calendar.getTime());

            // show
            textViewTime.setText(dateStr);
            textViewTime.setError(null);
        } catch (Exception e)
        {
            Log.e("Game", "date exception  " + e.getMessage());
        }
    }

    /* methods */

    /**
     * checks that a date and time were entered
     */
    public boolean checkDataEntered()
    {
        boolean valid = true;

        if (textViewDate.getText().length() == 0)
        {
            textViewDate.setError("");
            valid = false;
        }
        if (textViewTime.getText().length() == 0)
        {
            textViewTime.setError("");
            valid = false;
        }

        return valid;
    }

    public Calendar getSelectedTime()
    {
        return  calendar;
    }
}
