package com.camon.locationtracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.realm.Realm;

/**
 * Created by camon on 2015-10-07.
 */
public class LocationTrackerService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = LocationTrackerService.class.getName();
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Realm realm;
    private static final long UPDATE_INTERVAL = 1000 * 60 * 5; // 단위 : 밀리 세컨드 -> 분

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "LocationTracker starting", Toast.LENGTH_SHORT).show();
        initDB();
        initGoogleApiClient();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void initDB() {
        realm = Realm.getInstance(this);
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
        startLocationUpdates();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        // More about this in the 'Handle Connection Failures' section.
    }

    @Override
    public void onLocationChanged(Location location) {
        long time = location.getTime();
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        addLocation(time, lat, lng);
    }

    private void addLocation(long time, double lat, double lng) {
        Log.i(TAG, "time: " + time);
        Log.i(TAG, "lat: " + lat);
        Log.i(TAG, "lng: " + lng);
        realm.beginTransaction();
        MyLocation myLocation = realm.createObject(MyLocation.class);
        myLocation.setTime(time);
        myLocation.setLat(lat);
        myLocation.setLng(lng);
        realm.commitTransaction();
    }
}
