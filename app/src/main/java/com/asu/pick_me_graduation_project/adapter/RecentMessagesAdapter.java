package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 3/2/2016.
 */
public class RecentMessagesAdapter extends ArrayAdapter<ChatMessage>
{
    Listener listener2;
    String currentUserId;

    public RecentMessagesAdapter(Context context, int resource, String currentUserId)
    {
        super(context, resource);
        this.currentUserId = currentUserId;
    }

    /**
     * registers to be invoked on onClick callbacks
     */
    public void setListener(Listener listener)
    {
        this.listener2 = listener;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        // inflate view
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_user_chat, parent, false);

        //  reference views
        TextView UserName = (TextView) view.findViewById(R.id.UserName);
        TextView date = (TextView) view.findViewById(R.id.MsgDate);
        TextView LastMSg = (TextView) view.findViewById(R.id.LastMsg);
        final ImageView imageViewPPChat = (ImageView) view.findViewById(R.id.imageViewPPChat);
        final ChatMessage message = getItem(position);
        User otherUser = message.getFrom();
        if (otherUser.getUserId().equals(currentUserId))
            otherUser = message.getTo();

        //  add listener
        final View finalView = view;
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener2 != null)
                    listener2.onClick(position, message, finalView);
            }
        });

        // set data


        UserName.setText(otherUser.getFirstName() + " " + otherUser.getLastName());
        // date.setText((CharSequence) message.getDate().toString());
        long now = System.currentTimeMillis();
        date.setText(DateUtils.getRelativeTimeSpanString(getContext(), message.getDate().getTimeInMillis()));
        // set LastMsg
        LastMSg.setText(getItem(position).getContent());
        if (ValidationUtils.notEmpty(otherUser.getProfilePictureUrl()))
            Picasso.with(getContext()).
                    load(otherUser.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(imageViewPPChat, new Callback()
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


        return view;
    }

    public interface Listener
    {
        void onClick(int position, ChatMessage message, View view);
    }
}
