package com.plating.application;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.plating.BuildConfig;
import com.plating.R;
import com.plating.object.SingleMenu;
import com.plating.pages.d_menu_detail.MenuDetailActivity;
import com.plating.sdk_tools.crashlytics.CrashlyticsAPI;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.util.SVUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

//import com.kakao.auth.ApprovalType;
//import com.kakao.auth.AuthType;
//import com.kakao.auth.Session;


/**
 * Created by cheehoonha on 6/3/15.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication sInstance;


    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("LifeCycle", "Myapplication onCreate");
        sInstance = this;

        // Facebook SDK initialization
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // KakaoAPI SDK initialization
        Session.initialize(this, AuthType.KAKAO_TALK);

        // Disable crashlytics if in debug mode
        CrashlyticsAPI.disableCrashlyticsForDevelopmentMode();

        AppsFlyerLib.setAppsFlyerKey("5aJTmR8RGtTwQBiia8vPzh");
        AppsFlyerLib.sendTracking(getApplicationContext());

        AppsFlyerLib.registerConversionListener(this, new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                String media_source = "";
                String campaign_name = "";
                for (String attrName : conversionData.keySet()) {
                    Log.d(AppsFlyerLib.LOG_TAG, "attribute: " + attrName + " = " +
                            conversionData.get(attrName));
                    media_source = conversionData.get(attrName);
                    campaign_name = conversionData.get(attrName);
                }
                SVUtil.Set_AppsFlyer_media_source(getApplicationContext(), media_source);
                SVUtil.Set_AppsFlyer_campaign(getApplicationContext(), campaign_name);
            }
            @Override
            public void onInstallConversionFailure(String errorMessage) {
                Log.d(AppsFlyerLib.LOG_TAG, "error getting conversion data: " +
                        errorMessage);
            }
            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
            }
            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d(AppsFlyerLib.LOG_TAG, "error onAttributionFailure : " + errorMessage);
            }
        });
        /*
        AppsFlyerLib.registerConversionListener(getApplicationContext(), new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                String media_source = "";
                String campaign_name = "";
                Log.d("AppsFlyer", "attribute:");
                /*
                for (String attrName : conversionData.keySet()) {
                    //Log.d("AppsFlyer", "attribute: " + attrName + " = " + conversionData.get(attrName));
                    Log.d("AppsFlyer", conversionData.get(attrName));
                    /*
                    if (attrName == "media_source") {
                        media_source = conversionData.get(attrName);
                    } else if (attrName == "campaign_name") {
                        campaign_name = conversionData.get(attrName);
                    }

                }

                SVUtil.Set_AppsFlyer_media_source(getApplicationContext(), media_source);
                SVUtil.Set_AppsFlyer_campaign(getApplicationContext(), campaign_name);


            }

            @Override
            public void onInstallConversionFailure(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        });
        */



        Log.d("Alias", "GetUserIdx : "+SVUtil.GetUserIdx(getApplicationContext())+
                " isCreatedAlias : "+SVUtil.isCreatedAlias(getApplicationContext()));

        if ((SVUtil.GetUserIdx(getApplicationContext()) != 0) && (SVUtil.isCreatedAlias(getApplicationContext()) == false)) {
            Log.d("Alias", "Creating Alias for existing user");
            get_name_from_server();
        }



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void get_name_from_server() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.plating.co.kr/user/name?user_idx="+SVUtil.GetUserIdx(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jo = new JSONObject(response);
                            String user_name = jo.getString("name");


                            String identity = SVUtil.GetUserIdx(getApplicationContext())+" - "+user_name;
                            MixPanel.getMixPanelInstance().alias(identity, null);

                            MixPanel.getMixPanelInstance().getPeople().identify(identity);
                            MixPanel.getMixPanelInstance().getPeople().set("name", identity);

                            //Util.sharedInstance.mixPanel.people.set(["media_source":UserInfo.sharedInstance.appsflyer_media_source(), "campaign":UserInfo.sharedInstance.appsflyer_campaign()])

                            MixPanel.getMixPanelInstance().getPeople().set("media_source", SVUtil.AppsFlyer_media_source(getApplicationContext()));
                            MixPanel.getMixPanelInstance().getPeople().set("campaign", SVUtil.AppsFlyer_campaign(getApplicationContext()));


                            SVUtil.CreatedAlias(getApplicationContext());
                            MixPanel.getMixPanelInstance().identify(identity);

                            MixPanel.getMixPanelInstance().getPeople().initPushHandling(MyApplication.getAppContext().getResources().getString(R.string.GCM_project_number));





                        } catch(Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static boolean isDebugMode() {
        if (BuildConfig.DEBUG) {
            return true;
        }

        return false;
    }


}
