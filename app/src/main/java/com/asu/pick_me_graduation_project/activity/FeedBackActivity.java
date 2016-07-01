package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetFeedbackFormCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.FeedbackAPIController;
import com.asu.pick_me_graduation_project.model.Feedback;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends AppCompatActivity
{
    /* UI */
    @Bind(R.id.content)
    View content;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.checkboxCarModel)
    CheckBox carModel;
    @Bind(R.id.checkboxCarPlateNo)
    CheckBox carPlateNo;
    @Bind(R.id.checkboxCarAc)
    CheckBox carAc;
    @Bind(R.id.driverCard)
    CardView driverCard;
    @Bind(R.id.radioGroupSameCar)
    RadioGroup radioGroupSameCar;
    @Bind(R.id.radioButtonYes)
    RadioButton radioButtonYes;
    @Bind(R.id.radioButtonNo)
    RadioButton radioButtonNo;
    @Bind(R.id.feedbackLayout)
    LinearLayout feedbackLayout;
    @Bind(R.id.textViewSameCar)
    TextView textViewSameCar;

    /* fields */
    private String rideId;
    FeedbackAPIController controller = new FeedbackAPIController(this);


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);


        // get data from intent (later)
        rideId = getIntent().getStringExtra(Constants.RIDE_ID);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.feedback));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // add listener to same car radio button
        radioGroupSameCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == R.id.radioButtonYes)
                {
                    textViewSameCar.setVisibility(View.VISIBLE);
                    carModel.setVisibility(View.VISIBLE);
                    carPlateNo.setVisibility(View.VISIBLE);
                    carAc.setVisibility(View.VISIBLE);
                } else
                {
                    textViewSameCar.setVisibility(View.GONE);
                    carModel.setVisibility(View.GONE);
                    carPlateNo.setVisibility(View.GONE);
                    carAc.setVisibility(View.GONE);
                }
            }
        });

        // get data
        getFeedBackForm();

    }

    /**
     * gather the data as a list of Feedback objects
     * send it to backend
     */
    @OnClick(R.id.submitButton)
    void sendFeedback()
    {
        // gather the data
        List<Feedback> feedbackList = new ArrayList<>();
        Feedback.DriverSpecificFeedback driverFeedback = null;
        Feedback.RouteFeedback roadFeedback = null;

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedbackLayout);
        for (int i = 0; i < linearLayout.getChildCount(); i++)
        {
            Feedback passengerFeedback = new Feedback();

            View card = linearLayout.getChildAt(i);
            passengerFeedback.setUserId(card.getTag().toString());
            RatingBar atittude = (RatingBar) card.findViewById(R.id.ratingAttitude);
            passengerFeedback.setAttitude(atittude.getNumStars());
            RatingBar punctuality = (RatingBar) card.findViewById(R.id.ratingPunctuation);
            passengerFeedback.setPunctuality(punctuality.getNumStars());
            feedbackList.add(passengerFeedback);
        }
        RatingBar driving = (RatingBar) findViewById(R.id.ratingBar);
        // RatingBar traffic = (RatingBar) findViewById(R.id.ratingBar4);
        //   roadFeedback.setTrafficGoddness(traffic.getNumStars());
        // RatingBar smoothness = (RatingBar) findViewById(R.id.ratingBar5);
        // roadFeedback.setRouteSmoothness(smoothness.getNumStars());
        driverFeedback.setDriving(driving.getNumStars());
        if (radioButtonYes.isChecked())
        {
            driverFeedback.setSameAc(true);
            driverFeedback.setSameModel(true);
            driverFeedback.setSamePlate(true);

        } else
        {
            CheckBox sameAc = (CheckBox) findViewById(R.id.checkboxCarAc);
            driverFeedback.setSameAc(!(sameAc.isChecked()));
            CheckBox sameModel = (CheckBox) findViewById(R.id.checkboxCarModel);
            driverFeedback.setSameModel((!sameModel.isChecked()));
            CheckBox samePlate = (CheckBox) findViewById(R.id.checkboxCarPlateNo);
            driverFeedback.setSamePlate(!(samePlate.isChecked()));
        }

        controller.postFeedback(
                new AuthenticationAPIController(this).getTokken()
                , new AuthenticationAPIController(this).getCurrentUser().getUserId()
                , rideId
                , feedbackList
                , driverFeedback
                , roadFeedback
                , new GenericSuccessCallback()
                {
                    @Override
                    public void success()
                    {
                    }

                    @Override
                    public void fail(String message)
                    {

                    }
                });

    }


    /**
     * - get the users of that ride
     * - inflate a row for each user
     */
    private void getFeedBackForm()
    {
        progressBar.setVisibility(View.VISIBLE);

        controller.getFeedbackForm(
                new AuthenticationAPIController(this).getTokken(),
                rideId,
                new GetFeedbackFormCallback()
                {
                    @Override
                    public void success(List<User> passengers, User driver)
                    {
                        progressBar.setVisibility(View.INVISIBLE);

                        // inflate driving  feedback
                        String currentUserId = new AuthenticationAPIController(FeedBackActivity.this).getCurrentUser().getUserId();
                        boolean currentUserDriver = currentUserId.equals(driver.getUserId());
                        driverCard.setVisibility(currentUserDriver ? View.GONE : View.VISIBLE);

                        // inflate driver user feedback
                        if (!currentUserDriver)
                            inflateRow(driver);

                        // inflate passengers feedback
                        for (int i = 0; i < passengers.size(); i++)
                            if (!currentUserId.equals(passengers.get(i).getUserId()))
                                inflateRow(passengers.get(i));

                    }

                    @Override
                    public void fail(String message)
                    {
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                }
        );


    }

    /**
     * inflate the view
     * map the user to the view so we can retrieve the view later
     * add the view to the linear layout
     */
    private void inflateRow(User user)
    {
        // inflate view and give its a tag as the user id so we can get the user id from the view
        final View rowUserFeedback = LayoutInflater.from(this).inflate(R.layout.row_passenger_feedback, null);
        rowUserFeedback.setTag(user.getUserId());

        // reference views
        TextView textViewUserName = (TextView) rowUserFeedback.findViewById(R.id.textViewUserName);
        ImageView imageViewUserPP = (ImageView) rowUserFeedback.findViewById(R.id.imageViewUserPP);

        // set values
        textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(this).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(imageViewUserPP);

        // add to layout
        feedbackLayout.addView(rowUserFeedback);
    }


}
