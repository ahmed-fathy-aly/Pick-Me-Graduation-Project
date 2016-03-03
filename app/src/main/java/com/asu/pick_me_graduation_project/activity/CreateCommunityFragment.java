package com.asu.pick_me_graduation_project.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.asu.pick_me_graduation_project.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCommunityFragment extends android.support.v4.app.DialogFragment
{


    /* UI */
    private View rootView;
    private AlertDialog createCommunityDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create_community, null);


        // create the dialog
        createCommunityDialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setTitle(getString(R.string.title_create_community))
                .setPositiveButton(R.string.create, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        ButterKnife.bind(this, rootView);


        return createCommunityDialog;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        // super.onStart() is where dialog.show() is actually called on the underlying dialog,
        // so we have to do it after this point to prevent positive button listener rom dismissing the dialog
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    createCommunity();
                }
            });
        }
    }

    /**
     * asks the back end to create a new community
     */
    private void createCommunity()
    {

    }
}
