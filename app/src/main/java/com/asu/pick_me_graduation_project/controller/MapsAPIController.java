package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.GetRouteCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 4/27/2016.
 */
public class MapsAPIController
{
    Context context;

    public MapsAPIController(Context context)
    {
        this.context = context;
    }

    public void getRoute(List<LatLng> latlngs, final GetRouteCallback callback)
    {
        String url = getUrl(latlngs);
        Ion.with(context)
                .load("GET", url)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check if failed
                        if (e != null)
                        {
                            callback.fail(e.getMessage());
                            return;
                        }

                        // parse response
                        try
                        {
                            JSONObject response = new JSONObject(result);
                            String encodedPolyine = response.getJSONArray("routes")
                                    .getJSONObject(0)
                                    .getJSONObject("overview_polyline")
                                    .getString("points");
                            callback.success(decodePolyine(encodedPolyine));
                        } catch (Exception e1)
                        {
                            callback.fail(e1.getMessage());
                        }
                    }
                });
    }

    /**
     * api url to get route between source, destination and waypoints in between
     *
     * @param latlngs > 2. source is at the first index, destination at the last
     */
    public String getUrl(List<LatLng> latlngs)
    {
        StringBuilder strb = new StringBuilder();
        strb.append("http://maps.googleapis.com/maps/api/directions/json");
        strb.append("?origin=" + latlngs.get(0).latitude + "," + latlngs.get(0).longitude);
        strb.append("&destination=" + latlngs.get(latlngs.size() - 1).latitude + "," + latlngs.get(latlngs.size() - 1).longitude);
        if (latlngs.size() > 2)
        {
            strb.append("&waypoints=");
            for (int i = 1; i < latlngs.size() - 1; i++)
            {
                if (i != 1)
                    strb.append("|");
                strb.append("via:" + latlngs.get(i).latitude + "," + latlngs.get(i).longitude);
            }
        }

        return strb.toString();
    }

    /**
     * decodes a polyline string to a list of LatLng
     */
    public List<LatLng> decodePolyine(String encoded)
    {
        return PolyUtil.decode(encoded);
    }
}
