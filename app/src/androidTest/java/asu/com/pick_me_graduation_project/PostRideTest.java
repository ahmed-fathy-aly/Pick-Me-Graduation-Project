package asu.com.pick_me_graduation_project;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.MainActivity;
import com.asu.pick_me_graduation_project.utils.TimeUtils;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
/**
 * opens the main activity
 * clicks the post button
 * enters the ride details and post a ride
 */
public class PostRideTest
{

    /* the parameters of the ride to be posted*/
    double sourceLatitude = 30.1012229;
    double sourceLongitude = 31.3063849;
    double destinationLatitude = 30.0649032;
    double destinationLongitude = 31.2785737;
    int year = 2017;
    int month = 1;
    int day = 27;
    int hour = 6;
    int minutes = 0;
    String currentTime = TimeUtils.convertToDatabaseTime(Calendar.getInstance());
    String description = "ride from automated test at " + currentTime;
    String notes = "ride from automated test at " + currentTime;
    String freeSeats = "3";
    String carModel = "bmw";
    String carYear = "2016";
    String carplate = "abc123";
    boolean disabledPrefered = false;

    @Rule
    public ActivityTestRule<MainActivity> dashBoardActivityRule = new ActivityTestRule<>(
            MainActivity.class, true, false);

    @Test
    public void testPostRide() throws InterruptedException
    {
        // launch the main activity
        Intent intent = new Intent();
        MainActivity mainActivity = dashBoardActivityRule.launchActivity(intent);

        // open the post ride activity
        onView(withId(R.id.buttonPostRide))
                .perform(click());
        Thread.sleep(2000);

        // select source
        onView(withId(R.id.map))
                .perform(CustomViewActions.moveToLocation(R.id.genericMapsView, sourceLatitude, sourceLongitude));
        Thread.sleep(1000);
        onView(withId(R.id.layoutSelectSource))
                .perform(click());

        // select destination
        onView(withId(R.id.map))
                .perform(CustomViewActions.moveToLocation(R.id.genericMapsView, destinationLatitude, destinationLongitude));
        Thread.sleep(1000);
        onView(withId(R.id.layoutSelectDestination))
                .perform(click());

        // go to the next screen
        onView(withText(R.string.next))
                .perform(click());

        // select date
        onView(withText(R.string.select_date))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(android.support.test.espresso.contrib.PickerActions.setDate(year, month, day));
        onView(withText("OK"))
                .perform(click());

        // select time
        onView(withText(R.string.select_time))
                .perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(android.support.test.espresso.contrib.PickerActions.setTime(hour, minutes));
        onView(withText("OK"))
                .perform(click());

        // details
        String currentTime = TimeUtils.convertToDatabaseTime(Calendar.getInstance());
        onView(withHint(R.string.description))
                .perform(scrollTo(), replaceText(description));
        onView(withHint(R.string.notes))
                .perform(scrollTo(), replaceText(notes));
        onView(withHint(R.string.free_seats))
                .perform(scrollTo(), replaceText(freeSeats));
        if (disabledPrefered)
            onView(withText(R.string.disabled_welcomed))
                    .perform(click());

        // car details
        onView(withHint(R.string.model))
                .perform(scrollTo(), replaceText(carModel));
        onView(withHint(R.string.car_year))
                .perform(scrollTo(), replaceText(carYear));
        onView(withHint(R.string.plate_numer))
                .perform(scrollTo(), replaceText(carplate));

        // post
        onView(withText(R.string.post))
                .perform(click());

        // post
        onView(withText(R.string.success))
                .inRoot(withDecorView(not(is(mainActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }


}