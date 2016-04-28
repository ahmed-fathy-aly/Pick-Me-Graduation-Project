package com.asu.pick_me_graduation_project.callback;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by ahmed on 4/27/2016.
 */
public interface GetRouteCallback
{
    void  success(List<LatLng> latLngs);

    void fail(String error);
}
