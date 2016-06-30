package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

/**
 * Created by ahmed on 6/18/2016.
 */
public class linkedin extends AppCompatActivity {
    public static final String PACKAGE = "asu.com.pick_me_graduation_project";


    TextView login_linkedin_btn;
    ImageView imagel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linked1);
        login_linkedin_btn = (TextView) findViewById(R.id.login_button);
        login_linkedin_btn.setOnClickListener(loginClick);
        imagel = (ImageView) findViewById(R.id.imagel);
        imagel.setOnClickListener(loginClick);


    }

    // Authenticate with linkedin and intialize Session.

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

                    login();

        }
    };

    public void login() {
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    // After complete authentication start new HomePage Activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                requestCode, resultCode, data);
        Intent intent = new Intent(linkedin.this, SignUpActivity.class);
        startActivity(intent);
    }

    // This method is used to make permissions to retrieve data from linkedin

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }
}