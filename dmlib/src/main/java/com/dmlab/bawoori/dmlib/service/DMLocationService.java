package com.dmlab.bawoori.dmlib.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class DMLocationService extends Service {
    private static final long SCHEDULE_PERIOD = 10 * 1000;
    private static final long UPDATE_INTERVAL = 10 * 1000;
    private static final long FASTEST_INTERVAL = 2 * 1000;

    private static Location sLocation;
    private TimerTask mTask=null;
    private Timer mTimer=null;
    public LocationRequest mLocationRequest=null;

    public static double getLatitude() {
        return sLocation.getLatitude();
    }

    public static double getLongitude() {
        return sLocation.getLongitude();
    }

    public DMLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mTask == null) {
            //  mGeoFenceServiceHelper.init();
            mTask = new TimerTask() {
                @Override
                public void run() {
                    Log.d("bawoori", "running timer task........");
                    //  getLastLocation();
                }
            };

            mTimer = new Timer();
            mTimer.schedule(mTask, SCHEDULE_PERIOD, SCHEDULE_PERIOD);
        }

        if (mLocationRequest == null) {
            startLocationUpdates();
        }



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Trigger new location updates at interval
    private void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                null);
    }

    private void onLocationChanged(Location location) {
        // New location has now been determined
        if (location == null) {
            return;
        }
       /* String msg =
                "Latitude: " +
                Double.toString(location.getLatitude()) + "\n" +
                "Longitude: " +
                Double.toString(location.getLongitude()) + "\n" +
                "Accuracy: " +
                Double.toString(location.getAccuracy());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();*/

        // You can now create a LatLng Object for use with maps
        sLocation = location;
    }
}
