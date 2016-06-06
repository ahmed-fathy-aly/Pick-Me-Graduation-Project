package com.asu.pick_me_graduation_project.model;

/**
 * Created by ahmed on 2/16/2016.
 */
public class CarDetails
{
    String model;
    String year;
    String plateNumber;
    boolean conditioned;
    int numberOfSeats;
    int sizeOfBags;

    /* constructor */
    public CarDetails()
    {
    }

    /* getters and setters */

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getPlateNumber()
    {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber)
    {
        this.plateNumber = plateNumber;
    }

    public boolean isConditioned()
    {
        return conditioned;
    }

    public void setConditioned(boolean conditioned)
    {
        this.conditioned = conditioned;
    }

    public int getNumberOfSeats()
    {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats)
    {
        this.numberOfSeats = numberOfSeats;
    }

    public int getSizeOfBags()
    {
        return sizeOfBags;
    }

    public void setSizeOfBags(int sizeOfBags)
    {
        this.sizeOfBags = sizeOfBags;
    }
}
