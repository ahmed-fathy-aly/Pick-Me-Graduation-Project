package com.asu.pick_me_graduation_project.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.UserProfileActivity;
import com.asu.pick_me_graduation_project.adapter.UsersAdapter;
import com.asu.pick_me_graduation_project.adapter.UsersAdminAdapter;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersAdminsListFragment extends Fragment implements UsersAdminAdapter.Listener
{

    /* fields */
    private UsersAdminAdapter adapterUsers;

    /* UI */
    @Bind(R.id.listViewUsers)
    ListView listViewUsers;
    @Bind(R.id.content)
    View content;
    private String communityId;

    public MembersAdminsListFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // infalte and reference
        View view = inflater.inflate(R.layout.fragment_members_list, container, false);
        ButterKnife.bind(this, view);

        // get arguments
        communityId = getArguments().getString(Constants.COMMUNITY_ID);
        boolean isAdmin = getArguments().getBoolean(Constants.IS_COMMUNITY_ADMIN);

        // setup users list
        adapterUsers = new UsersAdminAdapter(getContext(), isAdmin);
        adapterUsers.setListener(this);
        listViewUsers.setAdapter(adapterUsers);

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(User user, int position, View v)
    {
        // go to the user profile activity
        Intent intent = new Intent(getContext(), UserProfileActivity.class);
        intent.putExtra(Constants.USER_ID, user.getUserId());
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        android.support.v4.util.Pair.create(v, getString(R.string.transition_user_list_to_profile))
                );
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }

    @Override
    public void onMakeAdmin(final User user)
    {
        // make a request
        adapterUsers.setProgress(user.getUserId());
        CommunityAPIController controller = new CommunityAPIController(getContext());
        controller.makeUserAdmin(new AuthenticationAPIController(getContext()).getTokken()
                , communityId
                , user.getUserId()
                , new GenericSuccessCallback()
        {

            @Override
            public void success()
            {
                adapterUsers.setAdmin(user.getUserId());
            }

            @Override
            public void fail(String message)
            {
                Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * shows the users in the list
     * clears the previous users if existing
     */
    public void setMembers(List<User> users)
    {
        adapterUsers.clear();
        adapterUsers.addAll(users);
    }



}
