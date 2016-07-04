package com.asu.pick_me_graduation_project.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.ChatMessagesAdapter;
import com.asu.pick_me_graduation_project.callback.GetMessagesCallback;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.callback.SendMessageCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.ChatAPIController;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.events.NewMessageEvent;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.GenericMapsView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.playlog.internal.LogEvent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.MessageEditor)
    EditText MessageEditor;
    @Bind(R.id.fabSend)
    FloatingActionButton fabSend;

    /* fields */
    String userId;
    ChatAPIController controller;
    private ChatMessagesAdapter adapter;
    TextView name;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // reference views
        ButterKnife.bind(this);
        name = (TextView) findViewById(R.id.receiverName);
        image = (ImageView) findViewById(R.id.receiverPP);
        User u = new User();

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);
        //name.setText();

        // get extras
        userId = getIntent().getStringExtra(Constants.USER_ID);


        // setup fields
        controller = new ChatAPIController(this);


        // TODO - setup list view and its adapter
        adapter = new ChatMessagesAdapter(this, new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId());
        ListViewChat.setAdapter(adapter);


        // load data
        loadMessages();

        // register for new message event
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ((MyApplication) getApplication()).currentChat = userId;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ((MyApplication) getApplication()).currentChat = userId;
    }

    private void loadMessages()
    {
        UserApiController controller2 = new UserApiController(getApplicationContext());
        controller2.getProfile(userId, new GetProfileCallback()
        {
            @Override
            public void success(User user)
            {
                name.setText(ValidationUtils.correct(user.getFirstName()) + " " + ValidationUtils.correct(user.getLastName()));
                if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
                    Picasso.with(getApplicationContext()).
                            load(user.getProfilePictureUrl())
                            .placeholder(R.drawable.ic_user_small)
                            .into(image, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                {
                                }

                                @Override
                                public void onError()
                                {

                                }
                            });

            }

            @Override
            public void fail(String message)
            {

            }
        });


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
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.addAll(messages);

                        // scroll to the bottom
                        ListViewChat.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ListViewChat.smoothScrollToPosition(adapter.getCount() - 1);
                            }
                        }, 100);
                    }

                    @Override
                    public void fail(String error)
                    {
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }

        );
    }

    @OnClick(R.id.fabSend)
    public void onClick()
    {
        final String contentt = MessageEditor.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        fabSend.setEnabled(false);
        controller.sendMessage(
                contentt,
                "",
                userId,
                new AuthenticationAPIController(this).getTokken(), new SendMessageCallback()
                {
                    @Override
                    public void success(ChatMessage chatMessage2)
                    {

                        progressBar.setVisibility(View.INVISIBLE);
                        fabSend.setEnabled(true);
                        MessageEditor.setText("");

                        addMessage(chatMessage2);

                    }

                    @Override
                    public void fail(String message)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        fabSend.setEnabled(true);
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();

                    }
                });

    }

    @OnClick(R.id.fabLocationSend)
    public void onclick()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick your Location");
        final GenericMapsView yourMap = new GenericMapsView(this);
        yourMap.goToMyLocation();
        builder.setView(yourMap);

        builder.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                final LatLng chosenLocation = yourMap.getCurrentLatlng();
                String message = getString(R.string.sent_a_location);
                String extras = ChatMessage.getExtrasLatlng(chosenLocation);

                progressBar.setVisibility(View.VISIBLE);
                controller.sendMessage(
                        message,
                        extras,
                        userId,
                        new AuthenticationAPIController(ChatActivity.this).getTokken(),
                        new SendMessageCallback()
                        {
                            @Override
                            public void success(ChatMessage chatMessage3)
                            {

                                progressBar.setVisibility(View.INVISIBLE);
                                addMessage(chatMessage3);


                            }

                            @Override
                            public void fail(String message)
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewMessageEvent newMessageEvent)
    {
        // check it's for this user
        if (!newMessageEvent.getUserId().equals(userId))
            return;

        // add the message to the adapter if it's not there
        ChatMessage newMessage = newMessageEvent.getChatMessage();
        addMessage(newMessage);
    }

    private void addMessage(ChatMessage newMessage)
    {
        if (!adapter.contains(newMessage.getId()))
            adapter.add(newMessage);

    }


}