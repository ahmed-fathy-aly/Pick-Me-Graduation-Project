package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetFeedbackFormCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.FeedbackAPIController;
import com.asu.pick_me_graduation_project.model.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedBackActivity extends AppCompatActivity {
    /* UI */
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;

    @Bind(R.id.yesButton)
    RadioButton yesButton;
    @Bind(R.id.noButton)
    RadioButton noButton;
    @Bind(R.id.IfNo)
    TextView ifno;
    @Bind(R.id.theDifferences)
    TextView difference;
    @Bind(R.id.checkboxCarModel)
    CheckBox carModel;
    @Bind(R.id.checkboxCarPlateNo)
    CheckBox carPlateNo;
    @Bind(R.id.checkboxCarAc)
    CheckBox carAc;
    @Bind(R.id.driverCard)
    CardView driverContent;
    @Bind(R.id.feedbackContent)
    LinearLayout feedbackContent;
    /* fields */
    private String rideId;
    FeedbackAPIController controller = new FeedbackAPIController(this);
    AuthenticationAPIController controller2 = new AuthenticationAPIController(this);
    RadioGroup radioGroup = (RadioGroup) findViewById(R.id.sameCarYorN);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);


        // get data from intent (later)
        rideId = "2";

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.feedback));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == noButton.getId()) {
                    ifno.setVisibility(View.VISIBLE);
                    difference.setVisibility(View.VISIBLE);
                    carModel.setVisibility(View.VISIBLE);
                    carPlateNo.setVisibility(View.VISIBLE);
                    carAc.setVisibility(View.VISIBLE);
                } else {
                    ifno.setVisibility(View.GONE);
                    difference.setVisibility(View.GONE);
                    carModel.setVisibility(View.GONE);
                    carPlateNo.setVisibility(View.GONE);
                    carAc.setVisibility(View.GONE);
                }
            }
        });

        ratingBar.setRating(ratingBar.getNumStars());

        // get data
        getFeedBackForm();

    }


    /**
     * TODO
     * - get the users of that ride
     * - inflate a row for each user
     */
    private void getFeedBackForm() {


        // feedback.setDriverSpecificFeedback();
        controller.getFeedbackForm(controller2.getTokken(), rideId, new GetFeedbackFormCallback() {
            @Override
            public void success(List<User> users, String riderId) {
                if (controller2.getCurrentUser().getUserId().equals(riderId)) {
                    driverContent.setVisibility(View.GONE);
                }
                for (int i = 0; i <= users.size(); i++) {

                    String s = users.get(i).getUserId();
                    if (!s.equals(controller2.getCurrentUser().getUserId())) {
                        inflateRow(users.get(i));

                    }

                }
                inflatebutton();
            }

            @Override
            public void fail(String message) {
                Snackbar.make(feedbackContent, message, Snackbar.LENGTH_SHORT).show();

            }
        });

    }
    // i should get the number of passengers inthis ride and check his id if not equal current user
    // id inflate new view


    /**
     * TODO
     * gather the data as a list of Feedback objects
     * send it to backend
     */
    @OnClick(R.id.submitButton)
    public void onclick() {
        sendFeedback();
    }

    void sendFeedback() {

    }

    /**
     * TODO
     * inflate the view
     * map the user to the view so we can retrieve the view later
     * add the view to the linear layout
     */
    private void inflateRow(User user) {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedbackLayout);
        final View PassengerCard = LayoutInflater.from(this).inflate(R.layout.passenger_row, null);
        linearLayout.addView(PassengerCard);
        Log.e("Game", "card added");

    }

    private void inflatebutton() {
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedbackLayout);
        final View submitButton = LayoutInflater.from(this).inflate(R.layout.submit_button, null);
        linearLayout.addView(submitButton);
        Log.e("Game", "button added");
    }
}
