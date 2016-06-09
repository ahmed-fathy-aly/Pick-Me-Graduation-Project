package com.asu.pick_me_graduation_project.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ahmed on 6/6/2016.
 */
public class RideDetails implements Serializable
{
    /* fields */
    int numberOfFreeSeats;
    boolean noSmoking;
    boolean ladiesOnly;
    CarDetails carDetails;
    List<Community> filteredCommunities;

    /* Constructor */
    public  RideDetails()
    {
    }

    /* Getters and setters */

    public int getNumberOfFreeSeats()
    {
        return numberOfFreeSeats;
    }

    public void setNumberOfFreeSeats(int numberOfFreeSeats)
    {
        this.numberOfFreeSeats = numberOfFreeSeats;
    }

    public boolean isNoSmoking()
    {
        return noSmoking;
    }

    public void setNoSmoking(boolean smokingOnly)
    {
        this.noSmoking = smokingOnly;
    }

    public boolean isLadiesOnly()
    {
        return ladiesOnly;
    }

    public void setLadiesOnly(boolean ladiesOnly)
    {
        this.ladiesOnly = ladiesOnly;
    }

    public CarDetails getCarDetails()
    {
        return carDetails;
    }

    public void setCarDetails(CarDetails carDetails)
    {
        this.carDetails = carDetails;
    }

    public List<Community> getFilteredCommunities()
    {
        return filteredCommunities;
    }

    public void setFilteredCommunities(List<Community> filteredCommunities)
    {
        this.filteredCommunities = filteredCommunities;
    }
}
