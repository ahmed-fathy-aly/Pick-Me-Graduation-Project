package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.koushikdutta.ion.future.ResponseFuture;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ahmed on 3/8/2016.
 */
public class CommunitiesAdapter extends ArrayAdapter<Community>
{
    /* fields */
    Listener listener;

    public CommunitiesAdapter(Context context)
    {
        super(context, R.layout.row_community);
    }

    /**
     * registeers to be invoked with callbacks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_community, parent, false);
        final View finalView = view;

        // reference view
        ImageView imageViewPP = (ImageView) view.findViewById(R.id.imageViewPP);
        TextView textViewCommunityName = (TextView) view.findViewById(R.id.textViewCommunityName);
        Button buttonJoin = (Button) view.findViewById(R.id.buttonJoin);

        // set listeners
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                    listener.onCommunityClicked(position, getItem(position), finalView);
            }
        });
        buttonJoin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                    listener.onJoinClicked(position, getItem(position));
            }
        });


        // set data
        Community community = getItem(position);
        textViewCommunityName.setText(community.getName());
        if (ValidationUtils.notEmpty(community.getProfilePictureUrl()))
            Picasso.with(getContext()).
                    load(community.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_group_black_48dp)
                    .into(imageViewPP);
       buttonJoin.setVisibility(community.isMember() ? View.INVISIBLE: View.VISIBLE);

        return  view;
    }

    public interface Listener
    {
        void onJoinClicked(int position, Community community);
        void onCommunityClicked(int position, Community community, View v);
    }
}
