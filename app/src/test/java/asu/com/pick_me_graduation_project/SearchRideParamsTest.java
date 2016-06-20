package asu.com.pick_me_graduation_project;

import com.asu.pick_me_graduation_project.model.Community;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.SearchRideParams;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 */
public class SearchRideParamsTest
{
    @Test
    public void testLocationsStrCorrect1() throws Exception
    {
        // add 3 communities
        Community c1= new Community();
        c1.setId("1");
        Community c2= new Community();
        c2.setId("2");
        Community c3= new Community();
        c3.setId("3");

        String result= Community.getCommunitiesStr(Arrays.asList(c1, c2, c3));
        String expected = "[1,2,3]";
        assertEquals(expected, result);
    }

    @Test
    public void testLocationsStrCorrect2() throws Exception
    {
        // no communities
        String result= Community.getCommunitiesStr(new ArrayList<Community>());
        String expected = "[]";
        assertEquals(expected, result);
    }



}