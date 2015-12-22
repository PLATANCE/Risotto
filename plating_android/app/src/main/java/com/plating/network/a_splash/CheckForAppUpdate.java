package com.plating.network.a_splash;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.network.RequestURL;
import com.plating.pages.a_splash.SplashActivity;

import org.json.JSONObject;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class CheckForAppUpdate {
    public static String LOG_TAG = Constant.APP_NAME + "CheckForAppUpdate";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, int versionCode) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(versionCode),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.onResponse: " + response.toString());
                        // Convert json to object
                        boolean updateExists = response.optBoolean("available", false);
                        ((SplashActivity)context).checkForAppUpdate_ResultSuccessCallback(updateExists);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "getDataFromServer: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl(int versionCode) {
        return RequestURL.SERVER__APP_UPDATE_AVAILABLE + "?version_code=" + versionCode;
    }
}
