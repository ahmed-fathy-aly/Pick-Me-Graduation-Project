package com.asu.pick_me_graduation_project.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.ChatActivity;
import com.asu.pick_me_graduation_project.activity.UserProfileActivity;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ahmed on 6/7/2016.
 */
public class ContactUserView extends LinearLayout
{
    /* UI */
    @Bind(R.id.imageViewPP)
    CircleImageView imageViewPP;
    @Bind(R.id.textViewUserName)
    TextView textViewUserName;
    @Bind(R.id.fabChat)
    FloatingActionButton fabChat;
@Bind(R.id.userContactLayout)
    View userContactLayout;

    /* Constructor */
    public ContactUserView(Context context)
    {
        super(context);
        init();
    }

    public ContactUserView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    public ContactUserView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.view_contact_user, this);
        ButterKnife.bind(this);
    }

    /* methods */

    /**
     * updates the UI with this user
     */
    public void setData(final User user)
    {
        // set data
        textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(getContext()).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(imageViewPP);

        // add listeners
        userContactLayout.setOnClickListener(new DebouncingOnClickListener()
        {
            @Override
            public void doClick(View v)
            {
                // open user profile
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra(Constants.USER_ID, user.getUserId());
                getContext().startActivity(intent);
            }
        });
        fabChat.setOnClickListener(new DebouncingOnClickListener()
        {
            @Override
            public void doClick(View v)
            {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(Constants.USER_ID, user.getUserId());
                getContext().startActivity(intent);
            }
        });
    }
}
