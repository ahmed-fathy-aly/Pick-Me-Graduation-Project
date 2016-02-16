package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.UsersAdapter;
import com.asu.pick_me_graduation_project.callback.SearchUserCallback;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchUsersActivity extends BaseActivity implements UsersAdapter.Listener
{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    SearchView searchView;
    @Bind(R.id.listViewUsers)
    ListView listViewUsers;
    @Bind(R.id.content)
    LinearLayout content;
    private UsersAdapter adapterUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        // reference views
        ButterKnife.bind(this);

        // setup common views
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup users list
        adapterUsers = new UsersAdapter(this);
        adapterUsers.setListener(this);
        listViewUsers.setAdapter(adapterUsers);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_search_users, menu);

        // setup the search view
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getString(R.string.search_by_username));
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (newText.length() >= 1)
                    searchUsers(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }


    /**
     * calls the api to search users with given substring
     * upadtes the list of users
     */
    private void searchUsers(String searchString)
    {
        UserApiController controller = new UserApiController(this);
        controller.searchusers(searchString, new SearchUserCallback()
        {
            @Override
            public void success(List<User> users)
            {
                // set new date to list
                adapterUsers.clear();
                adapterUsers.addAll(users);
            }

            @Override
            public void fail(String message)
            {
                // show error
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(User user, int position)
    {
        // go to the user profile activity
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(Constants.USER_ID, user.getUserId());
        startActivity(intent);
    }
}
