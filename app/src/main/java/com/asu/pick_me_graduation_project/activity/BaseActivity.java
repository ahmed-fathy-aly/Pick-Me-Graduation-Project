package com.asu.pick_me_graduation_project.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;


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
        final ProfileDrawerItem userProfile = new ProfileDrawerItem().withName(user.getFirstName()).
                withEmail(user.getEmail()).withIcon(user.getProfilePictureUrl());
        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(activity)
                .addProfiles(userProfile)
                .withAlternativeProfileHeaderSwitching(false)
                .withHeaderBackground(R.drawable.drawer_profile_header)
                .withCompactStyle(true)
                .build();

        Ion.with(getApplicationContext())
                .load(user.getProfilePictureUrl())
                .asBitmap()
                .setCallback(new FutureCallback<Bitmap>()
                {
                    @Override
                    public void onCompleted(Exception e, Bitmap result)
                    {
                        for (IProfile profile : accountHeader.getProfiles())
                            accountHeader.removeProfile(profile);
                        if (result != null)
                            userProfile.withIcon(result);
                        else
                            userProfile.withIcon(R.drawable.ic_user_small);
                        accountHeader.addProfiles(userProfile);
                    }
                });


        // build navigation drawer
        DrawerBuilder builder = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(accountHeader)
                .withToolbar(toolbar);
        builder.addDrawerItems(new PrimaryDrawerItem().withIdentifier(1).withName("DashBoard").withIcon(R.drawable.ic_menu_black_48dp)
                , new PrimaryDrawerItem().withIdentifier(2).withName("My Profile").withIcon(R.drawable.ic_face_black_48dp)
                , new PrimaryDrawerItem().withIdentifier(3).withName("Find User").withIcon(R.drawable.ic_search_black_48dp)
                , new PrimaryDrawerItem().withIdentifier(4).withName("Messages").withIcon(R.drawable.ic_chat_bubble_outline_black_48dp)
                , new PrimaryDrawerItem().withIdentifier(5).withName("Communities").withIcon(R.drawable.ic_people_outline_black_48dp)
                , new PrimaryDrawerItem().withIdentifier(6).withName("My Rides").withIcon(R.drawable.ic_people_outline_black_48dp)
                , new DividerDrawerItem()
                , new PrimaryDrawerItem().withIdentifier(10).withName("Log Out").withIcon(R.drawable.ic_exit_to_app_black_48dp));

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
                    }else if (id == 4)
                    {
                        intent.setClass(activity, RecentChatsActivity.class);
                    } else if (id == 5)
                    {
                        intent.setClass(activity, CommunitiesActivity.class);
                    }else if (id == 6)
                    {
                        intent.setClass(activity, MyRidesActivity.class);
                    }

                    // launch the activity after some milliseconds to show the drawer close animation
                    drawer.closeDrawer();
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

    protected  void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
