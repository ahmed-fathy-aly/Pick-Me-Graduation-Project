package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.callback.TestGetCalback;
import com.asu.pick_me_graduation_project.callback.TestPostCallback;
import com.asu.pick_me_graduation_project.controller.TestController;
import com.asu.pick_me_graduation_project.model.Contact;

import java.util.List;

/**
 * TODO : ONLY FOR TESTING
 */
public class TestActivity extends AppCompatActivity
{

    /* Ui */
    View content;
    Button buttonGET, buttonPOST;
    ProgressBar progressBar;
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // reference views
        content = findViewById(R.id.content);
        buttonGET = (Button) findViewById(R.id.buttonGET);
        buttonPOST = (Button) findViewById(R.id.buttonPOST);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewResult = (TextView) findViewById(R.id.textViweResult);

        // add listeners
        buttonGET.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makeGETRequest();
            }
        });
        buttonPOST.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                makePOSTRequest();
            }
        });

    }

    /**
     * test making a GET request
     */
    public void makeGETRequest()
    {
        progressBar.setVisibility(View.VISIBLE);
        TestController controller = new TestController(this);
        controller.makeGetRequest(new TestGetCalback()
        {
            @Override
            public void success(List<Contact> contacts)
            {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                // make a string that shows the books
                String result = "";
                for (int i = 0; i < Math.max(10, contacts.size()); i++)
                    result = result + contacts.get(i).toString() + "\n";
                textViewResult.setText(result);

            }

            @Override
            public void fail(String message)
            {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * test making a GET request
     */
    public void makePOSTRequest()
    {
        progressBar.setVisibility(View.VISIBLE);
        TestController controller = new TestController(this);
        controller.makePost(new TestPostCallback()
        {
            @Override
            public void success(String postResult)
            {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(content, getString(R.string.success), Snackbar.LENGTH_SHORT).show();

                textViewResult.setText(postResult);

            }

            @Override
            public void fail(String message)
            {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
