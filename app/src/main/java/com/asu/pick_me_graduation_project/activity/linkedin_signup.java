package com.asu.pick_me_graduation_project.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.LoginCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ahmed on 6/30/2016.
 */
public class linkedin_signup extends AppCompatActivity
    {   private static final String hostl = "api.linkedin.com";
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

        String profilepic;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            ButterKnife.bind(this);
            linkededinApiHelper();

        }

        public void linkededinApiHelper(){
            APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
            apiHelper.getRequest(linkedin_signup.this, topCardUrl, new ApiListener() {
                @Override
                public void onApiSuccess(ApiResponse result) {
                    try {

                        setprofile(result.getResponseDataAsJson());
                        //progress.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onApiError(LIApiError error) {
                    // ((TextView) findViewById(R.id.error)).setText(error.toString());

                }
            });
        }

    /*
       Set User Profile Information in Navigation Bar.
     */

        public  void  setprofile(JSONObject response){

            try {


                    editTextEmail.setText(response.get("emailAddress").toString());
                    editTextFirstName.setText(response.get("firstName").toString());
                    editTextLastName.setText(response.get("lastName").toString());
                    profilepic  = response.getString("pictureUrl").toString();

                } catch (Exception e){
                e.printStackTrace();
            }
        }


        @OnClick(R.id.buttonSignUp)
        public void signUp_bylinked()
        {

            // gather data
            String email = editTextEmail.getText().toString();
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            String password  = editTextPassword.getText().toString();
            String gender = spinngerGender.getSelectedItem().toString();

            // sign up
            progressBar.setVisibility(View.VISIBLE);
            AuthenticationAPIController controller = new AuthenticationAPIController(getApplicationContext());
            controller.signUp_linked(email, firstName, lastName, password, gender, profilepic, new LoginCallback() {

                @Override
                public void success(User user, String authenticationToken) {
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
                public void fail(String message) {
                    // show error
                    progressBar.setVisibility(View.GONE);
                    Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                }
            });


        }

    }


