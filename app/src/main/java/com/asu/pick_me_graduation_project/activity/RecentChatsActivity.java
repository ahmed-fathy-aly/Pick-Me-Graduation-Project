package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.RecentMessagesAdapter;
import com.asu.pick_me_graduation_project.controller.ChatAPIController;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentChatsActivity extends BaseActivity
{

    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /* fields */
    ChatAPIController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_chats);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup fields
        controller = new ChatAPIController(this);

        // TODO - setup list view and its adapter


        // load data
        loadMessages();

    }

    private void loadMessages()
    {
        progressBar.setVisibility(View.VISIBLE);

        // TODO - get the messages from the controller
    }
}
