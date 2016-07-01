package com.asu.pick_me_graduation_project.model;

/**
 * Created by ahmed on 7/1/2016.
 */
public class DrivingFeedback
{
    /* fields */
    String userId;
    int driving;
    boolean sameCar;
    boolean sameModel;
    boolean samePlate;
    boolean sameAc;

    /* setters and getters */
    public int getDriving()
    {
        return driving;
    }

    public void setDriving(int driving)
    {
        this.driving = driving;
    }

    public boolean isSameCar()
    {
        return sameCar;
    }

    public void setSameCar(boolean sameCar)
    {
        this.sameCar = sameCar;
    }

    public boolean isSameModel()
    {
        return sameModel;
    }

    public void setSameModel(boolean sameModel)
    {
        this.sameModel = sameModel;
    }

    public boolean isSamePlate()
    {
        return samePlate;
    }

    public void setSamePlate(boolean samePlate)
    {
        this.samePlate = samePlate;
    }

    public boolean isSameAc()
    {
        return sameAc;
    }

    public void setSameAc(boolean sameAc)
    {
        this.sameAc = sameAc;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
}
