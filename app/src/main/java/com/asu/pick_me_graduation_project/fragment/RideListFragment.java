package com.asu.pick_me_graduation_project.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Ride;

import java.util.List;

/**
 */
public class RideListFragment extends Fragment
{
    /* UI */

    /* fields */
    private RidesAdapter adapter;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RideListFragment()
    {
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
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RidesAdapter(getContext(), getFragmentManager());
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * replaces the rides in the recycler view
     */
    public void setData(List<Ride> rides)
    {
        adapter.setData(rides);
    }


}
