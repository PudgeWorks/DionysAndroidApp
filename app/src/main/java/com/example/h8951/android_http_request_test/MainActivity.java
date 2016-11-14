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
import com.google.android.gms.vision.text.Text;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.targetSdkVersion;

public class MainActivity extends Activity implements
    ConnectionCallbacks, OnConnectionFailedListener, LocationListener, JSONResponse {

    JSONObject jsonObject;
    private EditText urlText;
    private TextView textView;
    private TextView usersTextView;
    private EditText usersMultiline;
    String response;
    String urlVenues = "http://dionys-rest.azurewebsites.net/api/venues";
    String urlUsers = "http://dionys-rest.azurewebsites.net/api/users";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private TextView mockCoordinatesLat;
    private TextView mockCoordinatesLong;
    private TextView location;

    private final int REQUEST_LOCATION = 1;

    private SqliteDatabaseHandler db;

    Location currentLocation;
    Location mockLocation = new Location("MockLocation");
    //Test stuff
    Venue testVenue;
    List<User> localUsers = new ArrayList<>();
    List<Venue> localVenues = new ArrayList<>();

    AsyncFetchData test;
    AsyncFetchData getUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.myText);
        usersTextView = (TextView) findViewById(R.id.users);
        mockCoordinatesLat = (TextView) findViewById(R.id.mockLat);
        mockCoordinatesLong = (TextView) findViewById(R.id.mockLong);
        usersMultiline = (EditText) findViewById(R.id.usersMultiline);
        location = (TextView) findViewById(R.id.atVenue);

        //ensin tehdään ulommasta luokasta olio (HTTPRequest)
        //sitten sisemmästä luokasta olio
        // kutsumalla new ulomman olion alta.

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            textView.setText("Network connection found!");

            //DLWebPageTask.execute(stringUrl);
            test = new AsyncFetchData(this);
            test.execute(urlVenues,"venues");
            Log.d("STATUS:", "Out of async for venues.");

        } else {
            textView.setText("No network connection available.");
        }

        //Sqlite DB testing
        db = new SqliteDatabaseHandler(this);

        Log.d("Insert: ", "Inserting ..");
        //db.addVenue(new Venue("Eeppinen baari","Survontie 46","Aika jees paikka, mutta haisee koodarille", 64.132,25.51341));
        //db.addVenue(new Venue("Ylämummo","Survontie 32","Joku lätkäpaikka", 64.141,25.51332));

        //How many venues do we have
        Log.d("Number of venues", Integer.toString(db.getVenueCount()));
        //Read first venue
        //Venue venue = db.getVenue(1);
        //testVenue = venue;
        //Log.d("One venue", venue.getName() + "|" + venue.getDescription() );
        //Reading all venues
        //Log.d("Reading", "Reading all venues..");
        //localVenues = db.getAllVenues();

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
        mLongitudeText.setText("Longitude: " + location.getLongitude());
        currentLocation = location;
        //Testing distanceTo
        //amIthereYet();
    }

    private boolean amIthereYet(String name, Double latitude, Double longitude){
        GpsHelper gpsHelper = new GpsHelper();
        //Location remoteLocation = new Location(testVenue.getName());
        //remoteLocation.setLatitude(testVenue.getLatitude());
        //remoteLocation.setLongitude(testVenue.getLongitude());

        mockLocation.setLatitude(62.2439552);
        mockLocation.setLongitude(25.7482088);

        mockCoordinatesLat.setText("Mock longitude: " + Double.toString(mockLocation.getLatitude()));
        mockCoordinatesLong.setText("Mock latitude: " + Double.toString(mockLocation.getLongitude()));

        Location remoteLocation = new Location(name);
        remoteLocation.setLatitude(latitude);
        remoteLocation.setLongitude(longitude);

        /*currentLocation = new Location("CurrentLocation");
        currentLocation.setLatitude(64);
        currentLocation.setLongitude(25);*/
        //textView.setText(Double.toString(currentLocation.getLatitude()) +","+ Double.toString(currentLocation.getLongitude()));

//        Log.d("CurrentLocation",Double.toString(currentLocation.getLatitude()) +","+ Double.toString(currentLocation.getLongitude()));
  //      Log.d("RemoteLocation",Double.toString(remoteLocation.getLatitude()) +","+ Double.toString(remoteLocation.getLongitude()) );

        //float distanceToRemote = gpsHelper.calculateDistanceTo(currentLocation,remoteLocation);
        float distanceToRemote = gpsHelper.calculateDistanceTo(mockLocation,remoteLocation);
        Log.d("Dionys","Distance to " + name + " is " + distanceToRemote);

        if(distanceToRemote < 500f) {
            textView.setText("You've arrived to: " + name);
            location.setText("You are at: " + name);
            return true;
        } else {
            return false;
        }

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

        localVenues = venues;

        for(Venue venue : venues) {
            Log.d("Name for venue", venue.getName());
            //db.addVenue(venue);
        }
        compareCoordinates();
    }

    public void UsersResponse(List<User> users){

        int amountAtVenue = 0;

        for(User user : users) {
            Log.d("Name for user", user.getFname());
            amountAtVenue++;
            usersMultiline.append("\n" + user.getFname());
            //db.addUser(user);
            localUsers.add(user);
        }

        usersTextView.setText(usersTextView.getText() + Integer.toString(amountAtVenue));
    }

    public void compareCoordinates(){

        Log.d("STATUS: ", "in compareCoordinates");

        //localVenues = db.getAllVenues();
        boolean atLocation = false;
        int whichLocation = 0;

        Log.d("Size of localVenues", Integer.toString(localVenues.size()));
        for(Venue venue : localVenues){
            Log.d("STATUS: ", "checking venues...");
            Log.d("STATUS: ", "");
          atLocation = amIthereYet(venue.getName(), venue.getLatitude(), venue.getLongitude());
            if(atLocation == true ){
                whichLocation = venue.getId();
                break;
            }
        }
       getUsers = new AsyncFetchData(this);

        if(atLocation == true){
            urlUsers = urlUsers + "/" + whichLocation;
            Log.d("urlUsers: ", urlUsers);
            getUsers.execute(urlUsers, "users");
        }

    }

}
