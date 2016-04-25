package com.asu.pick_me_graduation_project.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.asu.pick_me_graduation_project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A generic maps fragment
 * arguments :
 * ARG_START_WITH_MY_LOCATION : go to my location when once the map is loaded
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback
{

    /* constants */
    public static final String ARG_START_WITH_MY_LOCATION = "startWithMyLocation";

    /* UI */
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.imageViewCenterLocation)
    ImageView imageViewCenterLocation;

    /* fields */
    private GoogleMap googleMap;
    HashMap<String, Marker> markers;

    public MapsFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        markers = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        ButterKnife.bind(this, view);


        Log.e("Game", "get map yala ");
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        Log.e("Game", "map ready");
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // zoom to my location
        if (getArguments() != null && getArguments().getBoolean(ARG_START_WITH_MY_LOCATION, false))
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
            {
                @Override
                public void onMyLocationChange(Location location)
                {
                    LatLng myLocation = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
                            500));
                    googleMap.setOnMyLocationChangeListener(null);
                }
            });

        mapView.onResume();


    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /* public methods */

    /**
     * shows or hide the marker in the middle of the map
     */
    public void setCenterLocationVisible(boolean visible)
    {
        imageViewCenterLocation.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    /**
     * gets the location in the center of the map shown
     */
    public LatLng getCurrentLatlng()
    {
        if (googleMap != null)
            return googleMap.getCameraPosition().target;
        else
            return null;
    }

    /**
     * adds a marker on the map
     * if there's a marker with that id already, update its position
     */
    public void addMarker(String id, String title, float color, LatLng latLng)
    {
        if (!markers.containsKey(id))
        {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(color));
            markers.put(id, googleMap.addMarker(markerOptions));
        } else
        {
            Marker marker = markers.get(id);
            marker.setPosition(latLng);
            marker.setTitle(title);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
        }
    }

    /**
     * gets the position of a previously added marker
     * returns null if no marker with that id is found
     */
    public LatLng getMarkerPosition(String id)
    {
        if (!markers.containsKey(id))
            return null;

        Marker marker = markers.get(id);
        return marker.getPosition();
    }

    /**
     * draw the route containing between the given position 0 -> 1 -> 2...etc
     */
    public void drawRoute(List<LatLng> latLngs)
    {

    }
}
