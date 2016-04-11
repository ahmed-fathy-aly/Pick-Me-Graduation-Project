package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;

/**
 * Created by ahmed on 4/10/2016.
 */
public class CommunityPostsAdapter extends RecyclerView.Adapter<CommunityPostsAdapter.ViewHolder> {

    /* fields */
    List<CommunityPost> data;
    Context context;

    public CommunityPostsAdapter(Context context)
    {
        this.context = context;
        this.data = new ArrayList<>();
    }

    /**
     * updates the data and updates UI
     */
    public void setData(List<CommunityPost> newData)
    {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_community_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommunityPost post = data.get(position);
        holder.textViewContent.setText(post.getContent());
        holder.textViewDate.setText(TimeUtils.getUserFriendlyDate(context, post.getDate()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.textViewDate)
        TextView textViewDate;
        @Bind(R.id.textViewContent)
        TextView textViewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
