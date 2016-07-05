package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
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
import com.asu.pick_me_graduation_project.events.NewMessageEvent;
import com.asu.pick_me_graduation_project.events.UpdateUserProfileEvent;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.github.florent37.materialimageloading.MaterialImageLoading;
import com.github.ornolfr.ratingview.RatingView;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

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
    @Bind(R.id.textViewno_of_rides)
    TextView no_of_rides;
    @Bind(R.id.points)
    TextView points;
    @Bind(R.id.Residence)
    TextView Residence;
    @Bind(R.id.textViewBio)
    TextView textViewBio;
    @Bind(R.id.textViewCarModel)
    TextView textViewCarModel;
    @Bind(R.id.textViewCarYear)
    TextView textViewCarYear;
    @Bind(R.id.textViewCarPlateNumber)
    TextView textViewCarPlateNumber;
    @Bind(R.id.checkBoxAirConditioned)
    CheckBox checkBoxAirConditioned;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.imageViewProfilePicture)
    ImageView imageViewProfilePicture;
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
    @Bind(R.id.progressBar)
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


        // setup collapsing toolbar
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (verticalOffset == 0)
                    ViewCompat.animate(imageViewProfilePicture).scaleX(1).scaleY(1).setDuration(300).start();
                if (verticalOffset < -30)
                    ViewCompat.animate(imageViewProfilePicture).scaleX(0).scaleY(0).setDuration(300).start();

            }

        });
        appBarLayout.setExpanded(false, false);
        imageViewProfilePicture.setScaleX(0);
        imageViewProfilePicture.setScaleY(0);


        // load data
        loadProfile();

        // register to profile update events
        EventBus.getDefault().register(this);


    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                appBarLayout.setExpanded(false, true);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        UserProfileActivity.super.onBackPressed();
                    }
                }, 300);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (editProfileFragment != null && editProfileFragment.isAdded())
            editProfileFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.buttonViewFeedback)
    void openFeedbackActivity()
    {
        Intent intent = new Intent(this, ViewFeedbackActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void onClick()
    {
        String currentUserId = new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId();
        boolean sameUser = currentUserId.equals(userId);
        if (sameUser)
        {
            //  show the edit profile dialog if it's the profile of this user
            //done by raafat
            if (userId.equals(new AuthenticationAPIController(getApplicationContext()).getCurrentUser().getUserId()))
            {
                editProfileFragment = new EditProfileFragment();
                editProfileFragment.show(getSupportFragmentManager(), getString(R.string.title_edit_profile));
            }
        } else
        {
            Intent chatIntent = new Intent(this, ChatActivity.class);
            chatIntent.putExtra(Constants.USER_ID, userId);
            startActivity(chatIntent);
        }

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

                //  set profile data to views
                textViewUserName.setText(ValidationUtils.correct(user.getFirstName()) + " " + ValidationUtils.correct(user.getLastName()));
                textViewEmail.setText(ValidationUtils.correct(user.getEmail()));
                textViewPhoneNumber.setText(ValidationUtils.correct(user.getPhoneNumber()));
                Residence.setText(ValidationUtils.correct(user.getResidence()));
                no_of_rides.setText(ValidationUtils.correct(user.getNo_of_rides()));
                points.setText(ValidationUtils.correct(user.getPoints()));
                if (user.getdob() != null)
                {
                    int age = TimeUtils.getAge(user.getdob());
                    textViewAge.setText(age + "");
                }
                textViewBio.setText(ValidationUtils.correct(user.getBio()));

                if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
                    Picasso.with(getApplicationContext()).
                            load(user.getProfilePictureUrl())
                            .placeholder(R.drawable.ic_user_large)
                            .into(imageViewProfilePicture, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                {
                                    MaterialImageLoading.animate(imageViewProfilePicture).setDuration(Constants.IMAGE_LOAD_ANIMATION_DURATION).start();
                                }

                                @Override
                                public void onError()
                                {

                                }
                            });

                textViewCarModel.setText(ValidationUtils.correct(user.getCarDetails().getModel()));
                textViewCarYear.setText(ValidationUtils.correct(user.getCarDetails().getYear()));
                textViewCarPlateNumber.setText(ValidationUtils.correct(user.getCarDetails().getPlateNumber()));
                checkBoxAirConditioned.setVisibility(View.VISIBLE);
                checkBoxAirConditioned.setChecked(user.getCarDetails().isConditioned());

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


    @Override
    public void onBackPressed()
    {
        appBarLayout.setExpanded(false, true);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                UserProfileActivity.super.onBackPressed();
            }
        }, 300);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUserProfileEvent updateUserProfileEvent)
    {
        // check it's for this user
        if (!updateUserProfileEvent.getUserId().equals(userId))
            return;

        loadProfile();
    }
}
