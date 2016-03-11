package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.asu.pick_me_graduation_project.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.buttonCreateCommunity)
    void createCommunity()
    {
        JsonObject json = new JsonObject();
        json.addProperty("communityName", "test new community");
        json.addProperty("description", "desc");
        Ion.with(this)
                .load("http://pickmeasu.azurewebsites.net/api/Create_Community")
                .addHeader("Authorization", "Bearer eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2IiwidHlwIjoiSldUIn0.eyJuYmYiOjE0NTc3MjA5MDUsImV4cCI6MTQ4OTI1NjkwNCwiaWF0IjoxNDU3NzIwOTA1LCJ1bmlxdWVfbmFtZSI6IjQzIiwiaXNzIjoiRXhhbXBsZUlzc3VlciIsImF1ZCI6IkV4YW1wbGVBdWRpZW5jZSJ9.eoya1ToYFlPkPh7vDeeRDOJY3eBjV0DL58NPOp9mrHC8vDozamtdUCUoMOjKqT1mgfu1_MAz-pU2T-t9GJsAqfI6j7OUXHzHEICSbgdmexnE85QpNX7-a-BTRvap9gNKPFm3BsDoNo6vSOCT-WSRggwdNl7ScJva9ylcB0fAsIq2jEJajPCq7bpHv8m8tV6UvF1x9G6RmYHoLo_53R64XD2FCH_TI-HdD6Y8s_cYjRxTpNJRlA9RVaymDP8ZqlWd7QBuApJGXpnSl6_RVkHN5GF4DBI1y1SYXloAR3vTMvqwB9NcrQ1JmXHFcYWmsA-cUyZI8W041HHSqeMn_1irAA")
                .addHeader("Content-Type", "application/json")
                .setStringBody("{\n" +
                        "    \n" +
                        "\"communityName\": \"New community 2\",\n" +
                        "\"description\" :\"blabla\",\n" +
                        "   \n" +
                        "}")
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        if (e != null)
                        {
                            Log.e("Game", "error " + e.getMessage());
                        } else
                            Log.e("Game", "test create community result " + result);
                    }
                });
    }

    @OnClick(R.id.buttonEditProfile)
    void editProfile()
    {
        // test edit profile

        JsonObject json2 = new JsonObject();
        json2.addProperty("bio", "new bio2");
        Ion.with(this)
                .load("http://pickmeasu.azurewebsites.net/api/EditProfile")
                .addHeader("Authorization", "Bearer eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2IiwidHlwIjoiSldUIn0.eyJuYmYiOjE0NTc3MjA5MDUsImV4cCI6MTQ4OTI1NjkwNCwiaWF0IjoxNDU3NzIwOTA1LCJ1bmlxdWVfbmFtZSI6IjQzIiwiaXNzIjoiRXhhbXBsZUlzc3VlciIsImF1ZCI6IkV4YW1wbGVBdWRpZW5jZSJ9.eoya1ToYFlPkPh7vDeeRDOJY3eBjV0DL58NPOp9mrHC8vDozamtdUCUoMOjKqT1mgfu1_MAz-pU2T-t9GJsAqfI6j7OUXHzHEICSbgdmexnE85QpNX7-a-BTRvap9gNKPFm3BsDoNo6vSOCT-WSRggwdNl7ScJva9ylcB0fAsIq2jEJajPCq7bpHv8m8tV6UvF1x9G6RmYHoLo_53R64XD2FCH_TI-HdD6Y8s_cYjRxTpNJRlA9RVaymDP8ZqlWd7QBuApJGXpnSl6_RVkHN5GF4DBI1y1SYXloAR3vTMvqwB9NcrQ1JmXHFcYWmsA-cUyZI8W041HHSqeMn_1irAA")
                .addHeader("Content-Type", "application/json")
                .setStringBody("{\n" +
                        "    \n" +
                        "\"bio\": \"blabla4\",\n" +
                        "\"carAC\" :\"true\",\n" +
                        "\"carModel\": \"\",\n" +
                        "\"carPlateNumber\":\"\",\n" +
                        "\"carYear\" : \"1980\"  ,\n" +
                        "\"dob\" :\"11-3-2016\"  ,\n" +
                        "\"fbLink\":\"https://www.facebook.com/fromapk\",\n" +
                        "\"firstName\" :\"From\",\n" +
                        "\"googleLink\" :\"https://plus.google.com/u/\",\n" +
                        "\"lastName\": \"Apk\",\n" +
                        "\"residence\" :\"in Bluestacks\",\n" +
                        "\"numberOfRides\" : \"0\",\n" +
                        "\"points\" : \"0\" \n" +
                        "   \n" +
                        "}")
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        if (e != null)
                        {
                            Log.e("Game", "error " + e.getMessage());
                        } else
                            Log.e("Game", "test edit profile result " + result);
                    }
                });
    }

}
