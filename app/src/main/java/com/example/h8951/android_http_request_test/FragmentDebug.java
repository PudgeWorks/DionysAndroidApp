package com.example.h8951.android_http_request_test;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by H8951 on 3.12.2016.
 */

public class FragmentDebug extends Fragment {

    private TextView textView;
    private TextView usersTextView;
    private EditText usersMultiline;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private TextView mockCoordinatesLat;
    private TextView mockCoordinatesLong;
    private TextView location;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_debug, container, false);

        textView = (TextView) view.findViewById(R.id.myText);
        usersTextView = (TextView) view.findViewById(R.id.users);
        mockCoordinatesLat = (TextView) view.findViewById(R.id.mockLat);
        mockCoordinatesLong = (TextView) view.findViewById(R.id.mockLong);
        mLatitudeText = (TextView) view.findViewById(R.id.latitude);
        mLongitudeText = (TextView) view.findViewById(R.id.longitude);
        usersMultiline = (EditText) view.findViewById(R.id.usersMultiline);
        location = (TextView) view.findViewById(R.id.atVenue);

        if(getArguments() != null){
            if(getArguments().getBoolean("asyncDone")) {

                //usersTextView.setText();
                usersTextView.setText(getArguments().getString("pplAmount"));
                mockCoordinatesLat.setText(getArguments().getString("mockLat"));
                mockCoordinatesLong.setText(getArguments().getString("mockLong"));
                mLatitudeText.setText(getArguments().getString("realLat"));
                mLongitudeText.setText(getArguments().getString("realLong"));
                location.setText(getArguments().getString("venue"));

                for(String user : getArguments().getStringArrayList("usersAtLocation")) {
                    Log.d("Henkilöt for loopissa: ", user);
                    usersMultiline.append(user);
                    usersMultiline.setText("Testing Testing...");
                }

            } else {
                textView.setText("Async has not completed in time.");
            }
        }

        return view;
    }


}
