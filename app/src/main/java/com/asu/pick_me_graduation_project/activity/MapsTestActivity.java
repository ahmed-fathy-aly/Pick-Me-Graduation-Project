package com.asu.pick_me_graduation_project.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.fragment.MapsFragment;

public class MapsTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_test);

        // add maps fragment
        MapsFragment mapsFragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapContainer, mapsFragment)
                .commit();
    }
}
