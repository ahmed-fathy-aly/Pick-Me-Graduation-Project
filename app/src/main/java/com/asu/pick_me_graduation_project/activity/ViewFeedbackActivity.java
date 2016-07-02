package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.FeedbackViewAdapter;
import com.asu.pick_me_graduation_project.callback.GetFeedbackCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.FeedbackAPIController;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewFeedbackActivity extends BaseActivity
{
    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerViewFeedback)
    RecyclerView recyclerViewFeedback;
    @Bind(R.id.content)
    View content;

    /* fields */
    private String userId;
    private FeedbackAPIController controller;
    private FeedbackViewAdapter adapterFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);
        ButterKnife.bind(this);

        // get data from intent
        userId = getIntent().getStringExtra(Constants.USER_ID);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.feedback));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // setup fields
        controller = new FeedbackAPIController(this);

        // setup recycler view
        adapterFeedback = new FeedbackViewAdapter(this);
        recyclerViewFeedback.setAdapter(adapterFeedback);
        recyclerViewFeedback.setLayoutManager(new LinearLayoutManager(this));

        // load data
        downloadFeedback();


    }

    /**
     * calls the backend to download the feedback
     */
    private void downloadFeedback()
    {
        progressBar.setVisibility(View.VISIBLE);

        controller.getUserFeedback(
                new AuthenticationAPIController(this).getTokken()
                , userId
                , new GetFeedbackCallback()
                {
                    @Override
                    public void success(List<Feedback> feedbackList)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        adapterFeedback.setData(feedbackList);
                    }

                    @Override
                    public void fail(String errorMessage)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(content, errorMessage, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
