package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.LoginCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{

    /* UI */
    @Bind(R.id.editTextUserEmail)
    EditText editTextUserEmail;
    @Bind(R.id.editTextPassword)
    EditText editTextPassword;
    @Bind(R.id.buttonLogIn)
    Button buttonLogIn;
    @Bind(R.id.textViewSignUp)
    TextView textViewSignUp;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.buttonFacebookLogin)
    LoginButton buttonFacebookLogin;


    /* fields */
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // set an exit transition

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Transition exitTrans = new Fade();
            getWindow().setExitTransition(exitTrans);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        }

        super.onCreate(savedInstanceState);

        // initialize facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();

        setContentView(R.layout.activity_login);

        // reference views
        ButterKnife.bind(this);

        // setup facebook login
        callbackManager = CallbackManager.Factory.create();
        LoginButton buttonFacebookLogin = (LoginButton) findViewById(R.id.buttonFacebookLogin);
        buttonFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                onFacebookLogin(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel()
            {
            }

            @Override
            public void onError(FacebookException error)
            {
                Log.e("Game", "error " + error.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onFacebookLogin(String token)
    {
        Log.e("Game", "token = " + token);
    }

    @OnClick(R.id.buttonLogIn)
    void logIn()
    {
        closeKeyboard();

        // gather data
        String email = editTextUserEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // login
        progressBar.setVisibility(View.VISIBLE);
        AuthenticationAPIController controller = new AuthenticationAPIController(getApplicationContext());
        controller.login(email, password, new LoginCallback()
        {
            @Override
            public void success(User user, String authenticationToken)
            {
                // show succes
                progressBar.setVisibility(View.GONE);

                // open the main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this);
                ActivityCompat.startActivity(LoginActivity.this, intent, options.toBundle());
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        finish();
                    }
                }, 1000);
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

    @OnClick(R.id.textViewSignUp)
    void goToSignUp()
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
