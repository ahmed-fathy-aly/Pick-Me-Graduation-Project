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
    DrivingFeedback drivingFeedback;

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

    public DrivingFeedback getDrivingFeedback()
    {
        return drivingFeedback;
    }

    public void setDrivingFeedback(DrivingFeedback drivingFeedback)
    {
        this.drivingFeedback = drivingFeedback;
    }


}
