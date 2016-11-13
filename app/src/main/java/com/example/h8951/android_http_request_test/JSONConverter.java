package com.example.h8951.android_http_request_test;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksi on 11.11.2016.
 */

public class JSONConverter {

    public List<Venue> venues = new ArrayList<>();

    protected List<Venue> convertJSONToVenue(String JSONData) throws JSONException
    {
        //JSONObject jsonObj = new JSONObject(JSONData);
        //JSONArray jsonArray = new JSONArray("Venue");
        //JSONArray jsonArray = jsonObj.getJSONArray("Venue");

        //List<Venue> venues = new ArrayList<>();

        /*for(int i = 0;i<jsonArray.length();i++)
        {
            int id = jsonArray.getJSONObject(i).getInt("Id");
            String name = jsonArray.getJSONObject(i).getString("name");
            String address = jsonArray.getJSONObject(i).getString("address");
            String desc = jsonArray.getJSONObject(i).getString("description");
            double lati = jsonArray.getJSONObject(i).getDouble("latitude");
            double longi = jsonArray.getJSONObject(i).getDouble("longitude");

            venues.add(new Venue(id, name, address, desc, lati, longi));
        }
        venues.add(new Venue(1, "asd", "asd", "ad",1,1));
       // return venues; */

        JSONArray jsonArray = new JSONArray(JSONData);

        for (int i = 0; i < jsonArray.length(); i++) {
            int id = jsonArray.getJSONObject(i).getInt("Id");
            String name = jsonArray.getJSONObject(i).getString("name");
            String address = jsonArray.getJSONObject(i).getString("address");
            String desc = jsonArray.getJSONObject(i).getString("desc");
            double lati = jsonArray.getJSONObject(i).getDouble("lati");
            double longi = jsonArray.getJSONObject(i).getDouble("longi");

            venues.add(new Venue(id, name, address, desc, lati, longi));
        }

        return venues;
    }
}
