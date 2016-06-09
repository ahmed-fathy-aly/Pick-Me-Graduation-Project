package com.asu.pick_me_graduation_project.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.UserProfileActivity;
import com.asu.pick_me_graduation_project.adapter.CommunityRequestsAdapter;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetUsersCallback;
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
public class CommunityJoinRequestsFragment extends Fragment implements CommunityRequestsAdapter.Listener {


    /* fields */
    private String communityId;
    private CommunityAPIController controller;
    private CommunityRequestsAdapter adapterPosts;

    /* views */
    @Bind(R.id.content)
    View content;
    @Bind(R.id.recyclerViewPosts)
    RecyclerView recyclerViewPosts;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    /* life cycle methods */

    public CommunityJoinRequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.communityId = getArguments().getString(Constants.COMMUNITY_ID);
        this.controller = new CommunityAPIController(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout and reference views
        View view = inflater.inflate(R.layout.fragment_community_join_requests, container, false);
        ButterKnife.bind(this, view);

        // setup recycler view
        adapterPosts = new CommunityRequestsAdapter(getContext());
        adapterPosts.setListener(this);
        recyclerViewPosts.setAdapter(adapterPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // only enable swipe refresh when the first item is at the top
        recyclerViewPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean enableSwipeToRefresh =
                        layoutManager.getChildCount() == 0
                                || layoutManager.getChildAt(0).getTop() >= 0;
                swipeRefreshLayout.setEnabled(enableSwipeToRefresh);
            }
        });

        // setup swipe refresh
        swipeRefreshLayout.setColorSchemeResources(R.color.accent);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPosts();
            }
        });

        // download data
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        downloadPosts();
        return view;
    }


    /* methods */
    private void downloadPosts() {

        controller.getCommunityRequests(
                new AuthenticationAPIController(getContext()).getTokken()
                , new AuthenticationAPIController(getContext()).getCurrentUser().getUserId()
                , communityId, new GetUsersCallback() {

                    @Override
                    public void success(List<User> users) {
                        adapterPosts.setData(users);
                    }

                    @Override
                    public void fail(String message) {
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(User user, int position, View v) {
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
    public void onAccept(User user, int adapterPosition) {

        controller.answerCommunityRequests(
                new AuthenticationAPIController(getContext()).getTokken()
                , user.getUserId()
                , communityId, new GenericSuccessCallback() {

                    @Override
                    public void success() {

                    }

                    @Override
                    public void fail(String message) {
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}
