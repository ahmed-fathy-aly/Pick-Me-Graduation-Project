package com.asu.pick_me_graduation_project.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.UserProfileActivity;
import com.asu.pick_me_graduation_project.adapter.UsersAdapter;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersListFragment extends Fragment implements UsersAdapter.Listener
{

    /* fields */
    private UsersAdapter adapterUsers;

    /* UI */
    @Bind(R.id.listViewUsers)
    ListView listViewUsers;

    public MembersListFragment()
    {
        setArguments(new Bundle());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // infalte and reference
        View view = inflater.inflate(R.layout.fragment_members_list, container, false);
        ButterKnife.bind(this, view);

        // setup users list
        adapterUsers = new UsersAdapter(getContext());
        adapterUsers.setListener(this);
        listViewUsers.setAdapter(adapterUsers);

        // add data
        List<User> users = getUsersFromArguments();
        if (users != null)
            adapterUsers.addAll(users);
        return view;
    }

    /**
     * @return null if no users in argument
     */
    private List<User> getUsersFromArguments()
    {
        if (getArguments() == null || !getArguments().containsKey(Constants.MEMBER_LIST))
            return null;
        return (List<User>) getArguments().getSerializable(Constants.MEMBER_LIST);
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
        startActivity(intent);
       // ActivityOptionsCompat options =
        //        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
        //                android.support.v4.util.Pair.create(v, getString(R.string.transition_user_list_to_profile))
        //        );
       // ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
    }

    /**
     * shows the users in the list
     * clears the previous users if existing
     */
    public void setMembers(List<User> users)
    {

        getArguments().putSerializable(Constants.MEMBER_LIST, (Serializable) users);
        if (adapterUsers != null)
        {
            adapterUsers.clear();
            adapterUsers.addAll(users);
        }
    }


}
