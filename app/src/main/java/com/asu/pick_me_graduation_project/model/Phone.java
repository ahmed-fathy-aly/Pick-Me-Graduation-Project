package com.asu.pick_me_graduation_project.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmed on 12/20/2015.
 * TODO : ONLY FOR TESTING
 */
public class Phone
{
    /* methods */
    String mobile;
    String home;
    String office;

    /* constructors */
    public Phone()
    {

    }

    /* setters and getters */
    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getHome()
    {
        return home;
    }

    public void setHome(String home)
    {
        this.home = home;
    }

    public String getOffice()
    {
        return office;
    }

    public void setOffice(String office)
    {
        this.office = office;
    }

    /* methods */

    /**
     * parses a Phone from a Json object
     * ex :
     * {
     "mobile": "+91 0000000000",
     "home": "00 000000",
     "office": "00 000000"
     }
     */
    public static Phone fromJson(JSONObject json) throws JSONException
    {
        Phone phone = new Phone();

        phone.setHome(json.getString("home"));
        phone.setMobile(json.getString("mobile"));
        phone.setOffice(json.getString("office"));

        return phone;
    }
}
