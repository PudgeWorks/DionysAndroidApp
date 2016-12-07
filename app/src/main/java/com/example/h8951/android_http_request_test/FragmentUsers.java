package com.example.h8951.android_http_request_test;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.example.h8951.android_http_request_test.R.id.usersRecyclerView;

/**
 * Created by H8951 on 3.12.2016.
 */

public class FragmentUsers extends Fragment {

    ArrayList<User> users = new ArrayList<User>();

    private RecyclerView usersRecyclerView;
    private RecyclerView.Adapter usersAdapter;
    private RecyclerView.LayoutManager usersLayoutManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        if(getArguments() != null){
            if(getArguments().getBoolean("asyncDone")) {

                users = getArguments().getParcelableArrayList("Escape Club");

                Log.d("users taulun koko: ", ""+users.size());

                if(users != null) {
                    for (User user : users) {
                        if(user != null){
                            Log.d("Venues for loopissa: ", user.getNick());
                        }
                    }
                }
            } else {
                Log.d("async: ", "Async taski ei ehtinyt suoriutua");
            }
        }

        usersRecyclerView = (RecyclerView) view.findViewById(R.id.usersRecyclerView);
        //connect recycler view
        usersRecyclerView = (RecyclerView) view.findViewById(R.id.usersRecyclerView);
        // create layoutmanager
        usersLayoutManager = new LinearLayoutManager(getActivity());
        // set manager to recycler view
        usersRecyclerView.setLayoutManager(usersLayoutManager);
        // create adapter
        usersAdapter = new UsersAdapter(users);
        // set adapter to recycler view
        usersRecyclerView.setAdapter(usersAdapter);

        return view;
    }
}
