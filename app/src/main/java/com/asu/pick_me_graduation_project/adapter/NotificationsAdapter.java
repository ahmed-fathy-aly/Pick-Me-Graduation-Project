package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.Notification;
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
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>
{

    /* fields */
    List<Notification> data;
    Context context;
    Listener listener;

    public NotificationsAdapter(Context context)
    {
        this.context = context;
        this.data = new ArrayList<>();
    }

    /**
     * registers to be invoked for callbacks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    /**
     * updates the data and updates UI
     */
    public void setData(List<Notification> newData)
    {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Notification notification = data.get(position);

        holder.textViewTitle.setText(ValidationUtils.correct(notification.getTitle()));
        holder.textViewMessage.setText(ValidationUtils.correct(notification.getMessage()));
        holder.imageViewNew.setVisibility(notification.isSeen() ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        @Bind(R.id.textViewTitle)
        TextView textViewTitle;
        @Bind(R.id.textViewMessage)
        TextView textViewMessage;
        @Bind(R.id.imageViewNew)
        ImageView imageViewNew;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null )
                        listener.onNotificationClick(data.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface  Listener
    {
        void onNotificationClick(Notification notification);
    }
}
