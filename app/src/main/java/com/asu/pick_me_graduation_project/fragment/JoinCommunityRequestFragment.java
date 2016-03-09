package com.asu.pick_me_graduation_project.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinCommunityRequestFragment extends android.support.v4.app.DialogFragment
{


    /* UI */
    private View rootView;
    private AlertDialog dialog;

    /* fields */
    String communityId;
    String communityName;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_request_join_community, null);

        // receive argument
        communityId = getArguments().getString(Constants.COMMUNITY_ID);
        communityName = getArguments().getString(Constants.COMMUNITY_NAME);

        // create the dialog
        dialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setTitle(getString(R.string.title_request_to_join))
                .setPositiveButton(R.string.send, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        ButterKnife.bind(this, rootView);


        return dialog;
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
                    requestToJoin();
                }
            });
        }
    }

    /**
     * asks the backend to request to join that community
     */
    private void requestToJoin()
    {
        // TODO
    }

}
