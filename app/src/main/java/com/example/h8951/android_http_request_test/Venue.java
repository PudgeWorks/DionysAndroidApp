package com.example.h8951.android_http_request_test;

/**
 * Created by Eurybus on 11.11.2016.
 */

public class Venue {

    //private variables
    int _id;
    String _name;
    String _desc;
    String _address;
    Double _lati;
    Double _longi;

    public Venue(){}

    //Constructor
    public Venue(int id, String name, String address, String desc, double lati, double longi){
        this._id = id;
        this._name = name;
        this._address = address;
        this._desc = desc;
        this._lati = lati;
        this._longi = longi;
    }
    public void setId(int id){ this._id = id;}
    public int getId(){return _id;}

    public void setName(String name){this._name = name;}
    public String getName(){return _name;}

    public void setAddress(String address){this._address = address;}
    public String getAddress(){return _address;}

    public void setDescription(String description){this._desc = description;}
    public String getDescription(){return  _desc;}

    public void setLatitude(double latitude){this._lati = latitude;}
    public double getLatitude(){return _lati;}

    public void setLongitude(double longitude){this._longi = longitude;}
    public double getLongitude(){return _longi;}
}
