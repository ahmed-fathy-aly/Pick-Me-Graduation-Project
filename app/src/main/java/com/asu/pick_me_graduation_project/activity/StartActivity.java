package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import io.fabric.sdk.android.Fabric;

/**
 * The activity started when the app is opened
 * Chooses which activity to open
 */
public class StartActivity extends AwesomeSplash
{

    @Override
    public void initSplash(ConfigSplash configSplash)
    {
        // setup fabric
        Fabric.with(this, new Crashlytics());

        configSplash.setAnimCircularRevealDuration(0);
        configSplash.setBackgroundColor(R.color.white);

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ic_pick_me_large_icon);
        configSplash.setAnimLogoSplashDuration(700);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);


        //Customize Title
        configSplash.setTitleSplash(getString(R.string.app_name));
        configSplash.setTitleTextColor(R.color.primary_text);
        configSplash.setTitleTextSize(24f);
        configSplash.setAnimTitleDuration(800);
        configSplash.setAnimTitleTechnique(Techniques.SlideInUp);

    }

    @Override
    public void animationsFinished()
    {
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
