package com.asu.pick_me_graduation_project.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
public class CommunityPostsFragment extends Fragment
{

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

    public CommunityPostsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.communityId = getArguments().getString(Constants.COMMUNITY_ID);
        this.controller = new CommunityAPIController(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout and reference views
        View view = inflater.inflate(R.layout.fragment_community_posts, container, false);
        ButterKnife.bind(this, view);

        // setup recycler view
        adapterPosts = new CommunityPostsAdapter(getContext());
        recyclerViewPosts.setAdapter(adapterPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        // only enable swipe refresh when the first item is at the top
        recyclerViewPosts.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                downloadPosts();
            }
        });

        // download data
        downloadPosts();
        return view;
    }

    /* listener */
    @OnClick(R.id.fab)
    void openCreatePost()
    {
        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.title_new_post));

        // Set up the input
        final EditText editTextMessage = new EditText(getContext());
        editTextMessage.setInputType(InputType.TYPE_CLASS_TEXT);
        editTextMessage.setHint(getString(R.string.write_a_post));
        editTextMessage.setGravity(Gravity.CENTER_VERTICAL);
        builder.setView(editTextMessage);

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.send), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String content = editTextMessage.getText().toString();
                createPost(content);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }


    /* methods */
    private void downloadPosts()
    {
        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        controller.getCommunityPosts(
                new AuthenticationAPIController(getContext()).getTokken()
                , communityId
                , new GetCommunityPostsCalback()
                {
                    @Override
                    public void success(List<CommunityPost> posts)
                    {
                        if (!isAdded())
                            return;
                        swipeRefreshLayout.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        adapterPosts.setData(posts);
                    }

                    @Override
                    public void fail(String error)
                    {
                        if (!isAdded())
                            return;
                        swipeRefreshLayout.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                        Snackbar.make(content, error, Snackbar.LENGTH_SHORT).show();
                    }
                });

    }


    /**
     * creates a new post
     */
    private void createPost(String postConent)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.posting));
        controller.createPost(
                new AuthenticationAPIController(getContext()).getTokken()
                , communityId
                , postConent
                , new CreatePostCallback()
                {
                    @Override
                    public void success(CommunityPost post)
                    {
                        // show success
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();

                        // pass the new post
                        adapterPosts.addFirst(post);
                    }

                    @Override
                    public void fail(String message)
                    {
                        // show error
                        progressDialog.dismiss();
                        Snackbar.make(content, message, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
