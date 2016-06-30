package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.LoginCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity
{
    private static final String hostl = "api.linkedin.com";
    private static final String topCardUrl = "https://" + hostl + "/v1/people/~:" +
            "(email-address,first-name,last-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";


    /* views */

    @Bind(R.id.editTextEmail)
    EditText editTextEmail;
    @Bind(R.id.editTextFirstName)
    EditText editTextFirstName;
    @Bind(R.id.editTextLastName)
    EditText editTextLastName;
    @Bind(R.id.editTextPassword)
    EditText editTextPassword;
    @Bind(R.id.spinnerGender)
    Spinner spinngerGender;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.content)
    LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        if (getIntent() != null && getIntent().getExtras() != null)
            populateDataFromIntent();
    }

    /**
     * moves data from the intent extras to the fields
     */
    private void populateDataFromIntent()
    {
        if (getIntent().getExtras().containsKey(Constants.FIRST_NAME))
            editTextFirstName.setText(getIntent().getStringExtra(Constants.FIRST_NAME));
        if (getIntent().getExtras().containsKey(Constants.LAST_NAME))
            editTextLastName.setText(getIntent().getStringExtra(Constants.LAST_NAME));
        if (getIntent().getExtras().containsKey(Constants.EMAIL))
            editTextEmail.setText(getIntent().getStringExtra(Constants.EMAIL));
    }


    @OnClick(R.id.buttonSignUp)
    public void signUp()
    {
        // gather data
        String email = editTextEmail.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String password = editTextPassword.getText().toString();
        String gender = spinngerGender.getSelectedItem().toString();
        String profilePicture = "";
        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(Constants.PROFILE_PICTURE))
            profilePicture = getIntent().getStringExtra(Constants.PROFILE_PICTURE);

        // sign up
        progressBar.setVisibility(View.VISIBLE);
        AuthenticationAPIController controller = new AuthenticationAPIController(getApplicationContext());
        controller.signUp(email, firstName, lastName, password, profilePicture,  gender, new LoginCallback()
        {

            @Override
            public void success(User user, String authenticationToken)
            {
                // show success
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();

                // open the main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void fail(String message)
            {
                // show error
                progressBar.setVisibility(View.GONE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });


    }

}
