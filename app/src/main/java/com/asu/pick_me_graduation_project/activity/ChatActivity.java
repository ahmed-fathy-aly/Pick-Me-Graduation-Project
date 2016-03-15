package com.asu.pick_me_graduation_project.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.ChatMessagesAdapter;
import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.ChatAPIController;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity
{
    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.listViewChat)
    ListView ListViewChat;

    /* fields */
    String userId;
    ChatAPIController controller;
    private ChatMessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);

        // get extras
        userId = getIntent().getStringExtra(Constants.USER_ID);

        // setup fields
        controller = new ChatAPIController(this);

        // TODO - setup list view and its adapter
        adapter=new ChatMessagesAdapter(this,new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId());
         ListViewChat.setAdapter(adapter);

        // load data
        loadMessages();

    }

    private void loadMessages()
    {
        progressBar.setVisibility(View.VISIBLE);
        controller.getChatMessages(
                new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , userId
                , new GetMessagesCallback()
                {

                    @Override
                    public void success(List<ChatMessage> messages)
                    {
                    adapter.addAll(messages);
                    }

                    @Override
                    public void fail(String error)
                    {

                    }
                }

        );
    }
}
