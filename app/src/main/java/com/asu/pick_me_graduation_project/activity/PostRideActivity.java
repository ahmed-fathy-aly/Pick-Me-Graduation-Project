package com.asu.pick_me_graduation_project.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.PostRidePagerAdapter;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostRideActivity extends BaseActivity
{


    /* UI */
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.indicator)
    InkPageIndicator indicator;
    @Bind(R.id.buttonNextOrSubmit)
    Button buttonNextOrSubmit;

    /* fields */
    private PostRidePagerAdapter postRidePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ride);
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.post_ride));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        // setup view pager
        postRidePagerAdapter = new PostRidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(postRidePagerAdapter);
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                buttonNextOrSubmit.setText(getString(position == 0 ? R.string.next :  R.string.submit));
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
    }

    @OnClick(R.id.buttonNextOrSubmit)
    void onNextOrSubmitClicked()
    {
        if (viewPager.getCurrentItem() == 0)
        {
            if (!postRidePagerAdapter.getChooseRouteFragment().checkDataEntered())
                return;
            viewPager.setCurrentItem(1, true);
            buttonNextOrSubmit.setText(getString(R.string.submit));
        }
    }

}
