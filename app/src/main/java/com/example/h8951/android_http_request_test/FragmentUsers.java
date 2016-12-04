package com.example.h8951.android_http_request_test;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by H8951 on 3.12.2016.
 */

public class FragmentUsers extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_users, container, false);
    }
}
