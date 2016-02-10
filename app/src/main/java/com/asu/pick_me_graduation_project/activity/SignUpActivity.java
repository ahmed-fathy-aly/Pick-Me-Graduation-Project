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
import com.asu.pick_me_graduation_project.callback.SignUpCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity
{

    /* views */

    @Bind(R.id.editTextEmail)
    EditText editTextEmail;
    @Bind(R.id.editTextUserName)
    EditText editTextUserName;
    @Bind(R.id.editTextFullName)
    EditText editTextFullName;
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
    }

    @OnClick(R.id.buttonSignUp)
    public void signUp()
    {
        // gather data
        String email = editTextEmail.getText().toString();
        String userName = editTextUserName.getText().toString();
        String fullName = editTextEmail.getText().toString();
        String password  = editTextPassword.getText().toString();
        String gender = spinngerGender.getSelectedItem().toString();

        // sign up
        progressBar.setVisibility(View.VISIBLE);
        AuthenticationAPIController controller = new AuthenticationAPIController(getApplicationContext());
        controller.signUp(email, userName, fullName, password, gender, new SignUpCallback()
        {

            @Override
            public void success(User user, String authenticationToken)
            {
                // show succes
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
