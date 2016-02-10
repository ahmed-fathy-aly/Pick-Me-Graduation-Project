package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;

/**
 * The activity started when the app is opened
 * Chooses which activity to open
 */
public class StartActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // check if user is logged in
        if (new AuthenticationAPIController(this).isUserLoggedIn() )
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
