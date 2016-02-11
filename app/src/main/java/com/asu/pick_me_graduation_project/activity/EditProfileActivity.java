package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.asu.pick_me_graduation_project.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditProfileActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        toolbar.setTitle(getString(R.string.title_edit_profile));

    }
}
