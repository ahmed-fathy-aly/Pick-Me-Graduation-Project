package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.GenericMapsView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.internal.DebouncingOnClickListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 */
public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder>
{


    private List<Ride> data;
    FragmentManager fragmentManager;
    private Context context;
    Listener listener;
    private boolean requestToJoinVisibility;

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

    public void setRequestToJoinButtonShown(boolean shown)
    {
        this.requestToJoinVisibility = shown;
    }

    /**
     * registers to be invoked with callbacks
     */
    public void setListener(Listener listener)
    {
        this.listener = listener;
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

        // user data
        User rider = ride.getRider();
        holder.textViewUserName.setText(rider.getFirstName() + " " + rider.getLastName());
        if (ValidationUtils.notEmpty(rider.getProfilePictureUrl()))
            Picasso.with(context).
                    load(rider.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(holder.imageViewUserPP);

        // time and description
        holder.textViewDescription.setText(ride.getDescription());
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
            holder.textViewTime.setText(sdf.format(ride.getTime().getTimeInMillis()));
        } catch (Exception e)
        {
            Log.e("Game", "ride time exception " + e.getMessage());
        }

        // locations;
        holder.mapsView.reset();
        for (Location location : ride.getLocations())
        {
            holder.mapsView.addMarker(
                    location.getId()
                    , ""
                    , BitmapDescriptorFactory.HUE_ORANGE
                    , new LatLng(location.getLatitude(), location.getLongitude()));
        }
        holder.mapsView.fitMarkers();

        // route
        List<LatLng> latLngs = new ArrayList<>();
        for (Location location : ride.getLocations())
            latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        holder.mapsView.drawRoute(latLngs, 0xFF00BCD4);

        // show or hide request to join
        holder.requestToJoinFrame.setVisibility(ride.isCanRequestToJoin() ? View.VISIBLE : View.GONE);
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
        @Bind(R.id.genericMapsView)
        GenericMapsView mapsView;
        @Bind(R.id.textViewDescription)
        TextView textViewDescription;
        @Bind(R.id.textViewTime)
        TextView textViewTime;
        @Bind(R.id.buttonRequestToJoin)
        Button buttonRequestToJoin;
        @Bind(R.id.requestToJoinFrame)
        View requestToJoinFrame;
        @Bind(R.id.layoutCard)
        View layoutCard;
        @Bind(R.id.cardView)
        CardView cardView;

        public ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            mapsView.setCenterMarkerShown(false);


            buttonRequestToJoin.setOnClickListener(new DebouncingOnClickListener()
            {
                @Override
                public void doClick(View v)
                {
                    if (listener != null)
                        listener.onRequestToJoin(getAdapterPosition(), data.get(getAdapterPosition()));
                }
            });

            cardView.setOnClickListener(new DebouncingOnClickListener()
            {
                @Override
                public void doClick(View v)
                {
                    if (listener!=null)
                        listener.onRideClicked(getAdapterPosition(), data.get(getAdapterPosition()));

                }
            });
        }

    }


    /**
     * changes whethere the request to join button for a ride is visible or not
     */
    public void setCanRequestToJoin(String id, boolean can)
    {
        for (int i = 0; i < data.size(); i++)
            if (data.get(i).getId().equals(id))
            {
                data.get(i).setCanRequestToJoin(can);
                notifyItemChanged(i);
            }
    }

    public interface Listener
    {
        public void onRequestToJoin(int position, Ride ride);
        public void onRideClicked(int position, Ride ride);
    }
}
