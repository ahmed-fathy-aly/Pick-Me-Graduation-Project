package com.asu.pick_me_graduation_project.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
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
import com.asu.pick_me_graduation_project.utils.Constants;

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

    @Override
    // set an exit transition
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Transition exitTrans = new Fade();
            getWindow().setExitTransition(exitTrans);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // reference views
        ButterKnife.bind(this);


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
