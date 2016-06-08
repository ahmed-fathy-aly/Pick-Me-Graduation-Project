package com.asu.pick_me_graduation_project.view;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.GetRouteCallback;
import com.asu.pick_me_graduation_project.controller.MapsAPIController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A generic maps view
 */
public class GenericMapsView extends FrameLayout implements OnMapReadyCallback
{


    /* UI */
    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.imageViewCenterLocation)
    ImageView imageViewCenterLocation;

    /* fields */
    private GoogleMap googleMap;
    HashMap<String, Marker> markers;
    HashMap<String, MarkerOptions> unaddedMarkers;
    private Polyline polyline;
    boolean goToMyLocation = false;
    boolean fitMarkers = false;

    public GenericMapsView(Context context)
    {
        super(context);
        init();
    }

    public GenericMapsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public GenericMapsView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init()
    {
        // inflate layout
        inflate(getContext(), R.layout.view_generic_map, this);
        ButterKnife.bind(this);

        // init fields
        markers = new HashMap<>();
        unaddedMarkers = new HashMap<>();

        // init map view
        mapView.onCreate(new Bundle());
        mapView.getMapAsync(this);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
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
        if (goToMyLocation)
        {
            goToMyLocationNow();
            goToMyLocation = false;
        }

        // unadded markers
        for (String id : unaddedMarkers.keySet())
            markers.put(id, googleMap.addMarker(unaddedMarkers.get(id)));
        unaddedMarkers.clear();

        // fit markers
        if (fitMarkers)
        {
            fitMarkersNow();
            fitMarkers = false;
        }

        mapView.onResume();


    }


    private void goToMyLocationNow()
    {
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                LatLng myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(myLocation)
                        .zoom(15)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnMyLocationChangeListener(null);
            }
        });
    }


    private void fitMarkersNow()
    {
        Log.e("Game", "fitting " + markers.keySet());

        // get the bounds
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers.values())
            builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();

        // update the map
        int padding = 25;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);
    }


    /* public methods */

    /**
     * zooms to my current location
     */
    public void goToMyLocation()
    {
        if (googleMap != null)
            goToMyLocationNow();
        else
            goToMyLocation = true;
    }

    /**
     * shows or hide the center marker
     */
    public void setCenterMarkerShown(boolean shown)
    {
        imageViewCenterLocation.setVisibility(shown ? VISIBLE : GONE);
    }

    /**
     * draw a polyline consisting of a path of latlngs
     * removes any existing polylines
     */
    private void drawPolyline(List<LatLng> latLngs, int color)
    {
        // remove any existing polylines
        if (polyline != null)
            polyline.remove();

        // draw the new polyline
        PolylineOptions polyLineOptions = new PolylineOptions();
        polyLineOptions.color(color);
        for (LatLng latlng : latLngs)
            polyLineOptions.add(latlng);
        polyline = googleMap.addPolyline(polyLineOptions);
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
            if (googleMap != null)
                markers.put(id, googleMap.addMarker(markerOptions));
            else
                unaddedMarkers.put(id, markerOptions);
        } else
        {
            Marker marker = markers.get(id);
            marker.setPosition(latLng);
            marker.setTitle(title);
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
        }
    }

    /**
     * zooms to fit all the makers
     */
    public void fitMarkers()
    {
        if (googleMap != null)
            fitMarkersNow();
        else
            fitMarkers = true;
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
    public void drawRoute(final List<LatLng> latLngs, final int color)
    {
        // make a request to get the route
        MapsAPIController controller = new MapsAPIController(getContext());
        controller.getRoute(latLngs, new GetRouteCallback()
        {
            @Override
            public void success(List<LatLng> latLngs)
            {
                drawPolyline(latLngs, color);
            }

            @Override
            public void fail(String error)
            {
                Log.e("Game", "Get route error " + error);
            }
        });
    }

    /**
     * removes all markers and routes
     */
    public void reset()
    {
        // remove markers
        for (Marker marker : markers.values())
            marker.remove();
        markers.clear();
        unaddedMarkers.clear();

        // remove polyine
        if (polyline != null)
            polyline.remove();
    }
}
