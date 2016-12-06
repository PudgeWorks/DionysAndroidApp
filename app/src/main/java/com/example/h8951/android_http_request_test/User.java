package com.example.h8951.android_http_request_test;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aleksi on 14.11.2016.
 */

public class User implements Parcelable {

    //private variables
    int _id;
    String _nick;
    String _fname;
    String _lname;
    int _age;
    boolean _sex;
    String _bio;
    String _avatar;
    String _url;
    String _password;
    String _salt;
    int _venue;

    boolean[] _sexArray = new boolean[1];

    public User(Parcel in){

        this._id = in.readInt();
        this._nick = in.readString();
        this._fname = in.readString();
        this._lname = in.readString();
        this._age = in.readInt();
        in.readBooleanArray(_sexArray);
        this._sex = _sexArray[0];
        this._bio = in.readString();
        this._avatar = in.readString();
        this._url = in.readString();
        this._password = in.readString();
        this._salt = in.readString();
        this._venue = in.readInt();
    }

    //Constructor
    public User(int id, String nick, String fname, String lname, int age, boolean sex, String bio, String avatar, String url, String password, String salt, int venue){
        this._id = id;
        this._nick = nick;
        this._fname = fname;
        this._lname = lname;
        this._age = age;
        this._sex = sex;
        this._bio = bio;
        this._avatar = avatar;
        this._url = url;
        this._password = password;
        this._salt = salt;
        this._venue = venue;
        this._sexArray[0] = _sex;
    }

    @Override
    public int  describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel dest, int i){
        dest.writeString(_nick);
        dest.writeInt(_age);
        dest.writeBooleanArray(_sexArray);
        dest.writeString(_bio);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public User createFromParcel(Parcel in) {
                    return new User(in);
                }

                public User[] newArray(int size) {
                    return new User[size];
                }
            };

    //public void setId(int id){ this._id = id;}
    public int getId(){return _id;}

    //public void setName(String name){this._name = name;}
    public String getNick(){return _nick;}

    //public void setAddress(String address){this._address = address;}
    public String getFname(){return _fname;}

    //public void setDescription(String description){this._desc = description;}
    public String getLname(){return  _lname;}

    //public void setLatitude(double latitude){this._lati = latitude;}
    public int getAge(){return _age;}

    //public void setLongitude(double longitude){this._longi = longitude;}
    public boolean getSex(){return _sex;}
    public String getBio(){return _bio;}
    public String getAvatar(){return _avatar;}
    public String getUrl(){return _url;}
    public String getPassword(){return _password;}
    public String getSalt(){return _salt;}
    public int getVenue(){return _venue;}
}
