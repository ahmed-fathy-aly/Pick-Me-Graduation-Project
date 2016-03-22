package com.asu.pick_me_graduation_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.UsersAdapter;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
import com.asu.pick_me_graduation_project.controller.UserApiController;
import com.asu.pick_me_graduation_project.fragment.MembersListFragment;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchUsersActivity extends BaseActivity implements UsersAdapter.Listener
{

    /* UI */
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.editTextSearch)
    EditText editTextSearch;


    /* fields */
    private UserApiController controller;
    private MembersListFragment membersListFragment;

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
        editTextSearch.setVisibility(View.VISIBLE);
        editTextSearch.setHint(getString(R.string.search_by_username));

        // setup members  list fragment
        membersListFragment = new MembersListFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragmentMembersListContent, membersListFragment )
                .commit();

        // edit text listener
        editTextSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.length() >= 1)
                    searchUsers(s.toString());
            }
        });
        // setup contoller
        controller = new UserApiController(this);


    }




    /**
     * calls the api to search users with given substring
     * upadtes the list of users
     */
    private void searchUsers(String searchString)
    {
        progressBar.setVisibility(View.VISIBLE);
        controller.searchusers(searchString, new GetUsersCallback()
        {
            @Override
            public void success(List<User> users)
            {
                progressBar.setVisibility(View.INVISIBLE);

                // set new date to list
                membersListFragment.setMembers(users);
            }

            @Override
            public void fail(String message)
            {
                if (message==null)
                    return;
                progressBar.setVisibility(View.INVISIBLE);

                // show error
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(User user, int position, View v)
    {
        // go to the user profile activity
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(Constants.USER_ID, user.getUserId());
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        android.support.v4.util.Pair.create(v, getString(R.string.transition_user_list_to_profile))
                );
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
}
