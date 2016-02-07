package com.asu.pick_me_graduation_project.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import butterknife.ButterKnife;

/**
 * shows the profile of a user whose id comes from an intent
 */
public class UserProfileActivity extends AppCompatActivity
{

    /* UI */

    /* fields */
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // get user id
        userId = getIntent().getExtras().getString(Constants.USER_ID);

        // TODO - reference views (if you dont wanna use butterknife like the log in activity no problem, use findViewByIdInstead
        ButterKnife.bind(this);

        loadProfile();

    }

    /**
     * downloads the user's profile and shows it
     */
    private void loadProfile()
    {
        UserApiController controller = new UserApiController(getApplicationContext());
        controller.getProfile(userId, new GetProfileCallback()
        {
            @Override
            public void success(User user)
            {
                // TODO set profile data to views
                Log.e("Game", "loaded");
            }

            @Override
            public void fail(String message)
            {
                // TODO show error using snack bar
            }
        });
    }
}
