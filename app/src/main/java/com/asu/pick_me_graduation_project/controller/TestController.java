package com.asu.pick_me_graduation_project.controller;

import android.content.Context;

import com.asu.pick_me_graduation_project.callback.TestGetCalback;
import com.asu.pick_me_graduation_project.callback.TestPostCallback;
import com.asu.pick_me_graduation_project.model.Contact;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 12/20/2015.
 * TODO : ONLY for testing
 */
public class TestController
{
    /* fields */
    Context context;

    /* constructor */
    public TestController(Context context)
    {

        this.context = context;
    }

    /* methods */

    /**
     * makes a get request to get the data at http://api.androidhive.info/contacts/ (open it in web browse to view its contents)
     * after it makes the GET using ION library it parses the String received to form the data
     * finally it invokes the callback (the thing that should be invoked with the data parsed )
     */
    public void makeGetRequest(final TestGetCalback calback)
    {
        String url = "http://api.androidhive.info/contacts/";
        Ion.with(context)
                .load("GET", url)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors ( e will be null if successful, not null if there was an error
                        if (e != null)
                        {
                            calback.fail(e.getMessage());
                            return;
                        }

                        // parse the contacts
                        try
                        {
                            JSONObject resultJson = new JSONObject(result);
                            JSONArray contactsJson = resultJson.getJSONArray("contacts");
                            List<Contact> contacts = new ArrayList<>();
                            for (int i = 0; i < contactsJson.length(); i++)
                                contacts.add(Contact.fromJson(contactsJson.getJSONObject(i)));
                            calback.success(contacts);
                        } catch (JSONException e1)
                        {
                            e1.printStackTrace();
                            calback.fail(e1.getMessage());
                        }
                    }
                });
    }

    /**
     * makes a post request to
     * if the request is successful it should return some string
     * don't care about what data it actually retuned, in the app our post requests return nothing
     * (we just care that they are succesful)
     * after the request is done the callback should be invoked
     */
    public void makePost(final TestPostCallback calback)
    {
        String url = "http://httpbin.org/post";
        Ion.with(context)
                .load("POST", url)
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        // check errors ( e will be null if successful, not null if there was an error
                        if (e != null)
                        {
                            calback.fail(e.getMessage());
                            return;
                        }

                        calback.success(result);
                    }
                });
    }

}
