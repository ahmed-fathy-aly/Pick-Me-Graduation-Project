package com.asu.pick_me_graduation_project.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.EditProfileCallback;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.events.UpdateUserProfileEvent;
import com.asu.pick_me_graduation_project.model.CarDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.CarDetailsView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
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
    @Bind(R.id.Residence)
    EditText Residence;
    @Bind(R.id.dob)
    EditText editTextDOB;

    /* fields */
    private Uri imageUri;
    private Calendar chosenDateOfBirth;


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


    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result)
    {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK)
        {
            // crop the picked image
            Uri destination = Uri.fromFile(new File(getContext().getCacheDir(), "cropped" + new Random(1000000)));
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
     */

    /* listeners */

    @OnClick(R.id.ProfilePic)
    void pickProfilePicture()
    {
        //Crop.pickImage(getActivity());
    }

    @OnClick(R.id.dob)
    void openDatePickerDialog()
    {
        // listener for when the user picks a time
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                // update the edit text and save the chosen time
                editTextDOB.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
                chosenDateOfBirth = Calendar.getInstance();
                chosenDateOfBirth.set(Calendar.YEAR, year);
                chosenDateOfBirth.set(Calendar.MONTH, monthOfYear);
                chosenDateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        // show the picker dialog
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext()
                , listener
                , now.get(Calendar.YEAR)
                , now.get(Calendar.MONTH)
                , now.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    /* methods */

    /**
     * gather user's entered data and upload to backend
     */
    private void editProfile()
    {
        // gather data
        User userToBeEdited = new AuthenticationAPIController(getContext()).getCurrentUser();
        userToBeEdited.setFirstName(Name.getText().toString());
        userToBeEdited.setLastName(Lastname.getText().toString());
        userToBeEdited.setEmail(Email.getText().toString());
        userToBeEdited.setResidence(Residence.getText().toString());
        userToBeEdited.setdob(chosenDateOfBirth);
        userToBeEdited.setPhoneNumber(Phonenumber.getText().toString());
        userToBeEdited.setBio(Bio.getText().toString());
        CarDetails carDetails = carDetailsView.getCarDetails();
        userToBeEdited.setCarDetails(carDetails);

        // make a progress dialog
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", "Updating...", false, false);

        // upload user data
        final UserApiController controller = new UserApiController(getContext());
        controller.editProfile(userToBeEdited
                , new AuthenticationAPIController(getContext()).getTokken()
                , new EditProfileCallback()
        {
            @Override
            public void success(User user)
            {
                if (!isAdded())
                    return;

                Log.e("Game", "edit success " + user.getdob());
                // close progress dialog
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                // update user data in preferences
                AuthenticationAPIController controller1 = new AuthenticationAPIController(getContext());
                controller1.updateUser(user);

                // notify the profile activity to update itself
                EventBus.getDefault().post(new UpdateUserProfileEvent(user.getUserId()));

                editProfileDialog.dismiss();
            }

            @Override
            public void fail(String message)
            {
                Log.e("Game", "edit faield " + message);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


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
                Residence.setText(ValidationUtils.correct(user.getResidence()));
                Phonenumber.setText(ValidationUtils.correct(user.getPhoneNumber()));
                if (user.getdob() != null)
                    editTextDOB.setText(TimeUtils.getUserFriendlyDOB(user.getdob()));
                Bio.setText(ValidationUtils.correct(user.getBio()));
                if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
                    Picasso.with(getContext()).
                            load(user.getProfilePictureUrl())
                            .placeholder(R.drawable.ic_user_large)
                            .into(ProfilePic);
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
