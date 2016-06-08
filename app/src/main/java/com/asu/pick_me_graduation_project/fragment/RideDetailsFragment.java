package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.ContactUserView;
import com.asu.pick_me_graduation_project.view.GenericMapsView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RideDetailsFragment extends Fragment
{
    @Bind(R.id.timeChooserView)
    ContactUserView timeChooserView;
    @Bind(R.id.mapsView)
    GenericMapsView mapsView;
    @Bind(R.id.textViewDescription)
    TextView textViewDescription;
    @Bind(R.id.textViewTime)
    TextView textViewTime;
    @Bind(R.id.textViewNotes)
    TextView textViewNotes;
    @Bind(R.id.textViewCarModel)
    TextView textViewCarModel;
    @Bind(R.id.textViewCarYear)
    TextView textViewCarYear;
    @Bind(R.id.textViewCarPlateNumber)
    TextView textViewCarPlateNumber;
    @Bind(R.id.checkBoxAirConditioned)
    CheckBox checkBoxAirConditioned;
    @Bind(R.id.textViewFreeSeats)
    TextView textViewFreeSeats;
    @Bind(R.id.textViewNoSmoking)
    TextView textViewNoSmoking;
    @Bind(R.id.textViewLadiesOnly)
    TextView textViewLadiesOnly;

    /* UI */

    public RideDetailsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * updates the UI with this ride
     */
    public void setData(Ride ride)
    {
        User user = new AuthenticationAPIController(getContext()).getCurrentUser();

        // time
        timeChooserView.setData(user);

        // map
        // locations
        mapsView.reset();
        for (Location location : ride.getLocations())
        {
            mapsView.addMarker(
                    location.getId()
                    , ""
                    , BitmapDescriptorFactory.HUE_ORANGE
                    , new LatLng(location.getLatitude(), location.getLongitude()));
        }
        mapsView.fitMarkers();

        // route
        List<LatLng> latLngs = new ArrayList<>();
        for (Location location : ride.getLocations())
            latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        mapsView.drawRoute(latLngs, 0xFF00BCD4);

        // ride details
        textViewDescription.setText(ride.getDescription());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        textViewTime.setText(sdf.format(ride.getTime().getTimeInMillis()));
        textViewNotes.setText(ride.getNotes());
        String freeSeats = ride.getRideDetails().getNumberOfFreeSeats() == 1 ?
                ride.getRideDetails().getNumberOfFreeSeats() + " " + getString(R.string.free_seat)
                :
                ride.getRideDetails().getNumberOfFreeSeats() + " " + getString(R.string.free_seats);
        textViewFreeSeats.setText(freeSeats);
        textViewNoSmoking.setVisibility(ride.getRideDetails().isNoSmoking() ? View.VISIBLE : View.GONE);
        textViewLadiesOnly.setVisibility(ride.getRideDetails().isLadiesOnly() ? View.VISIBLE : View.GONE);

        // car details
        textViewCarModel.setText(ValidationUtils.correct(ride.getRideDetails().getCarDetails().getModel()));
        textViewCarYear.setText(ValidationUtils.correct(ride.getRideDetails().getCarDetails().getYear()));
        textViewCarPlateNumber.setText(ValidationUtils.correct(ride.getRideDetails().getCarDetails().getPlateNumber()));
        checkBoxAirConditioned.setVisibility(ride.getRideDetails().getCarDetails().isConditioned() ? View.VISIBLE : View.GONE);
        checkBoxAirConditioned.setChecked(ride.getRideDetails().getCarDetails().isConditioned());

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
