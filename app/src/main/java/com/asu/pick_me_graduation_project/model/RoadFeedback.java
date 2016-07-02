package com.asu.pick_me_graduation_project.model;

/**
 * Created by ahmed on 7/1/2016.
 */
public class RoadFeedback
{
    /* fields */
    int trafficGoodness;
    int routeSmoothness;

    /* getters and setters */
    public void setTrafficGoddness(int goodness)
    {
        trafficGoodness = goodness;
    }

    public int getTrafficGoodness()
    {
        return trafficGoodness;
    }

    public int getRouteSmoothness()
    {
        return routeSmoothness;
    }

    public void setRouteSmoothness(int routeSmoothness)
    {
        this.routeSmoothness = routeSmoothness;
    }
}
