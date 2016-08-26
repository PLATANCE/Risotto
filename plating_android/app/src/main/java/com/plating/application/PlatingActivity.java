package com.plating.application;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.plating.R;
import com.plating.dialog.MakePhoneCallDialog;
import com.plating.helperAPI.GPS;
import com.plating.network.i_set_location.SetUserGPSLocation;
import com.plating.object.AnalyticsEvent;
import com.plating.object.MixPanelEvent;
import com.plating.object.MixPanelProperty;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.network.VolleySingleton;
import com.plating.object_singleton.Cart;
import com.plating.pages.h_cart.CartActivity;
import com.plating.util.SVUtil;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/18/15.
 */
public class PlatingActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public FirebaseAnalytics mFirebaseAnalytics;
    public AppEventsLogger mAppEventsLogger;
    public static String LOG_TAG_PREFIX = "PlatingActivity.";
    public String LOG_TAG;

    // Context and Activity
    public Context mContext;
    public Activity mActivity;

    // Volley Network queue
    public VolleySingleton mVolleySingleton;
    public RequestQueue mRequestQueue;
    public ImageLoader mImageLoader;

    // GPS related variables
    // Google client to interact with Google API
    public GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;

    private MixpanelAPI mMixpanelAPI;

    // GPS location updates intervals in sec
    public static int UPDATE_INTERVAL = 2000; // 2 sec
    public static int FATEST_INTERVAL = 1000; // 1 sec
    public static int DISPLACEMENT = 5; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifeCycle", "Plating Activity onCreate");
        LOG_TAG = LOG_TAG_PREFIX + this.getClass().getSimpleName();
        Log.d(LOG_TAG, "onCreate: Starts");

        // Context and Activity
        mContext = this;
        mActivity = this;

        // Set up volley network connection queue

        mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();
        mImageLoader = mVolleySingleton.getmImageLoader();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mMixpanelAPI = MixpanelAPI.getInstance(this, this.getString(R.string.mixpanel_token_release_version));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart: Starts");

        // Context and Activity
        mContext = this;
        mActivity = this;

        // Set up volley network connection queue
        mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();
        mImageLoader = mVolleySingleton.getmImageLoader();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop: Stop");

        // Context and Activity
        mContext = null;
        mActivity = null;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume: Starts");

        AppEventsLogger.activateApp(this, this.getString(R.string.facebook_app_id));

        // Everytime a user enters a new activity, send the name of activity to mixpanel
        String className = this.getClass().getSimpleName().replace("Activity", "");
        String mixPanel_screenTrack = "(SCREEN) ";
        char[] arr = className.toCharArray();
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] >= 'A' && arr[i] <= 'Z') {
                mixPanel_screenTrack += " ";
            }
            mixPanel_screenTrack += arr[i];
        }
        // String mixPanel_screenTrack = "(SCREEN) " + this.getClass().getSimpleName().replace("Activity", "");
        MixPanel.mixPanel_trackWithOutProperties(mixPanel_screenTrack);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this, this.getString(R.string.facebook_app_id));
    }

    // update latitude and longitude of user_table
    private void setUserLocation(final int user_idx, final double lat, final double lon) {
        SetUserGPSLocation.sendDataToServer(this, mRequestQueue, user_idx, lat, lon);
    }

    public void setUserLocation_callBack() {
        Log.d(LOG_TAG, "SubmitReview_callback");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy: Starts");

        // Mix panel sends data every 60 seconds.
        // So if the user exits app, send the existing data.
        mMixpanelAPI.flush();
    }

    public void sendLogEventToFirebase(AnalyticsEvent event) {
        mFirebaseAnalytics.logEvent(event.getEventName(), event.getEventArguments());
    }

    public void sendLogEventToFacebookAnalysis(String event) {
        mAppEventsLogger.logEvent(event);
    }

    public void mixPanelTrackingWithOutProperties(@NonNull String eventName) {
        mMixpanelAPI.track(eventName);
    }

    public void mixPanelTrackingWithProperties(@NonNull String eventName,
                                               @NonNull ArrayList<MixPanelProperty> properties) {
        MixPanelEvent event = new MixPanelEvent(properties);
        mMixpanelAPI.track(eventName, event.getProperties());
    }

    public void setMixPanelUserIdentity(MixPanelProperty property) {
        mMixpanelAPI.getPeople().identify(property.getPropertyName());
        mMixpanelAPI.getPeople().set(property.getPropertyName(), property.getPropertyObject());
        mMixpanelAPI.getPeople().initPushHandling(MyApplication.getAppContext().getResources().getString(R.string.GCM_project_number));
    }

    public void linkAliasProperty(MixPanelProperty property) {
        mMixpanelAPI.alias(String.valueOf(property.getPropertyObject()), null);
    }

    public void setProfileProperty(MixPanelProperty property) {
        // Profile Property를 설정하기 전에 identify를 먼저 호출해야한다.
        mMixpanelAPI.getPeople().identify(String.valueOf(SVUtil.getUserIdx(this)));
        mMixpanelAPI.getPeople().set(property.getPropertyName(), property.getPropertyObject());
    }

    public void updateNumericProperty(MixPanelProperty property) {
        mMixpanelAPI.getPeople().increment(property.getPropertyName(), Double.valueOf(property.getPropertyObject().toString()));
    }

    // Navigate back. Same as pressing back button
    public void onClickNavigateBack(View view) {
        MixPanel.mixPanel_trackWithOutProperties("Navigate Back");
        finish();
        overridePendingTransition(R.anim.transition_slide_in_from_left, R.anim.transition_slide_out_to_right);
    }

    public void onClickCallPlating(View view) {
        MixPanel.mixPanel_trackWithOutProperties("Show Call Plating Dialog");
        MakePhoneCallDialog.showDialogForPhoneCall(this, "070-7777-6114");
    }

    @Override
    public void onBackPressed() {
        MixPanel.mixPanel_trackWithOutProperties("Pressed Back Button");
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_slide_in_from_left, R.anim.transition_slide_out_to_right);
    }

    // Start cart activity if there is more than one item in the cart
    public void onClickStartCartActivity(View view) {
        MixPanel.mixPanel_trackWithOutProperties("Show Cart");
        // If cart is empty, don't go to CartActivity.
        if(Cart.getCartInstance().size() == 0) {
            Toast.makeText(this, Constant.CART_IS_EMPTY, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
    }

    public void updateGpsLocationOnce() {
        startGettingGPSLocation();
    }

    public void startGettingGPSLocation() {
        Debug.d(LOG_TAG, "startGettingGPSLocation: Starts");
        // Build google api client for getting location
        buildGoogleApiClient();
        // Location listener for constant listening
        createLocationRequest();

        // Google Play Service for current location
        if (mGoogleApiClient != null) {
            Debug.d(LOG_TAG, "mGoogleApiClient: non-null");
            mGoogleApiClient.connect();
        }
    }

    // Getting current location by using Google Play Service
    protected synchronized void buildGoogleApiClient() {
        Debug.d(LOG_TAG, "buildGoogleApiClient: Starts");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        Debug.d(LOG_TAG, "createLocationRequest: Starts");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Once connected with google api, get the location
        Constant.currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Debug.d(LOG_TAG, "onConnected: Location = " + Constant.currentLocation);


        // Update location
        if(GPS.isGpsTurnedOn()) {
            startLocationUpdates();
            if(Constant.currentLocation != null) {
                setUserLocation(SVUtil.getUserIdx(getApplicationContext()), Constant.currentLocation.getLatitude(), Constant.currentLocation.getLongitude());
            }
        } else {
            // If GPS is off, send null to MixPanel
            MixPanel.mixPanel_SendUserLocation(Constant.currentLocation);
        }
    }

    protected void startLocationUpdates() {
        Debug.d(LOG_TAG, "startLocationUpdates: Starts");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Debug.d(LOG_TAG, "onLocationChanged: Location = " + location.toString());
        Constant.currentLocation = location;

        // This ensures location is fetched only once. This will reduce battery consumption.
        stopGettingGPSLocation();

        // MixPanel - Send location of the user
        MixPanel.mixPanel_SendUserLocation(Constant.currentLocation);
    }

    public void stopGettingGPSLocation() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                stopLocationUpdates();
                mGoogleApiClient.disconnect();
            }
        }
    }

    protected void stopLocationUpdates() {
        Debug.d(LOG_TAG, "stopLocationUpdates: Starts");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    // This function is rarely called
    @Override
    public void onConnectionSuspended(int i) {
        Debug.d(LOG_TAG, "onConnectionSuspended: Starts");
        mGoogleApiClient.connect();
    }

    // This function is rarely called
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Debug.d(LOG_TAG, "onStatusChanged: Starts. ErrorCode: " + connectionResult.getErrorCode());
    }
}
