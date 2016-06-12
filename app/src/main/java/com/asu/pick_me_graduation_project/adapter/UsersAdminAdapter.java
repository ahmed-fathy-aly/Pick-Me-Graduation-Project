package com.asu.pick_me_graduation_project.adapter;

import android.app.ProgressDialog;
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
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.HashSet;


/**
 * Created by ahmed on 2/12/2016.
 * show a row with user name and pp
 */
public class UsersAdminAdapter extends ArrayAdapter<User>
{
    Listener listener;
    boolean canMakeAdmins;
    HashSet<String> progress;

    public UsersAdminAdapter(Context context, boolean canMakeAdmins)
    {
        super(context, R.layout.row_user);
        this.canMakeAdmins = canMakeAdmins;
        this.progress = new HashSet<>();
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
        view = LayoutInflater.from(getContext()).inflate(R.layout.row_user_admin, parent, false);

        // reference views
        TextView textViewUsername = (TextView) view.findViewById(R.id.textViewUserName);
        TextView textViewAdmin= (TextView) view.findViewById(R.id.textViewAdmin);
        Button buttonMakeAdmin = (Button) view.findViewById(R.id.buttonMakeAdmin);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final ImageView imageViewPP = (ImageView) view.findViewById(R.id.imageViewPP);

        // set data
        final User user = getItem(position);
        textViewUsername.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(getContext()).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(imageViewPP);
        boolean showButton = canMakeAdmins && !user.isAdmin() && !progress.contains(user.getUserId());
        buttonMakeAdmin.setVisibility(showButton ? View.VISIBLE : View.GONE);
        textViewAdmin.setVisibility(user.isAdmin() ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(progress.contains(user.getUserId()) ? View.VISIBLE : View.GONE )
        ;
        // add listenrs
        final View finalView = view;
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener != null)
                    listener.onClick(user, position, finalView);
            }
        });
        buttonMakeAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listener!=null)
                    listener.onMakeAdmin(user);
            }
        });

        return view;
    }


    public void setProgress(String id)
    {
        progress.add(id);
        notifyDataSetChanged();
    }

    public void setAdmin(String id)
    {
        progress.remove(id);
        for (int i = 0; i < getCount(); i++)
            if (getItem(i).getUserId().equals(id))
                getItem(i).setIsAdmin(true);
        notifyDataSetChanged();
    }

    public interface Listener
    {
        public void onClick(User user, int position, View v);

        void onMakeAdmin(User user);
    }


}
