package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CommunityRequestsAdapter extends RecyclerView.Adapter< CommunityRequestsAdapter.ViewHolder> {

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

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_community_joinrequests, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,final int position) {
            //CommunityPost post = data.get(position);
            //holder.textViewContent.setText(post.getContent());
            final User user = data.get(position);
            holder.textView.setText(user.getFirstName() + " " + user.getLastName());

            // / holder.textView.setText(TimeUtils.getUserFriendlyDate(context, post.getDate()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            @Bind(R.id.textView)
            TextView textView;
            // @Bind(R.id.imageViewPP)
            // TextView imageViewPP;
            @Bind(R.id.buttonaccept)
            Button acceptButton;
            @Bind(R.id.buttondecline)
            Button declineButton;


            public ViewHolder(final View holder) {
                super(holder);
                ButterKnife.bind(this, holder);
                final View itemView;
                itemView = holder;
                acceptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View holder) {
                        if (listener != null)
                            listener.onAccept(data.get(getAdapterPosition()), getAdapterPosition());
                        itemView.setVisibility(View.GONE);
                    }
                });

                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View holder) {
                        itemView.setVisibility(View.GONE);
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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

        }
    }
