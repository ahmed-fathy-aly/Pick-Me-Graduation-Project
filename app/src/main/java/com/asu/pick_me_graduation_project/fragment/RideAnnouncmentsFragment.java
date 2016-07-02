package com.asu.pick_me_graduation_project.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.CreateAnnouncementCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.RideAnnouncment;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RideAnnouncmentsFragment extends Fragment
{


    /* fields */
    private String rideId;
    private RidesAPIController controller;

    /* views */
    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.recyclerViewAnnoucnments)
    RecyclerView recyclerViewAnnoucnments;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    /* life cycle methods */

    public RideAnnouncmentsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.rideId = getArguments().getString(Constants.RIDE_ID);
        this.controller = new RidesAPIController(getContext());
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout and reference views
        View view = inflater.inflate(R.layout.fragment_ride_announcments, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    /* listener */
    @OnClick(R.id.fab)
    void openCreateAnnouncement()
    {
        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.make_an_announcment));

        // Set up the input
        final EditText editTextMessage = new EditText(getContext());
        editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextMessage.setHint(getString(R.string.announcment));
        editTextMessage.setGravity(Gravity.CENTER_VERTICAL);
        builder.setView(editTextMessage);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String content = editTextMessage.getText().toString();
                makeAnAnnouncment(content);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();

    }

    /**
     * make a post request to the backend
     *
     */
    private void makeAnAnnouncment(final String announcementContent)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.posting));
        controller.postAnnouncement(
                new AuthenticationAPIController(getContext()).getTokken()
                , rideId
                , announcementContent
                , new CreateAnnouncementCallback()
                {
                    @Override
                    public void success(RideAnnouncment announcment)
                    {
                        progressDialog.dismiss();
                        Log.e("Game", "succes " + announcment.getContent());
                    }

                    @Override
                    public void fail(String message)
                    {
                        progressDialog.dismiss();
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    /* methods */
}
