package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * shows the profile of a user whose id comes from an intent
 */
public class UserProfileActivity extends BaseActivity
{

    /* UI */
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content)
    View content;
    @Bind(R.id.textViewEmail)
    TextView textViewEmail;
    @Bind(R.id.textViewPhoneNumber)
    TextView textViewPhoneNumber;
    @Bind(R.id.textViewAge)
    TextView textViewAge;
    @Bind(R.id.textViewBio)
    TextView textViewBio;
    @Bind(R.id.textViewCarModel)
    TextView textViewCarModel;
    @Bind(R.id.textViewCarYear)
    TextView textViewCarYear;
    @Bind(R.id.textViewCarPlateNumber)
    TextView textViewCarPlateNumber;
    @Bind(R.id.iamgeViewCarIsConditioned)
    ImageView iamgeViewCarIsConditioned;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.imageViewProfilePicture)
    ImageView imageViewProfilePicture;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.scroll)
    NestedScrollView scroll;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.textViewUserName)
    TextView textViewUserName;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.profileHeaderRoot)
    LinearLayout profileHeaderRoot;
    ProgressBar progressBar;

    /* fields */
    private String userId;
    private EditProfileFragment editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // get user id
        userId = getIntent().getExtras().getString(Constants.USER_ID);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        progressBar = (ProgressBar) toolbar.findViewById(R.id.progressBar);

        Log.e("Game", "null?" + (progressBar==null));
        // add a progress bar
        //progressBar = addProgressBar(toolbar);

        // setup collapsing toolbar
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (verticalOffset == 0)
                    ViewCompat.animate(imageViewProfilePicture).scaleX(1).scaleY(1).setDuration(300).start();
                if (verticalOffset == -collapsingToolbar.getHeight() + toolbar.getHeight())
                    ViewCompat.animate(imageViewProfilePicture).scaleX(0).scaleY(0).setDuration(300).start();

            }

        });
        appBarLayout.setExpanded(false, false);
        imageViewProfilePicture.setScaleX(0);
        imageViewProfilePicture.setScaleY(0);


        // load data
        loadProfile();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (editProfileFragment != null && editProfileFragment.isAdded())
            editProfileFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * downloads the user's profile and shows it
     */
    private void loadProfile()
    {

        progressBar.setVisibility(View.VISIBLE);
        UserApiController controller = new UserApiController(getApplicationContext());
        controller.getProfile(userId, new GetProfileCallback()
        {
            @Override
            public void success(User user)
            {
                profileHeaderRoot.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                appBarLayout.setExpanded(true, true);

                Log.e("Game", "success");
                //  set profile data to views
                textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
                ratingBar.setNumStars(user.getRate());
                textViewEmail.setText(user.getEmail());
                textViewPhoneNumber.setText(user.getPhoneNumber());
                if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
                    Picasso.with(getApplicationContext()).
                            load(user.getProfilePictureUrl())
                            .placeholder(R.drawable.ic_user_large)
                            .into(imageViewProfilePicture);

                textViewBio.setText(user.getBio());
                textViewCarModel.setText(user.getCarDetails().getModel());
                textViewCarYear.setText(user.getCarDetails().getYear());
                textViewCarPlateNumber.setText(user.getCarDetails().getPlateNumber());
                //iamgeViewCarIsConditioned.setImageResource(Integer.parseInt(String.valueOf(user.getCarDetails().isConditioned())));//not sure


                Log.e("Game", "used id = " + userId);
                Log.e("Game", "current id = " + new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId());

                // set the icon of the fab...depending on it's the profile of this user or not
                if (userId.equals(new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId()))
                {
                    fab.setImageResource(R.drawable.ic_edit);
                } else
                {
                    fab.setImageResource(R.drawable.ic_chat);
                }
            }

            @Override
            public void fail(String message)
            {

                progressBar.setVisibility(View.INVISIBLE);
                //  show error using snack bar
                //done by ra2fat imported linearLayout and make linearLayput content
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                Log.e("Game", "failed to get profile" + message);
            }
        });
    }

    @OnClick(R.id.fab)
    public void onClick()
    {
        if (userId.equals(new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId()))
        {
            //  show the edit profile dialog if it's the profile of this user
            //done by raafat
            if (userId.equals(new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId()))
            {
                editProfileFragment = new EditProfileFragment();
                editProfileFragment.show(getSupportFragmentManager(), getString(R.string.title_edit_profile));
            }
        }
    }
}
