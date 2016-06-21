package com.asu.pick_me_graduation_project.model;

/**
 * Created by ahmed on 6/14/2016.
 */
public class Feedback
{
    /* fields */
    String userId;
    String comment;
    int attitude;
    int punctuality;
    DriverSpecificFeedback driverSpecificFeedback;

    /* constructor */
    public Feedback()
    {
    }

    /* getters and setters */
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public int getAttitude()
    {
        return attitude;
    }

    public void setAttitude(int attitude)
    {
        this.attitude = attitude;
    }

    public int getPunctuality()
    {
        return punctuality;
    }

    public void setPunctuality(int punctuality)
    {
        this.punctuality = punctuality;
    }

    public DriverSpecificFeedback getDriverSpecificFeedback()
    {
        return driverSpecificFeedback;
    }

    public void setDriverSpecificFeedback(DriverSpecificFeedback driverSpecificFeedback)
    {
        this.driverSpecificFeedback = driverSpecificFeedback;
    }

    public class DriverSpecificFeedback
    {
        int driving;
        boolean sameCar;
        boolean sameModel;
        boolean samePlate;
        boolean sameAc;

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
    }
}
