package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedBackActivity extends AppCompatActivity
{
    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /* fields */
    private String rideId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);

        // get data from intent (later)
        rideId = "2";

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.feedback));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // get data
        getFeedBackForm();

    }

    /**
     * TODO
     * - get the users of that ride
     * - inflate a row for each user
     */
        private void getFeedBackForm()
    {
    }

    /**
     * TODO
     * gather the data as a list of Feedback objects
     * send it to backend
     */
    void sendFeedback()
    {

    }

    /**
     * TODO
     * inflate the view
     * map the user to the view so we can retrieve the view later
     * add the view to the linear layout
     */
    private void inflateRow(User user)
    {

    }
}
