package com.example.h8951.android_http_request_test;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by H8951 on 3.12.2016.
 */

public class FragmentVenues extends Fragment {

    ArrayList<Venue> venues = new ArrayList<Venue>();
    private RecyclerView venuesRecyclerView;
    private RecyclerView.Adapter venuesAdapter;
    private RecyclerView.LayoutManager venuesLayoutManager;
    private MainActivity _context;

    public FragmentVenues(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_venues, container, false);

        if(getArguments() != null){
            if(getArguments().getBoolean("asyncDone")) {

                venues = getArguments().getParcelableArrayList("venuesList");

                for(Venue venue : venues) {
                    Log.d("Venues for loopissa: ", venue.getName());
                }

            } else {
                Log.d("async: ", "Async taski ei ehtinyt suoriutua");
            }
        }

        venuesRecyclerView = (RecyclerView) view.findViewById(R.id.venuesRecyclerView);
         //connect recycler view
        venuesRecyclerView = (RecyclerView) view.findViewById(R.id.venuesRecyclerView);
        // create layoutmanager
        venuesLayoutManager = new LinearLayoutManager(getActivity());
        // set manager to recycler view
        venuesRecyclerView.setLayoutManager(venuesLayoutManager);
        // create adapter
        venuesAdapter = new VenuesAdapter(venues, ((MainActivity) getActivity()));
        // set adapter to recycler view
        venuesRecyclerView.setAdapter(venuesAdapter);

        return view;
    }
}
