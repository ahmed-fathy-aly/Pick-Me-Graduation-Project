package com.asu.pick_me_graduation_project.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.CreateCommunityCallback;
import com.asu.pick_me_graduation_project.callback.CreatePostCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePostFragment extends DialogFragment {

    /* fields */
    String communityId;

    /* UI */
    @Bind(R.id.content)
    View content;
    @Bind(R.id.editTextContent)
    EditText editTextContent;
    private AlertDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communityId = getArguments().getString(Constants.COMMUNITY_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_create_post, null);


        // create the dialog
        dialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setTitle(getString(R.string.title_new_post))
                .setPositiveButton(R.string.post, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        ButterKnife.bind(this, rootView);


        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // super.onStart() is where dialog.show() is actually called on the underlying dialog,
        // so we have to do it after this point to prevent positive button listener rom dismissing the dialog
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPost();
                }
            });
        }
    }

    /**
     * asks the back end to create a new community
     */
    private void createPost() {
        // gather data
        String conentText = editTextContent.getText().toString();

        // create a a post
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.posting));
        CommunityAPIController controller = new CommunityAPIController(getContext());
        controller.createPost(
                new AuthenticationAPIController(getContext()).getTokken()
                , communityId
                , conentText
                , new CreatePostCallback() {
                    @Override
                    public void success(CommunityPost post) {
                        // show success
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();

                        // pass the new post

                        // finish
                        dialog.dismiss();
                    }

                    @Override
                    public void fail(String message) {
                        // show error
                        progressDialog.dismiss();
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });


    }

}
