package com.example.h8951.android_http_request_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eurybus on 11.11.2016.
 */

public class sqliteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME= "DionysAppStorage";

    private static final String TABLE_VENUES = "venues";

    //Venue table column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "description";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LATI = "latitude";
    private static final String KEY_LONGI = "longitude";

    public sqliteDatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VENUES_TABLE = "CREATE TABLE " + TABLE_VENUES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_LATI + " REAL," + KEY_LONGI + " REAL" + ")";
        db.execSQL(CREATE_VENUES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Let's drop old table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENUES);
        //And create it again
        onCreate(db);
    }

    //CRUD operations

    //Adding a new venue
    public void addVenue(Venue venue){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, venue.getName());
        values.put(KEY_DESC, venue.getDescription());
        values.put(KEY_ADDRESS, venue.getAddress());
        values.put(KEY_LATI, venue.getLatitude());
        values.put(KEY_LONGI,venue.getLongitude());

        //Inserting row
        db.insert(TABLE_VENUES, null, values);
        db.close();
    }

    public Venue getVenue(int id){
        Log.d("getVenue", "Getting venue "+id);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VENUES, new String[] {
                KEY_ID, KEY_NAME, KEY_DESC, KEY_ADDRESS, KEY_LATI, KEY_LONGI
        }, KEY_ID + "=?", new String[] {
                String.valueOf(id)}, null, null, null, null );

        if (cursor != null){
            cursor.moveToFirst();
        }

        Venue venue = new Venue(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3) ,Double.parseDouble(cursor.getString(4)),
                Double.parseDouble(cursor.getString(5)) );
        cursor.close();
        return venue;
    }

    public List<Venue> getAllVenues(){
        List<Venue> venueList = new ArrayList<Venue>();

        //Select all
        String selectQuery = "SELECT * FROM " + TABLE_VENUES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping thorugh all rows and adding to list

        if(cursor.moveToFirst()){
            do{
                Log.d("getAllVenues", "Adding venue to list");
                Venue venue = new Venue();
                venue.setId(Integer.parseInt(cursor.getString(0)));
                venue.setName(cursor.getString(1));
                venue.setDescription(cursor.getString(2));
                venue.setAddress(cursor.getString(3));
                venue.setLatitude(Double.parseDouble(cursor.getString(4)));
                venue.setLongitude(Double.parseDouble(cursor.getString(5)));
            }
            while (cursor.moveToNext());
            Log.d("getAllVenues", "Finished adding venues to list");
        }
        Log.d("getAllVenues", "Returning venuelist");
        cursor.close();
        return venueList;
    }

    public int getVenueCount(){
        String countQuery = "SELECT * FROM " + TABLE_VENUES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return  count;
    }

    public int updateVenue(Venue venue){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, venue.getName());
        values.put(KEY_DESC, venue.getDescription());
        values.put(KEY_ADDRESS, venue.getAddress());
        values.put(KEY_LATI, venue.getLatitude());
        values.put(KEY_LONGI,venue.getLongitude());

        //Update row
        return db.update(TABLE_VENUES, values, KEY_ID + "= ? ",
                new String[] {String.valueOf(venue.getId())});
    }

    public void deleteVenue(Venue venue){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VENUES, KEY_ID + "= ?",
                new String[] {String.valueOf(venue.getId())});

        db.close();
    }
}
