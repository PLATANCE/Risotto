package com.plating.helperAPI;

import android.content.Context;
import android.location.LocationManager;

import com.plating.application.MyApplication;

/**
 * Created by cheehoonha on 10/2/15.
 */
public class GPS {
    public static boolean isGpsTurnedOn() {
        final LocationManager locationManager = (LocationManager) MyApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);

        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            return false;
        }

        return true;
    }
}
