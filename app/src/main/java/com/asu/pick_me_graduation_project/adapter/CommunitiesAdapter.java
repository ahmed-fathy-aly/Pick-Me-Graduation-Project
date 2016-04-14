package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by ahmed on 3/8/2016.
 */
public class CommunitiesAdapter extends ArrayAdapter<Community> {
    /* fields */
    Listener listener;
    HashSet<String> joiningSet;
    HashSet<String> joinedSet;

    public CommunitiesAdapter(Context context) {
        super(context, R.layout.row_community);
    }

    /**
     * registeers to be invoked with callbacks
     */
    public void setListener(Listener listener) {
        this.listener = listener;
        joiningSet = new HashSet<>();
        joinedSet = new HashSet<>();
    }

    @Override
    public void addAll(Collection<? extends Community> collection) {
        super.addAll(collection);
        joiningSet.clear();
        joinedSet.clear();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.row_community, parent, false);
        final View finalView = view;

        // reference view
        ImageView imageViewPP = (ImageView) view.findViewById(R.id.imageViewPP);
        TextView textViewCommunityName = (TextView) view.findViewById(R.id.textViewCommunityName);
        Button buttonJoin = (Button) view.findViewById(R.id.buttonJoin);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // set listeners
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCommunityClicked(position, getItem(position), finalView);
            }
        });
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


        // check if join is still loading
        boolean showJoin = !community.isMember()
                && !joiningSet.contains(community.getId())
                && !joinedSet.contains(community.getId());
        boolean showProgress = joiningSet.contains(community.getId());
        buttonJoin.setVisibility(showJoin ? View.VISIBLE : View.INVISIBLE);
        progressBar.setVisibility(showProgress ? View.VISIBLE : View.INVISIBLE);

        return view;
    }

    /**
     * removes the progress bar and join button for that community
     */
    public void markRequestSent(String id) {
        joinedSet.add(id);
        joiningSet.remove(id);
        notifyDataSetChanged();
    }

    /**
     * removes the join button and show the progress bar for that community
     */
    public void markRequestSending(String id) {
        joiningSet.add(id);
        notifyDataSetChanged();
    }

    public interface Listener {
        void onJoinClicked(int position, Community community);

        void onCommunityClicked(int position, Community community, View v);
    }
}
