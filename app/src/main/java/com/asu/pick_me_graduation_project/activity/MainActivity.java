package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.asu.pick_me_graduation_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * blank activity :D
 */
public class MainActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setupNavigationBar(this, toolbar);
        toolbar.setTitle("Dashboard");
    }

}
