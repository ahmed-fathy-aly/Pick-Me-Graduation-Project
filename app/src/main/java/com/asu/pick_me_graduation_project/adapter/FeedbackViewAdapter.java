package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.github.ornolfr.ratingview.RatingView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 4/10/2016.
 */
public class FeedbackViewAdapter extends RecyclerView.Adapter<FeedbackViewAdapter.ViewHolder>
{

    /* fields */
    List<Feedback> data;
    Context context;

    public FeedbackViewAdapter(Context context)
    {
        this.context = context;
        this.data = new ArrayList<>();
    }

    /**
     * updates the data and updates UI
     */
    public void setData(List<Feedback> newData)
    {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_feedback_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Feedback feedback = data.get(position);

        // from user
        User user = feedback.getFromUser();
        holder.textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(context).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(holder.imageViewUserPP);

        // passenger feedback
        holder.ratingAttitude.setRating(feedback.getAttitude());
        holder.ratingPunctuation.setRating(feedback.getPunctuality());
        holder.textViewComment.setText(ValidationUtils.correct(feedback.getComment()));
        holder.textViewComment.setVisibility(holder.textViewComment.getText().length() > 0
                ? View.VISIBLE : View.INVISIBLE);

        // driving feedback
        if (feedback.getDrivingFeedback() != null)
        {
            holder.layoutDriving.setVisibility(View.VISIBLE);
            holder.ratingDriving.setRating(feedback.getDrivingFeedback().getDriving());
        } else
            holder.layoutDriving.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.ratingAttitude)
        RatingView ratingAttitude;
        @Bind(R.id.ratingPunctuation)
        RatingView ratingPunctuation;
        @Bind(R.id.ratingDriving)
        RatingView ratingDriving;
        @Bind(R.id.layoutDriving)
        View layoutDriving;
        @Bind(R.id.textViewComment)
        TextView textViewComment;
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
