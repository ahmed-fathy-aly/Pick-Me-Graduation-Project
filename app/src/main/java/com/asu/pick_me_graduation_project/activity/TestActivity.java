package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Random;

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
        Log.e("Game", "creating community");

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
        Log.e("Game", "editing profile");
        Ion.with(this)
                .load("PUT", "http://196.202.117.93:8081/api/EditProfile")
                .addHeader("Authorization", "Bearer eyJhbGciOiJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGRzaWctbW9yZSNyc2Etc2hhMjU2IiwidHlwIjoiSldUIn0.eyJuYmYiOjE0NTc4NzY5MDYsImV4cCI6MTQ4OTQxMjkwNSwiaWF0IjoxNDU3ODc2OTA2LCJ1bmlxdWVfbmFtZSI6IjQzIiwiaXNzIjoiRXhhbXBsZUlzc3VlciIsImF1ZCI6IkV4YW1wbGVBdWRpZW5jZSJ9.EZT4scjIJAlTQ56FP0Bcr_AI1bo1h6hfUa7oeKaXEqc53JGW90N8RP1zEWOBHB47PrHndOlUjefCkEKfqyhzvRJ5TRwpc30BeE73H5QI5WlTVrDlvM94oIVDzjYBEDVetYvzMP1TGFuHmTI8KLbmy6272rIIWxt9IpoArLqfRboeJ99tzIdXLK8Dkwpxxd0rLIdgHYi3Z2rZWRV4108MrbpWthtAF9Oqk56ANOYopOGhO4OlTE2WZPoZHjQzPWPAzklCFnu4qw_RDPhFpWlxGYZ5KjShaIcvmHoAIBXFCLRF2-LSTDNKgyFYEelpAK9yX8rMGYQbT6l6xuN5vrpQ-w")
                .addHeader("Content-Type", "application/json")
                .setStringBody("{\n" +
                        "    \n" +
                        "\"bio\": \"blabla5\",\n" +
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
                            Toast.makeText(getApplicationContext(), "error " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Game", "error " + e.getMessage());
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "response " + result, Toast.LENGTH_LONG).show();
                            Log.e("Game", "test edit profile result " + result);
                        }
                    }
                });
    }

    @OnClick(R.id.buttonSignUp)
    void signUp()
    {
        Log.e("Game", "signing up");

        Ion.with(this)
                .load("http://196.202.117.93:8081/api/sign_up")
                .addHeader("Content-Type", "application/json")
                .setStringBody("{\n" +
                        "    \"firstName\": \"name\",\n" +
                        "    \"lastName\":\"B\",\n" +
                        "    \"email\":\"test" + new Random().nextInt(1000) + "@mail.com\",\n" +
                        "    \"gender\": false,\n" +
                        "    \"password\": \"password\"\n" +
                        "    \n" +
                        "}")
                .asString()
                .setCallback(new FutureCallback<String>()
                {
                    @Override
                    public void onCompleted(Exception e, String result)
                    {
                        if (e != null)
                        {
                            Toast.makeText(getApplicationContext(), "error " + e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Game", "error " + e.getMessage());
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "response " + result, Toast.LENGTH_LONG).show();
                            Log.e("Game", "test sign up result " + result);
                        }
                    }
                });
    }
}
