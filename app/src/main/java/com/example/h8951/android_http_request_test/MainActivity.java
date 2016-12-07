package com.example.h8951.android_http_request_test;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.handle;
import static android.R.attr.targetSdkVersion;

public class MainActivity extends Activity implements
    ConnectionCallbacks, OnConnectionFailedListener, LocationListener, JSONResponse, UsersInterface {

    JSONObject jsonObject;
    private EditText urlText;

    String response;
    String debugToBundle;
    String urlVenues = "http://dionys-rest.azurewebsites.net/api/venues";
    String urlUsers = "http://dionys-rest.azurewebsites.net/api/users";
    String urlInput ="http://dionys-rest.azurewebsites.net/api/input";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final int REQUEST_LOCATION = 1;

    private SqliteDatabaseHandler db;

    Location currentLocation;
    Location mockLocation = new Location("MockLocation");

    //Test stuff
    Venue testVenue;

    List<User> localUsers = new ArrayList<>();
    List<Venue> localVenues = new ArrayList<>();

    ArrayList<User> allUsers = new ArrayList<>();

    AsyncFetchData getVenues;
    AsyncFetchData getUsers;
    AsyncFetchData getAllUsers;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    android.app.FragmentTransaction FragTrans;
    FragmentManager FragMan;

    FragmentVenues FragVenues = new FragmentVenues();
    FragmentUsers FragUsers = new FragmentUsers();
    FragmentDebug FragDebug = new FragmentDebug();
    FragmentLogin FragLogin = new FragmentLogin();

    Bundle bundleDebug = new Bundle();
    Bundle bundleVenues = new Bundle();
    Bundle bundleUsers = new Bundle();

    boolean bundleSetArgumentsDoOnceDebug = false;
    boolean bundleSetArgumentsDoOnceVenues = false;
    boolean bundleSetArgumentsDoOnceUsers = false;

    boolean isDown = false;
    boolean isUp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundleDebug.putBoolean("asyncDone", false);

        FragMan = getFragmentManager();
        FragTrans = FragMan.beginTransaction();

        FragTrans.replace(R.id.visibleFragment, FragLogin );
        FragTrans.commit();

       /* final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        View mainView = viewGroup.getChildAt(0);

        mainView.setOnTouchListener(new OnSwipeTouchListener(this){

            @Override
            public void onSwipeLeft() {
                Log.d("Swipe detection:", "HOLY FUCKER YOU SWIPED LEFT");
            }
            public void onSwipeRight() {
                Log.d("Swipe detection:", "HOLY SHITFACE YOU SWIPED RIGHT");
            }
        });
        */

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //textView.setText("Network connection found!");

            getVenues = new AsyncFetchData(this);
            getVenues.execute(urlVenues,"venues");

            getAllUsers = new AsyncFetchData(this);
            getAllUsers.execute(urlUsers, "allUsers");

            Log.d("STATUS:", "Out of async for venues.");

        } else {
            //textView.setText("No network connection available.");
        }

        db = new SqliteDatabaseHandler(this);

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

        //Build google Play services client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                isDown = true;
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                isUp = true;
                break;
        }
        if(isDown && isUp){
            handleMotion();
            isUp = false;
            isDown = false;
        }
        return super.onTouchEvent(event);
    }

    public void handleMotion(){
        float deltaX = x2 - x1;
        float distanceLeft = 0;

        Log.d("x1: ", ""+ x1);
        Log.d("x2: ", ""+ x2);
        Log.d("deltaX: ", ""+ deltaX);

        if(Math.abs(deltaX) > MIN_DISTANCE)
            Log.d("deltaX", " yli min_distancen");

        //liikkunut tarpeeksi oikealle, tai liikkunut tarpeeksi vasemmalle ollakseen swipe
        if (Math.abs(deltaX) > MIN_DISTANCE){
            if (deltaX > 0){
                Toast.makeText(this, "left to right swipe", Toast.LENGTH_SHORT).show ();
                selectFragment(true);
            } else if (deltaX < 0){
                Toast.makeText(this, "right to left swipe", Toast.LENGTH_SHORT).show ();
                selectFragment(false);
            }
            else {
                Toast.makeText(this, "not a swipe", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else
        {
            // jotain muuta kuin swipe
        }
    }

    public void selectFragment(boolean right){          //right = oikea swipe, else = vasen swipe
                                                        //oikea swipe = debug fragment, vasen swipe = venues fragment
        if (right){
                                                        //set argumentsia ei voi käyttää useammin kuin kerran yhteen fragmenttiin.
            if(!bundleSetArgumentsDoOnceDebug){              //Tarkastetaan onko sitä käytetty kerran ja muutetaan kerran asetettuja arvoja
                FragDebug.setArguments(bundleDebug);         //getArgumentsin kautta jos on.
                bundleSetArgumentsDoOnceDebug = true;
            } else {
                FragDebug.getArguments().putAll(bundleDebug);
            }
            FragTrans = FragMan.beginTransaction();
            FragTrans.replace(R.id.visibleFragment, FragDebug );
            FragTrans.commit();
        } else {
            if(!bundleSetArgumentsDoOnceVenues){
                bundleSetArgumentsDoOnceVenues = true;          //Tarkastetaan onko sitä käytetty kerran ja muutetaan kerran asetettuja arvoja
                FragVenues.setArguments(bundleVenues);         //getArgumentsin kautta jos on.

            } else {
                FragVenues.getArguments().putAll(bundleVenues);
            }

            FragTrans = FragMan.beginTransaction();
            FragTrans.replace(R.id.visibleFragment, FragVenues );
            FragTrans.commit();
        }
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
        bundleDebug.putString("realLat", "Latitude: " + location.getLatitude());
        bundleDebug.putString("realLong", "Longitude: " + location.getLongitude());
        //mLatitudeText.setText("Latitude: " + location.getLatitude());
        //mLongitudeText.setText("Longitude: " + location.getLongitude());
        currentLocation = location;

        //amIthereYet();    //tarvitsee ajastimen sijainnin tarkistusten välillä. Ehkä 10 min?
    }

    private boolean amIthereYet(String name, Double latitude, Double longitude){

        GpsHelper gpsHelper = new GpsHelper();

        mockLocation.setLatitude(62.2439552);
        mockLocation.setLongitude(25.7482088);

        bundleDebug.putString("mockLat", "Mock latitude: " + Double.toString(mockLocation.getLatitude()));
        bundleDebug.putString("mockLong", "Mock longitude: " + Double.toString(mockLocation.getLongitude()));

        //mockCoordinatesLat.setText("Mock longitude: " + Double.toString(mockLocation.getLatitude()));
        //mockCoordinatesLong.setText("Mock latitude: " + Double.toString(mockLocation.getLongitude()));

        Location remoteLocation = new Location(name);
        remoteLocation.setLatitude(latitude);
        remoteLocation.setLongitude(longitude);

        float distanceToRemote = gpsHelper.calculateDistanceTo(mockLocation,remoteLocation);
        Log.d("Dionys","Distance to " + name + " is " + distanceToRemote);

        if(distanceToRemote < 500f) {
            bundleDebug.putString("venue", "You are at: " + name);
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
            // ask permission, a dialog will be opened
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
        mLocationRequest.setInterval(5000); //in milliseconds

        int hasLocationPermission = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(hasLocationPermission == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
            );


    }

    public void VenuesResponse(List<Venue> venues){

        localVenues = venues;
        ArrayList<Venue> localVenuesClone = new ArrayList<Venue>(localVenues);      //localVenues on list tyyppiä, jota ei voida muuntaa ParcelableArrayListiksi, joten tehdään konversio ja siirretään collection localVenuesista.
        bundleVenues.putParcelableArrayList("venuesList", localVenuesClone);        //<-- muutettiin Venues luokka implementoimaan Parcelable,
                                                                                    // jolloin bundleen voidaan pistää taulukko Venue olioita Parcelable muodossa,
                                                                                    // tällöin bundlella voidaan toimittaa Venue olioita mainactivitystä venues fragmenttiin.
        for(Venue venue : venues) {
            Log.d("Name for venue", venue.getName());
            //db.addVenue(venue);       //sqlite lukemisessa vielä jotain häikkää. Lukeminen tehdään tällä hetkellä suoraan ajon aikaisesta oliokokoelmasta.
        }
        compareCoordinates();
    }

    public void UsersResponse(List<User> users){

        int amountAtVenue = 0;
        ArrayList<String> usersAtVenue = new ArrayList<String>();

        for(User user : users) {
            Log.d("Name for user", user.getFname());
            amountAtVenue++;
            usersAtVenue.add(user.getFname());

            //usersMultiline.append("\n" + user.getFname());
            //db.addUser(user);         //sqlite lukemisessa vielä jotain häikkää. Lukeminen tehdään tällä hetkellä suoraan ajon aikaisesta oliokokoelmasta.
            localUsers.add(user);
        }

        bundleDebug.putStringArrayList("usersAtLocation", usersAtVenue);

        bundleDebug.putString("pplAmount", Integer.toString(amountAtVenue));
        //usersTextView.setText(usersTextView.getText() + Integer.toString(amountAtVenue));
        bundleDebug.putBoolean("asyncDone", true);
        bundleVenues.putBoolean("asyncDone", true);
        bundleUsers.putBoolean("asyncDone", true);
        Log.d("UsersResponse: ", "Bundle putit tehty");

    }

    public void AllUsersResponse(List<User>users){                                                  //vastaanottaa AsyncFetchDatasta kaikki käyttäjät ja tuo ne main threadin saataville
        ArrayList<User> allUsersClone = new ArrayList<User>(users);                                 //tyyppimuunnos listasta arraylistaksi
        allUsers = allUsersClone;
        bundleUsers.putBoolean("asyncDone", true);
        sortUsersToVenues();
    }

    public void compareCoordinates(){

        Log.d("STATUS: ", "in compareCoordinates");

        //localVenues = db.getAllVenues();   //sqlite lukemisessa vielä jotain häikkää. Lukeminen tehdään tällä hetkellä suoraan ajon aikaisesta oliokokoelmasta.
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

    public void sortUsersToVenues(){

        User[][] usersInVenues = new User[localVenues.size()][100];
        List<List<User>> usersInVenues2 = new ArrayList<List<User>>();
        List<User>[] arrayOfLists = new List[localVenues.size()];

        int i = 0;
        int j = 0;

        ArrayList<Venue> venuesWithUsersIn = new ArrayList<Venue>();

        for(Venue venue : localVenues){
            //usersInVenues2.add(new ArrayList<User>());
            //arrayOfLists[i] = new ArrayList<User>();
            for(User user : allUsers){
                if(user.getVenue() == venue.getId()) {
                    Log.d("Venuessa " + venue.getName(), " on henkilö: " + user.getNick());
                    venuesWithUsersIn.add(venue);
                    usersInVenues[i][j] = user;
                    //usersInVenues2.iterator().next().add
                    //arrayOfLists[i].add(user);
                }
                j++;
            }
            i++;
        }

        ArrayList<User> temp;

        for(User[] userArray : usersInVenues){
        //for(List<User> userArray : arrayOfLists){
            temp  = new ArrayList<User>(Arrays.asList(userArray));
            //temp = new ArrayList<User>(userArray);
            try{
                bundleUsers.putParcelableArrayList(venuesWithUsersIn.iterator().next().getName(), temp);
            } catch(Exception ex){
                Log.d("sortUsersToVenues: ", "No more venues with users. Array out of bounds.");
            }
        }
    }

    public void activateAndPopulateUsersFragment(){

        if(!bundleSetArgumentsDoOnceUsers){
            bundleSetArgumentsDoOnceUsers = true;          //Tarkastetaan onko sitä käytetty kerran ja muutetaan kerran asetettuja arvoja
            FragUsers.setArguments(bundleUsers);         //getArgumentsin kautta jos on.

        } else {
            FragUsers.getArguments().putAll(bundleUsers);
        }

        FragTrans = FragMan.beginTransaction();
        FragTrans.replace(R.id.visibleFragment, FragUsers );
        FragTrans.commit();

    }

    public void demoButtonClicked(View view){
        User user = localUsers.get(1);
        String currentVenueId = Integer.toString(user.getVenue());
        String venueId = "11";
        String url = urlInput;
        String user_nick = user.getNick();
        AsyncPutData putData = new AsyncPutData(this);
        putData.execute(url,"nick",user_nick,"venue_key",venueId);

    }
}
