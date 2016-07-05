package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.BuildConfig;
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
        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        // no circle reveal
        configSplash.setAnimCircularRevealDuration(0);
        configSplash.setBackgroundColor(R.color.white);

        // logo fade in
        configSplash.setLogoSplash(R.drawable.ic_pick_me_large_icon);
        configSplash.setAnimLogoSplashDuration(700);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

        // no title
        configSplash.setAnimTitleDuration(0);
    }

    @Override
    public void animationsFinished()
    {
        // check if user is logged in
        if (new AuthenticationAPIController(this).isUserLoggedIn())
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
