package com.plating.sdk_tools.mix_panel;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.MyApplication;
import com.plating.network.i_set_location.SetUserGPSLocation;
import com.plating.network.p_write_review_activity.SubmitReview;
import com.plating.util.SVUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/1/15.
 */

// Singleton class for MixPanel
public class MixPanel {
    private static String LOG_TAG = "MixPanel";
    private static MixpanelAPI mMixPanel;

    /********************************
     * * MIX PANEL INITIALIZATION * *
     ********************************/
    // Get instance of Mixpanel
    public static MixpanelAPI getMixPanelInstance() {
        if (mMixPanel == null) {
            Context context = MyApplication.getAppContext();
            String projectToken = getMixPanelToken(context);
            mMixPanel = MixpanelAPI.getInstance(context, projectToken);
        }

        return mMixPanel;
    }

    public static String getMixPanelToken(Context context) {
        if(MyApplication.isDebugMode()) {
            return context.getString(R.string.mixpanel_token_development_version);
        }

        // If not debug mode, it's a release mode
        return context.getString(R.string.mixpanel_token_release_version);
    }


    /*************************
     * * MIX PANEL METHODS * *
     *************************/

    public static void mixPanel_trackWithOutProperties(String eventName) {
        getMixPanelInstance().track(eventName);
    }

    public static void mixPanel_trackWithProperties(String eventName, ArrayList<MixPanelProperty> mixPanelPropertyArrayList) {
        JSONObject properties = new JSONObject();

        for(int i = 0; i < mixPanelPropertyArrayList.size(); i++) {
            try {
                properties.put(mixPanelPropertyArrayList.get(i).eventName, mixPanelPropertyArrayList.get(i).eventObject);
            } catch (JSONException e) {
                Log.d(LOG_TAG, "mixPanel_trackWithProperties: Error = " + e.toString());
            }
        }

        getMixPanelInstance().track(eventName, properties);
    }

    // Set getPeople
    public static void mixPanel_getPeople(String mixPanelUserIdentity) {
        getMixPanelInstance().getPeople().identify(mixPanelUserIdentity);
        getMixPanelInstance().getPeople().set("name", mixPanelUserIdentity);
        getMixPanelInstance().getPeople().initPushHandling(MyApplication.getAppContext().getResources().getString(R.string.GCM_project_number));
    }


    // This is called whenever user location is fetched using GoogleLocationApi
    public static void mixPanel_SendUserLocation(Location location) {
        MixpanelAPI mixpanelAPI = getMixPanelInstance();
        try {
            JSONObject properties = new JSONObject();
            // If location is not obtained, just send dummy location
            if(location == null) {
                location = new Location("dummy_location");
                location.setLatitude(0.0);
                location.setLongitude(0.0);
            }

            properties.put("Latitude", location.getLatitude());
            properties.put("Longitude", location.getLongitude());

            mixpanelAPI.track("(GPS) CURRENT LOCATION", properties);
        } catch (JSONException e) {
            Log.d(LOG_TAG, Constant.MIX_PANEL_JSON_ERROR, e);
        }
    }

    // Send this dummy property. This will re-order people list.
    public static void mixPanel_orderPeopleListByLatestUsage() {
        getMixPanelInstance().getPeople().set("Plating Temp", "Dummy property");
    }

    /******************************
     * * MIX PANEL SYSTEM CALLS * *
     ******************************/

    // MixPanel sends data every 60 seconds.
    // Thus when user exits the app, everything need to be flushed to the MixPanel server
    public static void flush() {
        MixpanelAPI mixpanelAPI = getMixPanelInstance();
        mixpanelAPI.flush();
    }
}
