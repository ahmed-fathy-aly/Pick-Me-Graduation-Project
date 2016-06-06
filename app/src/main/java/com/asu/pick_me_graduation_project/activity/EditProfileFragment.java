package com.asu.pick_me_graduation_project.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.EditProfileCallback;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.model.CarDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.CarDetailsView;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditProfileFragment extends android.support.v4.app.DialogFragment
{

    /* UI */

    View rootView;
    AlertDialog editProfileDialog;
    @Bind(R.id.ProfilePic)
    ImageView ProfilePic;
    @Bind(R.id.Name)
    EditText Name;
    @Bind(R.id.Lastname)
    EditText Lastname;
    @Bind(R.id.Age)
    EditText Age;
    @Bind(R.id.Email)
    EditText Email;
    @Bind(R.id.CountryCode)
    TextView CountryCode;
    @Bind(R.id.Phonenumber)
    EditText Phonenumber;
    @Bind(R.id.contentphone)
    LinearLayout contentphone;
    @Bind(R.id.quteos)
    ImageView quteos;
    @Bind(R.id.Bio)
    EditText Bio;
    @Bind(R.id.contentbio)
    LinearLayout contentbio;
    @Bind(R.id.Carswitch)
    Switch Carswitch;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.carDetailsView)
    CarDetailsView carDetailsView;

    /* fields */
    private Uri imageUri;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.activity_edit_profile, null);


        // create the dialog
        editProfileDialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setTitle(getString(R.string.title_edit_profile))
                .setPositiveButton(R.string.edit, null)
                .setNegativeButton(R.string.cancel, null)
                .create();
        ButterKnife.bind(this, rootView);

        // add listeners
        Carswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                    carDetailsView.setVisibility(View.VISIBLE);
                else
                    carDetailsView.setVisibility(View.GONE);
            }
        });

        // load data
        loadProfile();

        return editProfileDialog;
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
                    editProfile();
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result)
    {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK)
        {
            // crop the picked image
            Uri destination = Uri.fromFile(new File(getContext().getCacheDir(), "cropped" +  new Random(1000000)));
            Crop.of(result.getData(), destination).asSquare().start(getActivity());
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK)
        {
            // set the cropped image to the image view
            imageUri = Crop.getOutput(result);
            Picasso.with(getContext().getApplicationContext())
                    .load(imageUri)
                    .into(ProfilePic);
        }
    }

    /* listeners */

    @OnClick(R.id.ProfilePic)
    void pickProfilePicture()
    {
        Crop.pickImage(getActivity());
    }

    /**
     * gather user's entered data and upload to backend
     */
    private void editProfile()
    {
        // gather data
        User user = new AuthenticationAPIController(getContext()).getCurrentUser();
        user.setFirstName(Name.getText().toString());
        user.setLastName(Lastname.getText().toString());
        user.setEmail(Email.getText().toString());
        user.setPhoneNumber(Phonenumber.getText().toString());
        user.setBio(Bio.getText().toString());
        CarDetails carDetails = carDetailsView.getCarDetails();
        user.setCarDetails(carDetails);

        // make a progress dialog
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Updating...", false, false);

        // upload user data
        final UserApiController controller = new UserApiController(getContext());
        controller.editProfile(user
                , new AuthenticationAPIController(getContext()).getTokken()
                ,new EditProfileCallback()
        {
            @Override
            public void success(User user)
            {
                // close progress dialog
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                // update user data in preferences
                AuthenticationAPIController controller1 = new AuthenticationAPIController(getContext());
                controller1.updateUser(user);
                editProfileDialog.dismiss();
            }

            @Override
            public void fail(String message)
            {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    /* methods */

    /**
     * downloads the user's profile and shows it
     */
    private void loadProfile()
    {
        UserApiController controller = new UserApiController(getContext().getApplicationContext());
        controller.getProfile(new AuthenticationAPIController(getContext()).getCurrentUser().getUserId(), new GetProfileCallback()
        {
            @Override
            public void success(User user)
            {
                if (!isAdded())
                    return;

                //  set profile data to views
                Name.setText(ValidationUtils.correct(user.getFirstName()));
                Lastname.setText(ValidationUtils.correct(user.getLastName()));
                Email.setText(ValidationUtils.correct(user.getEmail()));
                Phonenumber.setText(ValidationUtils.correct(user.getPhoneNumber()));
                Bio.setText(ValidationUtils.correct(user.getBio()));
                if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))

                    Picasso.with(getContext()).
                            load(user.getProfilePictureUrl())
                            .placeholder(R.drawable.ic_user_large)
                            .into(ProfilePic);

                // TODO age
                Age.setText("30");
                carDetailsView.setCarDetails(user.getCarDetails());

            }

            @Override
            public void fail(String message)
            {
                //  show error using snack bar
                //done by ra2fat imported linearLayout and make linearLayput content
                if (!isAdded())
                    return;
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


}
