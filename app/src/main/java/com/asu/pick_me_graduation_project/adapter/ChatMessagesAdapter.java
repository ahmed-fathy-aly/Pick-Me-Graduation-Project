package com.asu.pick_me_graduation_project.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asu.pick_me_graduation_project.R;
import com.asu.pick_me_graduation_project.model.ChatMessage;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;
import com.asu.pick_me_graduation_project.utils.ValidationUtils;
import com.asu.pick_me_graduation_project.view.GenericMapsView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 3/11/2016.
 */
public class ChatMessagesAdapter extends ArrayAdapter<ChatMessage>
{
    String CurrentUserId;

    public ChatMessagesAdapter(Context context,String currentUserId)
    {
        super(context, R.layout.sender_row);
        this.CurrentUserId = currentUserId;
    }


    /**
     * checks if there's a message with that id
     */
    public boolean contains(String chatMessageId)
    {
        for (int i = 0; i < getCount(); i++)
            if (getItem(i).getId().equals(chatMessageId))
                return  true;
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        final ChatMessage message = getItem(position);
        User user = message.getFrom();
        boolean isSender =user.getUserId().equals(CurrentUserId);

        if (message.getLatLngExtra() != null)
        {
            // choose which resource to inflate
            int resourceId = isSender ? R.layout.row_location_sender : R.layout.row_location_reciever;
            View locationView = LayoutInflater.from(getContext())
                    .inflate(resourceId, parent, false);

            // reference view
            GenericMapsView genericMapsView = (GenericMapsView) locationView.findViewById(R.id.genericMapsView);
            genericMapsView.setScrollable(false);

            // get the location from extras
            LatLng latLng = message.getLatLngExtra();
            genericMapsView.addMarker(
                    "locationSent"
                    ,user.getFirstName() + " " + user.getLastName()
                    , BitmapDescriptorFactory.HUE_CYAN
                    ,latLng);

            // move to that location
            genericMapsView.setCenterMarkerShown(false);
            genericMapsView.moveToLocation(latLng);

            return locationView;
        }
        else if (isSender) {

            // TODO - infalte
            view = LayoutInflater.from(getContext()).inflate(R.layout.sender_row, parent, false);
            // TODO - reference view
            // rference views in sender rpw
            TextView msgSender=(TextView)view.findViewById(R.id.MessageFromSender);
            TextView messageDate2=(TextView)view.findViewById(R.id.MsgDate2);
            // TODO - set data
            msgSender.setText(message.getContent());
            messageDate2.setText(DateUtils.getRelativeTimeSpanString(getContext(), message.getDate().getTimeInMillis()));


        }
        else {
            view = LayoutInflater.from(getContext()).inflate(R.layout.receiver_row, parent, false);
            // TODO - reference view
            // reference views in receiver row
            TextView msgReceiver=(TextView)view.findViewById(R.id.MessageFromReceiver);
            TextView messageDate=(TextView)view.findViewById(R.id.MessageDate);
            final ImageView receiverPP=(ImageView) view.findViewById(R.id.receiverPP);
            // TODO - set data
            msgReceiver.setText(message.getContent());
            messageDate.setText(DateUtils.getRelativeTimeSpanString(getContext(), message.getDate().getTimeInMillis()));
            if (ValidationUtils.notEmpty(user.getProfilePictureUrl()))
                Picasso.with(getContext()).
                        load(user.getProfilePictureUrl())
                        .placeholder(R.drawable.ic_user_small)
                        .into(receiverPP, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {
                            }

                            @Override
                            public void onError()
                            {

                            }
                        });
        }






        return view;
    }

}
