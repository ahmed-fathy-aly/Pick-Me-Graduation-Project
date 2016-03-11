package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.RecentMessagesAdapter;
import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.ChatAPIController;
import com.asu.pick_me_graduation_project.model.ChatMessage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentChatsActivity extends BaseActivity
{

    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.listViewUsersChat)
    ListView ListViewChat;
    LinearLayout content;

    /* fields */
    ChatAPIController controller;
    private RecentMessagesAdapter adapter;

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
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);

        //setup fields
        controller = new ChatAPIController(this);

        //  setup list view and its adapter
        adapter = new RecentMessagesAdapter(this, R.layout.row_user_chat);
        ListViewChat.setAdapter(adapter);


        // load data
        loadMessages();

    }

    private void loadMessages()
    {

        //  get the messages from the controller
        progressBar.setVisibility(View.VISIBLE);
        controller.getRecentChats(
                new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , new GetMessagesCallback()
                {
                    @Override
                    public void success(List<ChatMessage> messages)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.clear();
                        adapter.addAll(messages);

                    }

                    @Override
                    public void fail(String error)
                    {
                        if (error == null)
                            return;
                        progressBar.setVisibility(View.INVISIBLE);

                        // show error
                        Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
                    }
                });


    }
}
