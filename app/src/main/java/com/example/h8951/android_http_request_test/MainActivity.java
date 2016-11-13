package com.example.h8951.android_http_request_test;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import org.json.JSONObject;

import java.util.List;

import static android.R.attr.targetSdkVersion;

public class MainActivity extends Activity implements
    ConnectionCallbacks, OnConnectionFailedListener, LocationListener, VenuesResponse{

    JSONObject jsonObject;
    private EditText urlText;
    private TextView textView;
    String response;
    String stringUrl = "http://dionys-rest.azurewebsites.net/api/venues";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private TextView mLatitudeText;
    private TextView mLongitudeText;

    private final int REQUEST_LOCATION = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.myText);

        //ensin tehdään ulommasta luokasta olio (HTTPRequest)
        //sitten sisemmästä luokasta olio
        // kutsumalla new ulomman olion alta.

        AsyncFetchData test = new AsyncFetchData(this);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            textView.setText("Network connection found!");

            //DLWebPageTask.execute(stringUrl);

            test.execute(stringUrl);

        } else {
            textView.setText("No network connection available.");
        }

        //Sqlite DB testing
        SqliteDatabaseHandler db = new SqliteDatabaseHandler(this);

        Log.d("Insert: ", "Inserting ..");
        //db.addVenue(new Venue("Eeppinen baari","Survontie 46","Aika jees paikka, mutta haisee koodarille", 64.132,25.51341));
        //db.addVenue(new Venue("Ylämummo","Survontie 32","Joku lätkäpaikka", 64.141,25.51332));

        //How many venues do we have
        Log.d("Number of venues", Integer.toString(db.getVenueCount()));
        //Read first venue
        Venue venue = db.getVenue(1);
        Log.d("One venue", venue.getName() + "|" + venue.getDescription() );
        //Reading all venues
        Log.d("Reading", "Reading all venues..");
        List<Venue> venues = db.getAllVenues();

        /*for(Venue vn: venues){
            String log = "Id: " + vn.getId() + ", Name: " + vn.getName()
                    + ", Description: " + vn.getDescription() + ", Address: " + vn.getAddress()
                    + ", Latitude: " + vn.getLatitude() + ", Longitude: " + vn.getLongitude();
            Log.d("Venues: ",log);
        }*/

        //Getting debug textviews
        mLatitudeText = (TextView) findViewById(R.id.latitude);
        mLongitudeText = (TextView) findViewById(R.id.longitude);

        //Build google Play services clinet
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        if(mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("onConnected","Connection formed to GoogleClientApi");
        if(mGoogleApiClient.isConnected())
            checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", "connection to GoogleClientApi failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        // show location in TextViews
        mLatitudeText.setText("Latitude: " + location.getLatitude());
        mLongitudeText.setText("Latitude: " + location.getLongitude());
    }

    //Checking location permission
    public boolean selfPermissionGranted(String permission){
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = this.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(this, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }
    private void checkPermissions() {
        Log.d("checkPermissions","Checking permissions");
        // check permission
        int hasLocationPermission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        // permission is not granted yet
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // ask it -> a dialog will be opened
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Log.d("checkPermissions", "let's get location");
            // permission is already granted, start get location information
            startGettingLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // accepted, start getting location information
                    startGettingLocation();
                } else {
                    // denied
                    Toast.makeText(this, "Location access denied by the user!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startGettingLocation(){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000); //in miliseconds

        int hasLocationPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(hasLocationPermission == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
            );


    }

    public void VenuesResponse(List<Venue> venues){

        for(Venue venue : venues) {
            Log.d("Name for venue:", venue.getName());
        }
    }

}
