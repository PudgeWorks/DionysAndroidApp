package com.example.h8951.android_http_request_test;

/**
 * Created by Eurybus on 13.11.2016.
 */
import android.location.Location;


public class GpsHelper {

    private double _localLatitude;
    private double _localLongitude;
    private double _remoteLatitude;
    private double _remoteLongitude;
    public double MinDistanceBetween = 200; //In meters

    public GpsHelper(){}

    public float calculateDistanceTo(Location localLocation,Location remoteLocation){
        float result = localLocation.distanceTo(remoteLocation);
        return result;
    }
}
