package com.example.h8951.android_http_request_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eurybus on 11.11.2016.
 * Modified and made Parcelable by H8951 on 4.12.2016
 */

public class Venue implements Parcelable {

    //private variables
    private int _id;
    private String _name;
    private String _desc;
    private String _address;
    private int _imageId;
    private Double _lati;
    private Double _longi;

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
    //Overload
    public Venue(String name, String address,String desc, double lati, double longi){
        this._name = name;
        this._address = address;
        this._desc = desc;
        this._lati = lati;
        this._longi = longi;
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Venue createFromParcel(Parcel in) {
                    return new Venue(in);
                }

                public Venue[] newArray(int size) {
                    return new Venue[size];
                }
            };

    @Override
    public int  describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel dest, int i){
        dest.writeInt(_id);
        dest.writeString(_name);
        dest.writeString(_address);
        dest.writeString(_desc);
        dest.writeDouble(_lati);
        dest.writeDouble(_longi);
    }

    public Venue(Parcel in){

        this._id = in.readInt();
        this._name = in.readString();
        this._address = in.readString();
        this._desc = in.readString();
        this._lati = in.readDouble();
        this._longi = in.readDouble();

    }

    public int getImageId(){

        return _imageId;
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
