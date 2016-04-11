package com.asu.pick_me_graduation_project.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
        name= (TextView) findViewById(R.id.receiverName);
        image= (ImageView) findViewById(R.id.receiverPP);
        User u=new User();
        // u.setFirstName();
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
        adapter=new ChatMessagesAdapter(this,new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId());
        ListViewChat.setAdapter(adapter);


        // load data
        loadMessages();

    }

    private void loadMessages()

    {
        UserApiController controller2 = new UserApiController(getApplicationContext());
        controller2.getProfile(userId, new GetProfileCallback() {
            @Override
            public void success(User user) {
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
            public void fail(String message) {

            }
        });


        progressBar.setVisibility(View.VISIBLE);
        controller.getChatMessages(
                new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , userId
                , new GetMessagesCallback() {

                    @Override
                    public void success(List<ChatMessage> messages) {
                        adapter.addAll(messages);
                    }

                    @Override
                    public void fail(String error) {

                    }
                }

        );
    }
    @OnClick(R.id.fabSend)
    public void onClick()
    {
        final String contentt= MessageEditor.getText().toString();
        controller.sendMessage(contentt, userId, new AuthenticationAPIController(this).getTokken(), new SendMessageCallback() {
            @Override
            public void success(ChatMessage chatMessage2) {

                progressBar.setVisibility(View.INVISIBLE);
                MessageEditor.setText("");

                adapter.add(chatMessage2);


            }

            @Override
            public void fail(String message) {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();

            }
        });

    }


}