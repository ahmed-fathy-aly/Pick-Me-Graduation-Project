package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.view.GenericMapsView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * chooses source and destination locations
 */
public class ChooseRouteFragment extends Fragment
{

    /* UI */
    @Bind(R.id.imageViewCheckSource)
    ImageView imageViewCheckSource;
    @Bind(R.id.imageViewCheckDestination)
    ImageView imageViewCheckDestination;
    @Bind(R.id.map)
    GenericMapsView genericMapsView;

    /* fields */
    public ChooseRouteFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_route, container, false);
        ButterKnife.bind(this, view);

        // go to my location
        genericMapsView.goToMyLocation();

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /* listeners */
    @OnClick(R.id.layoutSelectSource)
    void selectSource()
    {
        // get the selected position
        LatLng latLng = genericMapsView.getCurrentLatlng();
        if (latLng == null)
            return;

        // add a marker
        genericMapsView.addMarker("Source", "Source", BitmapDescriptorFactory.HUE_ORANGE, latLng);

        // change check color
        imageViewCheckSource.setImageResource(R.drawable.ic_check_green);

        // draw route
        drawRoute();
    }

    @OnClick(R.id.layoutSelectDestination)
    void selectDestination()
    {
        // get the selected position
        LatLng latLng = genericMapsView.getCurrentLatlng();
        if (latLng == null)
            return;

        // add a marker
        genericMapsView.addMarker("Destination", "Destination", BitmapDescriptorFactory.HUE_GREEN, latLng);

        // change check color
        imageViewCheckDestination.setImageResource(R.drawable.ic_check_green);

        // draw route
        drawRoute();
    }


    /* methods */

    /**
     * draws the route between source and destination if possible
     */
    private void drawRoute()
    {
        // get source and destination selected
        LatLng source = genericMapsView.getMarkerPosition("Source");
        LatLng destination = genericMapsView.getMarkerPosition("Destination");
        if (source == null || destination == null)
            return;

        // draw route
        genericMapsView.drawRoute(Arrays.asList(source, destination)
                , getResources().getColor(R.color.primary));
    }

    /**
     * checks that a source and a destinatoin are entered
     * shows a toast if one of them is not entered
     *
     * @return true if both are entered
     */
    public boolean checkDataEntered()
    {
        if (genericMapsView.getMarkerPosition("Source") == null)
        {
            Toast.makeText(getContext(), getString(R.string.select_source), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (genericMapsView.getMarkerPosition("Destination") == null)
        {
            Toast.makeText(getContext(), getString(R.string.select_destination), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
}
