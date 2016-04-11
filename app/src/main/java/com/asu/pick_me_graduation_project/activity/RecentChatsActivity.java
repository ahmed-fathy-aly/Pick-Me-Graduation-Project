package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
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

import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentChatsActivity extends BaseActivity implements RecentMessagesAdapter.Listener
{

    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.listViewUsersChat)
    ListView ListViewChat;
    @Bind(R.id.content)
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
        toolbar.setTitle(getString(R.string.title_messages));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_messages));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);

        //setup fields
        controller = new ChatAPIController(this);

        //  setup list view and its adapter
        adapter = new RecentMessagesAdapter(this, R.layout.row_user_chat);
        adapter.setListener(this);
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
                , new GetMessagesCallback() {
                    @Override
                    public void success(List<ChatMessage> messages) {
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.clear();
                        adapter.addAll(messages);

                    }

                    @Override
                    public void fail(String error) {
                        if (error == null)
                            return;
                        progressBar.setVisibility(View.INVISIBLE);

                        // show error
                        Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void onClick(int position, ChatMessage message, View view) {
        // go to the user profile activity
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.USER_ID, message.getTo().getUserId());
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        android.support.v4.util.Pair.create(view, getString(R.string.transition_recentChat_list_to_chat))
                );
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}

