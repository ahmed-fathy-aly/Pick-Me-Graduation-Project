package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.RideAnnouncement;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 4/10/2016.
 */
public class RideAnnoucementsAdapter extends RecyclerView.Adapter<RideAnnoucementsAdapter.ViewHolder>
{

    /* fields */
    List<RideAnnouncement> data;
    Context context;

    public RideAnnoucementsAdapter(Context context)
    {
        this.context = context;
        this.data = new ArrayList<>();
    }

    /**
     * updates the data and updates UI
     */
    public void setData(List<RideAnnouncement> newData)
    {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    /**
     * insert in the first position
     */
    public void addToTop(RideAnnouncement announcement)
    {
        data.add(0, announcement);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_ride_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        RideAnnouncement announcment = data.get(position);

        // from user
        User user = announcment.getUser();
        holder.textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(context).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(holder.imageViewUserPP);

        // content and date
        String dateStr = TimeUtils.getUserFriendlyDate(context, announcment.getDate());
        holder.textViewDate.setText(dateStr);
        holder.textViewContent.setText(announcment.getContent());
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.textViewContent)
        TextView textViewContent;
        @Bind(R.id.textViewDate)
        TextView textViewDate;
        @Bind(R.id.textViewUserName)
        TextView textViewUserName;
        @Bind(R.id.imageViewUserPP)
        ImageView imageViewUserPP;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
