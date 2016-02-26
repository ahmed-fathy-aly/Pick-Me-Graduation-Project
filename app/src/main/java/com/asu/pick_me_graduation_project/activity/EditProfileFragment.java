package com.asu.pick_me_graduation_project.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.asu.pick_me_graduation_project.view.CircleTransform;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.File;

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
    @Bind(R.id.CarModel)
    EditText CarModel;
    @Bind(R.id.CarYear)
    EditText CarYear;
    @Bind(R.id.CarPlate)
    EditText CarPlate;
    @Bind(R.id.Aircondition)
    CheckBox Aircondition;
    @Bind(R.id.carcontent)
    LinearLayout carcontent;
    @Bind(R.id.content)
    LinearLayout content;

    /* fields */

    private Uri imageUri;

    /* fields */

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
                    carcontent.setVisibility(View.VISIBLE);
                else
                    carcontent.setVisibility(View.GONE);
            }
        });

        // load data
        loadProfile();

        return editProfileDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        return rootView;
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
            Uri destination = Uri.fromFile(new File(getContext().getCacheDir(), "cropped"));
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
        CarDetails carDetails = new CarDetails();
        carDetails.setPlateNumber(CarPlate.getText().toString());
        carDetails.setConditioned(Aircondition.isChecked());
        carDetails.setYear(CarYear.getText().toString());
        user.setCarDetails(carDetails);

        // make a progress dialog
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Updating...", false, false);

        // upload user data
        final UserApiController controller = new UserApiController(getContext());
        controller.editProfile(user, new EditProfileCallback()
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

    /**
     * downloads the user's profile and shows it
     */
    private void loadProfile()
    {
        UserApiController controller = new UserApiController(getContext());
        controller.getProfile(new AuthenticationAPIController(getContext()).getCurrentUser().getUserId(), new GetProfileCallback()
        {
            @Override
            public void success(User user)
            {
                //  set profile data to views
                Name.setText(user.getFirstName());
                Lastname.setText(user.getLastName());
                Email.setText(user.getEmail());
                Phonenumber.setText(user.getPhoneNumber());
                Bio.setText(user.getBio());
                if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))

                    Picasso.with(getContext()).
                            load(user.getProfilePictureUrl())
                            .placeholder(R.drawable.ic_user_large)
                            .into(ProfilePic);

                // TODO age
                CarModel.setText(user.getCarDetails().getModel());
                CarYear.setText(user.getCarDetails().getYear());
                CarPlate.setText(user.getCarDetails().getPlateNumber());
                Aircondition.setChecked(user.getCarDetails().isConditioned());

            }

            @Override
            public void fail(String message)
            {
                //  show error using snack bar
                //done by ra2fat imported linearLayout and make linearLayput content
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


}
