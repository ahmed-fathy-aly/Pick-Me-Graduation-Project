package com.asu.pick_me_graduation_project.model;

import com.asu.pick_me_graduation_project.model.Phone;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ahmed on 12/20/2015.
 * TODO : ONLY FOR TESTING
 */
public class Contact
{
    /* fields */
    String name;
    String id;
    Phone phone;

    /* constructor */
    public Contact()
    {
    }

    /* setters and getters */

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Phone getPhone()
    {
        return phone;
    }

    public void setPhone(Phone phone)
    {
        this.phone = phone;
    }

    /* methods */

    /**
     * parses a contact from a json object
     * ex :
     * <p/>
     * {
     * "id": "c200",
     * "name": "Ravi Tamada",
     * "phone": {
     * "mobile": "+91 0000000000",
     * "home": "00 000000",
     * "office": "00 000000"
     * }
     * }
     */
    public static Contact fromJson(JSONObject json) throws JSONException
    {
        Contact contact = new Contact();

        contact.setId(json.getString("id"));
        contact.setName(json.getString("name"));
        JSONObject phoneJson = json.getJSONObject("phone");

        Phone phone = Phone.fromJson(phoneJson);
        contact.setPhone(phone);

        return contact;
    }

    public String toString()
    {
        return name + " " + id + " " + phone.getMobile();
    }
}
