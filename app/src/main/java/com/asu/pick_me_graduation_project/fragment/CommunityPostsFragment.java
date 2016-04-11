package com.asu.pick_me_graduation_project.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.EditProfileFragment;
import com.asu.pick_me_graduation_project.adapter.CommunityPostsAdapter;
import com.asu.pick_me_graduation_project.callback.CreatePostCallback;
import com.asu.pick_me_graduation_project.callback.GetCommunityPostsCalback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.CommunityAPIController;
import com.asu.pick_me_graduation_project.model.CommunityPost;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommunityPostsFragment extends Fragment {

    /* fields */
    private String communityId;
    private CommunityAPIController controller;
    private CommunityPostsAdapter adapterPosts;

    /* views */
    @Bind(R.id.content)
    View content;
    @Bind(R.id.recyclerViewPosts)
    RecyclerView recyclerViewPosts;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    /* life cycle methods */

    public CommunityPostsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_community_posts, container, false);
        ButterKnife.bind(this, view);

        // setup recycler view
        adapterPosts = new CommunityPostsAdapter(getContext());
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

    /* listener */
    @OnClick(R.id.fab)
    void openCreatePost()
    {
        // open the create post dialog
        CreatePostFragment createPostFragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString(Constants.COMMUNITY_ID, communityId);
        createPostFragment.setArguments(args);
        createPostFragment.show(getFragmentManager(), getString(R.string.title_new_post));
    }

    /* methods */
    private void downloadPosts() {
        controller.getCommunityPosts(
                new AuthenticationAPIController(getContext()).getTokken()
                , communityId
                , new GetCommunityPostsCalback() {
                    @Override
                    public void success(List<CommunityPost> posts) {
                        swipeRefreshLayout.setRefreshing(false);
                        adapterPosts.setData(posts);
                    }

                    @Override
                    public void fail(String error) {
                        Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
