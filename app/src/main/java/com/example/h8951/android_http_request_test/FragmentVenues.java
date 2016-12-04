package com.example.h8951.android_http_request_test;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by H8951 on 3.12.2016.
 */

public class FragmentVenues extends Fragment {

    ArrayList<Venue> venues = new ArrayList<Venue>();

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
        // fake data
        final Employees employees = new Employees();
        employees.initializeData();
        mEmployeeList = employees.getEmployees();

        // connect recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.employeeRecyclerView);
        // create layoutmanager
        mLayoutManager = new LinearLayoutManager(this);
        // set manager to recycler view
        mRecyclerView.setLayoutManager(mLayoutManager);
        // create adapter
        mAdapter = new EmployeesAdapter(mEmployeeList);
        // set adapter to recycler view
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
