package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetProfileCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
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

    /* fields */
    private String userId;

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

        // load data
        loadProfile();

    }


    @Override
    protected void onResume()
    {
        super.onResume();


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                // TODO animate fab
            }
        }, 300);
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
                //  set profile data to views
                textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
                ratingBar.setNumStars(user.getRate());
                textViewEmail.setText(user.getEmail());
                textViewPhoneNumber.setText(user.getPhoneNumber());
                Picasso.with(getApplicationContext()).
                        load(user.getProfilePictureUrl())
                        .noFade()
                        .into(imageViewProfilePicture);

                textViewAge.setText("23");
                textViewBio.setText(user.getBio());
                textViewCarModel.setText(user.getCarDetails().getModel());
                textViewCarYear.setText(user.getCarDetails().getYear());
                textViewCarPlateNumber.setText(user.getCarDetails().getPlateNumber());
                //iamgeViewCarIsConditioned.setImageResource(Integer.parseInt(String.valueOf(user.getCarDetails().isConditioned())));//not sure


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
                //  show error using snack bar
                //done by ra2fat imported linearLayout and make linearLayput content
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
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
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                editProfileFragment.show(getSupportFragmentManager(), getString(R.string.title_edit_profile));
            }
        }
    }
}
