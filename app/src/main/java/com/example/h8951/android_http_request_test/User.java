package com.example.h8951.android_http_request_test;

/**
 * Created by Aleksi on 14.11.2016.
 */

public class User {

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


    public User(){}

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
    }

   /* public Venue(String name, String address,String desc, double lati, double longi){
        this._name = name;
        this._address = address;
        this._desc = desc;
        this._lati = lati;
        this._longi = longi;
    }*/

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
