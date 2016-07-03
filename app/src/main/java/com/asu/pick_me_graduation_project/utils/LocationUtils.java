package com.asu.pick_me_graduation_project.utils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 7/3/2016.
 */
public class LocationUtils
{

    /**
     * the markers in the middle should be added first then the first marker then the last marker
     * to make sure the last and first marker would be above the markers in the middle if overlapped
     */
    public static List<Integer> getMarkerAddingOrder(int locationsSize)
    {
        ArrayList<Integer> markersAddingOrder = new ArrayList<>();

        for (int i = 1; i < locationsSize- 1; i++)
            markersAddingOrder.add(i);
        if (locationsSize > 0)
            markersAddingOrder.add(0);
        if (locationsSize> 1)
            markersAddingOrder.add(locationsSize- 1);

        return markersAddingOrder;
    }

    /**
     * colors each location of the route
     * first location -> green
     * last location -> red
     * user's location -> cyan
     * other -> orange
     */
    public static float getMarkerColor(int position, int locationsSize, boolean uniqueUser)
    {
        float color = BitmapDescriptorFactory.HUE_ORANGE;

        if (uniqueUser)
            color = BitmapDescriptorFactory.HUE_CYAN;
        if (position == 0)
            color = BitmapDescriptorFactory.HUE_GREEN;
        else if (position == locationsSize - 1)
            color = BitmapDescriptorFactory.HUE_RED;

        return color;
    }
}
