package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CommunityRequestsAdapter extends RecyclerView.Adapter<CommunityRequestsAdapter.ViewHolder>
{

    /* fields */
    List<User> data;
    Context context;


    Listener listener;

    /**
     * registers to be invoked on clicks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public CommunityRequestsAdapter(Context context)
    {
        this.context = context;
        this.data = new ArrayList<>();
    }

    /**
     * updates the data and updates UI
     */
    public void setData(List<User> newData)
    {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }


    /**
     * removes the user with that id and updates UI
     */
    public void remove(String userId)
    {
        // find the position of the user to be removed
        int position = -1;
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).getUserId().equals(userId))
                position = i;

        // remove that position
        if (position != -1)
        {
            data.remove(position);
            notifyItemRemoved(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_community_joinrequests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        final User user = data.get(position);
        holder.textView.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(context).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(holder.imageViewPP);

    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.textView)
        TextView textView;
        @Bind(R.id.imageViewPP)
        ImageView imageViewPP;
        @Bind(R.id.buttonaccept)
        Button acceptButton;
        @Bind(R.id.buttondecline)
        Button declineButton;


        public ViewHolder(final View holder)
        {
            super(holder);
            ButterKnife.bind(this, holder);

            acceptButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View holder)
                {
                    if (listener != null)
                        listener.onAccept(data.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            declineButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View holder)
                {
                    if (listener != null)
                        listener.onDecline(data.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            textView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onClick(data.get(getAdapterPosition())
                            , getAdapterPosition(), holder);
                }
            });

        }
    }

    public interface Listener
    {
        void onClick(User user, int position, View v);

        void onAccept(User user, int adapterPosition);

        void onDecline(User user, int adapterPosition);

    }
}
