package com.asu.pick_me_graduation_project.fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Ride;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 */
public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder>
{


    private List<Ride> data;
    FragmentManager fragmentManager;
    private Context context;

    public RidesAdapter(Context context, FragmentManager fragmentManager)
    {
        this.context = context;
        this.data = new ArrayList<>();
        this.fragmentManager = fragmentManager;
    }

    /**
     * replaces the data and updates UI
     *
     * @param newData
     */
    public void setData(List<Ride> newData)
    {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_ride, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        Ride ride = data.get(position);

        /*
        // set maps fragment
        MapsFragment mapsFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putBoolean(MapsFragment.ARG_SHOW_CENTER_LOCATION, false);
        args.putBoolean(MapsFragment.ARG_START_WITH_MY_LOCATION, false);
        mapsFragment.setArguments(args);
        holder.mapContainers.setId(position*132 + 5);
        fragmentManager
                .beginTransaction()
                .replace(position*132 + 5, mapsFragment)
                .commit();

        */
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @Bind(R.id.imageViewUserPP)
        CircleImageView imageViewUserPP;
        @Bind(R.id.textViewUserName)
        TextView textViewUserName;
        @Bind(R.id.mapContainers)
        FrameLayout mapContainers;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.textViewColor)
        TextView textViewColor;

        public ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);


        }

    }
}
