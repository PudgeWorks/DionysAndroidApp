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
    public List<User> users = new ArrayList<>();

    protected List<Venue> convertJSONToVenue(String JSONData) throws JSONException
    {

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

    protected List<User> convertJSONToUser(String JSONData) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(JSONData);

        for (int i = 0; i < jsonArray.length(); i++) {
            int id = jsonArray.getJSONObject(i).getInt("Id");
            String nick = jsonArray.getJSONObject(i).getString("nick");
            String fname = jsonArray.getJSONObject(i).getString("fname");
            String lname = jsonArray.getJSONObject(i).getString("lname");
            int age = jsonArray.getJSONObject(i).getInt("age");
            boolean sex = jsonArray.getJSONObject(i).getBoolean("sex");
            String bio = jsonArray.getJSONObject(i).getString("bio");
            String avatar = jsonArray.getJSONObject(i).getString("avatar");
            String url = jsonArray.getJSONObject(i).getString("url");
            String password = jsonArray.getJSONObject(i).getString("password");
            String salt = jsonArray.getJSONObject(i).getString("salt");
            int venue = jsonArray.getJSONObject(i).getInt("venue");

            users.add(new User(id, nick, fname, lname, age, sex, bio, avatar, url, password, salt, venue));
        }

        return users;
    }
}
