package com.stechnologies.signaltracker;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by KH9216 on 3/17/2017.
 */

public class Signal {
    int signalStrength;
    double latitude;
    double longitude;
    String provider;

    public Signal(){

    }

    public Signal(int signalStrength, double latitude, double longitude,String provider){
        this.latitude = latitude;
        this.longitude = longitude;
        this.signalStrength = signalStrength;
        this.provider = provider;
    }
}
