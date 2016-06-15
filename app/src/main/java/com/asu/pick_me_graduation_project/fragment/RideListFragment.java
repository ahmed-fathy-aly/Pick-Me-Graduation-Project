package com.asu.pick_me_graduation_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.activity.RideDetailsActivity;
import com.asu.pick_me_graduation_project.adapter.RidesAdapter;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.RideDetails;
import com.asu.pick_me_graduation_project.utils.Constants;

import java.util.List;

/**
 */
public class RideListFragment extends Fragment implements RidesAdapter.Listener
{;
    /* UI */

    /* fields */
    private RidesAdapter adapter;
    private RecyclerView recyclerView;
    List<Ride> data;
    private Listener listener;

    public RideListFragment()
    {
    }

    public RideListFragment(RideListFragment.Listener listener)
    {
        this.listener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_ride_list, container, false);

        // setup recycler view
        adapter = new RidesAdapter(getContext(), getFragmentManager());
        adapter.setListener(this);
        if (data != null)
            adapter.setData(data);
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * replaces the rides in the recycler view
     */
    public void setData(List<Ride> rides)
    {
        if (adapter!= null)
            adapter.setData(rides);
        else
            this.data = rides;
    }



    public void setCanRequestToJoin(String rideId, boolean can)
    {
        adapter.setCanRequestToJoin(rideId, can);
    }

    @Override
    public void onRequestToJoin(int position, Ride ride)
    {
        if (listener != null)
            listener.onRequestToJoin(position, ride);
    }

    @Override
    public void onRideClicked(int position, Ride ride)
    {
        Intent intent = new Intent(getContext(), RideDetailsActivity.class);
        intent.putExtra(Constants.RIDE_ID, ride.getId());
        startActivity(intent);
    }

    public interface Listener
    {
        public void onRequestToJoin(int position, Ride ride);
    }
}
