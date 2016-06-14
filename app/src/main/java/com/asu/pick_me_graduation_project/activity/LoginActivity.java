package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
import android.widget.Toast;

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
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends BaseActivity
{

    /* constants */
    private static final int RC_SIGN_IN = 42;

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
    Button buttonFacebookLogin;
    @Bind(R.id.buttonGoogleSignIn)
    Button buttonGoogleSignIn;


    /* fields */
    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;

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
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        // reference views
        ButterKnife.bind(this);

        // init facebook
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
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
                    public void onError(FacebookException exception)
                    {
                    }
                });

        // init google client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult)
                    {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            // google login
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.e("Game", "google result " + result.getStatus().toString());
            if (result.isSuccess())
            {
                GoogleSignInAccount acct = result.getSignInAccount();
                String idToken = acct.getIdToken();
                onGoogleLogin(idToken);
            }
        }
        else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.buttonFacebookLogin)
    void onButtonFacebookClicked()
    {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    @OnClick(R.id.buttonGoogleSignIn)
    void onButtonGoogleClicked()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    private void onFacebookLogin(String token)
    {
        Log.e("Game", "facebook token = " + token);
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
    }


    private void onGoogleLogin(String token)
    {
        Log.e("Game", "google token = " + token);
        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.textViewSignUp)
    void goToSignUp()
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
