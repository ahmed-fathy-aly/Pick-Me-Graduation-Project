package asu.com.pick_me_graduation_project;

import android.util.Log;

import com.asu.pick_me_graduation_project.controller.MapsAPIController;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class MapsControllerTest
{
    @Test
    public void testGetUrl() throws Exception
    {
        MapsAPIController controller = new MapsAPIController(null);
        List<LatLng> latLngs = Arrays.asList(
                new LatLng(37.0157,37.18315),
                new LatLng(37.01583,37.18677),
                new LatLng(37.01693,37.18675),
                new LatLng(37.01384,37.18705));

        String expected = "http://maps.googleapis.com/maps/api/directions/json?origin=37.0157,37.18315&destination=37.01384,37.18705&waypoints=via:37.01583,37.18677|via:37.01693,37.18675";
        String result = controller.getUrl(latLngs);
        assertEquals(expected, result);

    }

    @Test
    public void testDecodePolyline()
    {
        MapsAPIController controller = new MapsAPIController(null);
        String polyline = "cs|`Foi}aFkFyJEoJ";

        List<LatLng> result = controller.decodePolyine(polyline);
        assertEquals(3, result.size());
        assertEquals(37.01688, result.get(1).latitude, 0.00001);
        assertEquals(37.18312, result.get(0).longitude, 0.00001);

    }
}