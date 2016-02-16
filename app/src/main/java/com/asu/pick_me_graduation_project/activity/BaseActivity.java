package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class BaseActivity extends AppCompatActivity
{

    /* UI */
    protected Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * sets up the navigation drawer in the actibity
     *
     * @param toolbar the activitie's toolbar, to add a burger icon
     */
    protected Drawer setupNavigationBar(final BaseActivity activity, Toolbar toolbar)
    {
        // find the id of that activity
        int currentActivityId = -1;
        if (activity.getClass().equals(MainActivity.class))
            currentActivityId = 1;
        else if (activity.getClass().equals(UserProfileActivity.class))
            currentActivityId = 2;
        else if (activity.getClass().equals(SearchUsersActivity.class))
            currentActivityId = 3;
        final int finalCurrentActivityId = currentActivityId;

        // get the current user
        final User user = new AuthenticationAPIController(activity).getCurrentUser();

        // profile header
        ProfileDrawerItem userProfile = new ProfileDrawerItem().withName(user.getFirstName()).withEmail(user.getEmail()).withNameShown(true);
        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(userProfile)
                .withAlternativeProfileHeaderSwitching(false)
                .withHeaderBackground(R.drawable.drawer_profile_header)
                .withCompactStyle(true)
                .build();


        // build navigation drawer
        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(accountHeader)
                .withToolbar(toolbar);
        builder.addDrawerItems(new PrimaryDrawerItem().withIdentifier(1).withName("DashBoard")
                , new PrimaryDrawerItem().withIdentifier(2).withName("My Profile")
                , new PrimaryDrawerItem().withIdentifier(3).withName("Find User")
                , new DividerDrawerItem()
                , new PrimaryDrawerItem().withIdentifier(10).withName("Log Out"));

        builder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
        {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
            {
                // if clicked on the same activity we are on do nothing
                int id = drawerItem.getIdentifier();
                if (id == finalCurrentActivityId)
                    return true;

                // launch another activity
                if (id < 10)
                {
                    // select the activity to launch
                    final Intent intent = new Intent();
                    if (id == 2)
                    {
                        intent.setClass(activity, UserProfileActivity.class);
                        intent.putExtra(Constants.USER_ID, user.getUserId());
                    } else if (id == 3)
                    {
                        intent.setClass(activity, SearchUsersActivity.class);
                    }

                    // launch the activity after some milliseconds to show the drawer close animation
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            activity.startActivity(intent);
                            drawer.setSelection(1, false);
                        }
                    }, 500);

                } else if (id == 10)
                {
                    // log out
                    AuthenticationAPIController controller = new AuthenticationAPIController(activity);
                    controller.logOut();
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

                return true;
            }
        });

        // select the current activity
        drawer = builder.build();
        if (currentActivityId > 1)
            drawer.setSelection(currentActivityId);
        return drawer;
    }

    @Override
    public void onBackPressed()
    {
        if (drawer != null && drawer.isDrawerOpen())
            drawer.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
