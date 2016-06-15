package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.JoinRideRequest;
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
public class JoinRideRequestAdapter extends RecyclerView.Adapter<JoinRideRequestAdapter.ViewHolder>
{


    private List<JoinRideRequest> data;
    FragmentManager fragmentManager;
    private Context context;
    Listener listener;
    private boolean requestToJoinVisibility;

    public JoinRideRequestAdapter(Context context, FragmentManager fragmentManager)
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
    public void setData(List<JoinRideRequest> newData)
    {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
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
                .inflate(R.layout.row_join_ride_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        JoinRideRequest joinRideRequest = data.get(position);

        // user data
        User user = joinRideRequest.getUser();
        holder.textViewUserName.setText(user.getFirstName() + " " + user.getLastName());
        if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
            Picasso.with(context).
                    load(user.getProfilePictureUrl())
                    .placeholder(R.drawable.ic_user_small)
                    .into(holder.imageViewUserPP);

        // message
        holder.textViewMessage.setText(joinRideRequest.getMessage());

        // locations;
        holder.mapsView.reset();
        for (Location location : joinRideRequest.getLocationList())
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
        for (Location location : joinRideRequest.getLocationList())
            latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        holder.mapsView.drawRoute(latLngs, 0xFF00BCD4);

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
        @Bind(R.id.textViewMessage)
        TextView textViewMessage;
        @Bind(R.id.buttonAccept)
        Button buttonAccept;
        @Bind(R.id.buttonReject)
        Button buttonReject;

        public ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            mapsView.setCenterMarkerShown(false);


            buttonAccept.setOnClickListener(new DebouncingOnClickListener()
            {
                @Override
                public void doClick(View v)
                {
                    if (listener != null)
                        listener.respond(getAdapterPosition(), data.get(getAdapterPosition()), true);
                }
            });
            buttonReject.setOnClickListener(new DebouncingOnClickListener()
            {
                @Override
                public void doClick(View v)
                {
                    if (listener != null)
                        listener.respond(getAdapterPosition(), data.get(getAdapterPosition()), false);
                }
            });

        }

    }



    public interface Listener
    {
        public void respond(int position, JoinRideRequest joinRideRequest, boolean accept);

    }
}
