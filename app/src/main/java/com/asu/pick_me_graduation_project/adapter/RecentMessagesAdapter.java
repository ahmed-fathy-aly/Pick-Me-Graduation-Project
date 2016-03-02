package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.asu.pick_me_graduation_project.model.ChatMessage;

/**
 * Created by ahmed on 3/2/2016.
 */
public class RecentMessagesAdapter extends ArrayAdapter<ChatMessage>
{
    public RecentMessagesAdapter(Context context, int resource)
    {
        super(context, resource);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        //TODO - inflate view

        //TODO -  reference views

        //TODO -  set data

        return view;
    }
}
