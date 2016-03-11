package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.ChatMessage;

/**
 * Created by ahmed on 3/11/2016.
 */
public class ChatMessagesAdapter extends ArrayAdapter<ChatMessage>
{
    public ChatMessagesAdapter(Context context)
    {
        // TODO - replace with your layout
        super(context, R.layout.row_user);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // TODO - infalte

        // TODO - reference view

        // TODO - set data

        return view;
    }
}
