package asu.com.pick_me_graduation_project;

import android.app.DatePickerDialog;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;
import android.widget.DatePicker;

import com.asu.pick_me_graduation_project.view.GenericMapsView;
import com.google.android.gms.maps.model.LatLng;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

/**
 * Created by ahmed on 6/28/2016.
 */
public class CustomViewActions
{
    /**
     * moves a GenericMapView to a lat lng
     */
    public static ViewAction moveToLocation(int id, final double latitude, final double longitude)
    {
        return new ViewAction()
        {
            @Override
            public Matcher<View> getConstraints()
            {
                return isAssignableFrom(GenericMapsView.class);
            }

            @Override
            public String getDescription()
            {
                return "move to location";
            }

            @Override
            public void perform(UiController uiController, View view)
            {
                GenericMapsView genericMapsView = (GenericMapsView) view;
                LatLng latLng = new LatLng(latitude, longitude);
                genericMapsView.moveToLocation(latLng);
            }
        };
    }


}
