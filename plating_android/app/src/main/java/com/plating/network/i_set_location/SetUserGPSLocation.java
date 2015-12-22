package com.plating.network.i_set_location;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.application.PlatingActivity;
import com.plating.network.RequestURL;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.pages.p_write_review_activity.WriteReviewListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by seungjong on 2015-12-08.
 */
public class SetUserGPSLocation {
    public static String LOG_TAG = Constant.APP_NAME + "SetUserGPSLocation";
    public static void sendDataToServer(final Context context, RequestQueue requestQueue, int user_idx ,double lat, double lon) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(user_idx, lat, lon),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        ((PlatingActivity)context).setUserLocation_callBack();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "sendDataToServer.error: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl() {
        return RequestURL.SERVER__SET_USER_GPS_LOCATION;
    }
    public static JSONObject createJsonForPost(int user_idx, double lat, double lon) {
        Debug.d(LOG_TAG, "sendDataToServer.response: " + "user_idx : " + user_idx + " latitude : " + lat + " longitude :" + lon);
        JSONObject json = new JSONObject();
        try {
            json.put("user_idx", user_idx);
            json.put("lat", lat);
            json.put("lon", lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
