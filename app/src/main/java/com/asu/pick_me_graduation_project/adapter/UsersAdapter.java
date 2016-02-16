package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.view.CircleTransform;
import com.squareup.picasso.Picasso;


/**
 * Created by ahmed on 2/12/2016.
 * show a row with user name and pp
 */
public class UsersAdapter extends ArrayAdapter<User>
{
    Listener listener;

    public UsersAdapter(Context context)
    {
        super(context, R.layout.row_user);
    }

    /**
     * registers to be invoked on clicks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        // inflate view
        //if (view == null)
        view = LayoutInflater.from(getContext()).inflate(R.layout.row_user, parent, false);

        // reference views
        TextView textViewUsername = (TextView) view.findViewById(R.id.textViewUserName);
        ImageView imageViewPP = (ImageView) view.findViewById(R.id.imageViewPP);

        // set data
        final User user = getItem(position);
        textViewUsername.setText(user.getFirstName());
        Picasso.with(getContext()).
                load(user.getProfilePictureUrl())
                .transform(new CircleTransform())
                .into(imageViewPP);

        // add listenrs
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                    listener.onClick(user, position);
            }
        });

        return view;
    }

    public interface Listener
    {
        public void onClick(User user, int position);

    }
}
