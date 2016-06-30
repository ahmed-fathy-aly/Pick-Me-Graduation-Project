package com.asu.pick_me_graduation_project.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
{

    /* constants */
    private static final int GOOGLE_SIGN_IN = 42;
    private static final int FACEBOOK_SIGN_IN = 43;
    private static final String LINKED_IN_HOST = "api.linkedin.com";
    private static final String LINKED_IN_TOP_CARD_URL = "https://" + LINKED_IN_HOST + "/v1/people/~:" +
            "(email-address,first-name,last-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

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

        // init social media login
        initFacebookLogin();
        initGoogleLogin();

        printHashKey();


    }

    public void printHashKey() {
        try {

            PackageInfo info = getPackageManager().getPackageInfo(
                    "asu.com.pick_me_graduation_project",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            Log.e("Game", "hash error " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {

            Log.e("Game", "hash error " + e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN)
        {
            // google login
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                GoogleSignInAccount acct = result.getSignInAccount();
                String idToken = acct.getIdToken();
                onGoogleLogin(idToken);
            }
        } else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())
        {
            // facebook login
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else
        {
            // linked in sign in
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                    requestCode, resultCode, data);
        }
    }


    /* listeners */

    @OnClick(R.id.textViewSignUp)
    void goToSignUp()
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.buttonFacebookLogin)
    void onButtonFacebookClicked()
    {
        LoginManager
                .getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    @OnClick(R.id.buttonGoogleSignIn)
    void onButtonGoogleClicked()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.buttonLinkedInLogin)
    void onButtonLinkedInClicked()
    {
        LISessionManager
                .getInstance(getApplicationContext())
                .init(this, Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS), new AuthListener()
                {
                    @Override
                    public void onAuthSuccess()
                    {
                        Log.e("Game", "linked in token " + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString());
                        onLinkedInLogin();
                    }

                    @Override
                    public void onAuthError(LIAuthError error)
                    {
                        Snackbar.make(content, error.toString(), Snackbar.LENGTH_SHORT).show();
                        Log.e("Game", "linked in error " + error);
                    }
                }, true);
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
        controller.login(email, password, new LoginCallback());
    }

    /* methods */

    /**
     * prepares the google client for login
     */
    private void initGoogleLogin()
    {
        // init google client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .requestProfile()
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

    /**
     * prepares facebook callbacks for login
     */
    private void initFacebookLogin()
    {
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

    }

    /**
     * called after we receive the token from faceboko SDK
     * sends the facebook token to the backend for authentication
     */
    private void onFacebookLogin(String token)
    {
        progressBar.setVisibility(View.VISIBLE);

        // login from backend
        AuthenticationAPIController controller = new AuthenticationAPIController(this);
        controller.loginByFacebook(token, new LoginCallback());
    }


    /**
     * called after we receive the token from google SDK
     * sends the google token to the backend for authentication
     */
    private void onGoogleLogin(String token)
    {
        progressBar.setVisibility(View.VISIBLE);

        // login from backend
        AuthenticationAPIController controller = new AuthenticationAPIController(this);
        controller.loginByGmail(token, new LoginCallback());
    }


    /**
     * called after we receive the token from linked in
     * fetches the profile and passes the data to the Sign Up activity
     */
    private void onLinkedInLogin()
    {
        progressBar.setVisibility(View.VISIBLE);

        // make a request to get the user's profile
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, LINKED_IN_TOP_CARD_URL, new ApiListener()
        {
            @Override
            public void onApiSuccess(ApiResponse result)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

                // parse the profile data
                try
                {
                    JSONObject response = result.getResponseDataAsJson();
                    if (!response.isNull("emailAddress"))
                        intent.putExtra(Constants.EMAIL, response.get("emailAddress").toString());
                    if (!response.isNull("firstName"))
                        intent.putExtra(Constants.FIRST_NAME, response.get("firstName").toString());
                    if (!response.isNull("lastName"))
                        intent.putExtra(Constants.LAST_NAME, response.get("lastName").toString());
                    if (!response.isNull("pictureUrl"))
                        intent.putExtra(Constants.PROFILE_PICTURE, response.get("pictureUrl").toString());

                } catch (Exception e)
                {
                }

                // open the sign up activity
                startActivity(intent);
            }

            @Override
            public void onApiError(LIApiError error)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(content, error.toString(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * called after google or facebook or normal login
     * opens the dashboard activity then finishes this one
     */
    class LoginCallback implements com.asu.pick_me_graduation_project.callback.LoginCallback
    {
        @Override
        public void success(User user, String authenticationToken)
        {
            // show succes
            progressBar.setVisibility(View.GONE);

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
    }



}
