package com.asu.pick_me_graduation_project.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.adapter.JoinRideRequestAdapter;
import com.asu.pick_me_graduation_project.callback.GenericSuccessCallback;
import com.asu.pick_me_graduation_project.callback.GetRideJoinRequestsCallback;
import com.asu.pick_me_graduation_project.controller.AuthenticationAPIController;
import com.asu.pick_me_graduation_project.controller.RidesAPIController;
import com.asu.pick_me_graduation_project.model.JoinRideRequest;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class JoinRideRequestsFragment extends Fragment implements JoinRideRequestAdapter.Listener, SwipeRefreshLayout.OnRefreshListener
{
    /* UI */
    @Bind(R.id.content)
    View content;
    @Bind(R.id.recyclerViewJoinRideRequests)
    RecyclerView recyclerViewJoinRideRequests;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    /* fields */
    private JoinRideRequestAdapter adapter;
    private String rideId;
    private RidesAPIController controller;


    public JoinRideRequestsFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_ride_requests, container, false);
        ButterKnife.bind(this, view);

        // get id
        rideId = getArguments().getString(Constants.RIDE_ID);

        // setup fields
        controller = new RidesAPIController(getContext());

        // setup recycler view
        adapter = new JoinRideRequestAdapter(getContext(), getFragmentManager());
        adapter.setListener(this);
        recyclerViewJoinRideRequests.setAdapter(adapter);
        recyclerViewJoinRideRequests.setLayoutManager(new LinearLayoutManager(getContext()));

        // setup swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(this);

        // load data
        loadData();

        return view;
    }


    @Override
    public void onRefresh()
    {
        loadData();
    }

    /**
     * downloads the join requests
     */
    private void loadData()
    {
        Log.e("Game", "loading join ride requests");

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        controller.getRideJoinRequest(
                new AuthenticationAPIController(getContext()).getTokken()
                , rideId
                , new GetRideJoinRequestsCallback()
                {

                    @Override
                    public void success(List<JoinRideRequest> requests)
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
                        adapter.setData(requests);
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

    @Override
    public void respond(int position, final JoinRideRequest joinRideRequest, boolean accept)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "", getString(R.string.sending));

        controller.respondToJoinRideRequest(
                new AuthenticationAPIController(getContext()).getTokken()
                , rideId
                , joinRideRequest.getUser().getUserId()
                , accept
                , new GenericSuccessCallback()
                {

                    @Override
                    public void success()
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        adapter.remove(joinRideRequest.getUser().getUserId());
                    }

                    @Override
                    public void fail(String message)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}
