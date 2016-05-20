package com.plating.network.i_set_location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.network.RequestURL;
import com.plating.object.AvailableLocation;
import com.plating.pages.i_set_location.SetLocationActivity;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class GetAvailableLocations {
    public static String LOG_TAG = Constant.APP_NAME + "getAvailableLocations";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, String address) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(address),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("", "response : "+response);
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        ArrayList<AvailableLocation> availableLocationArrayList = convertJsonToAvailableAddresses(response);
                        ((SetLocationActivity)context).getAvailableLocationsFromServer_Callback(availableLocationArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "getDataFromServer.error: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl(String address) {
        return RequestURL.SERVER__GET_AVAILABLE_LOCATIONS + "?query=" + address;
    }

    private static ArrayList<AvailableLocation> convertJsonToAvailableAddresses(JSONArray response) {

        Debug.d(LOG_TAG, "convertJsonToMenuDetail: Start");
        ArrayList<AvailableLocation> availableLocationArrayList = new ArrayList<AvailableLocation>();

        if (response != null || response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                String roadNameAddress = response.optJSONObject(i).optString("roadNameAddress", "N/A");
                String jibunAddress = response.optJSONObject(i).optString("jibunAddress", "N/A");
                boolean available = response.optJSONObject(i).optBoolean("available", false);
                double latitude = response.optJSONObject(i).optDouble("latitude", 0);
                double longitude = response.optJSONObject(i).optDouble("longitude", 0);
                String area = response.optJSONObject(i).optString("area", "N/A");
                int reservationType = response.optJSONObject(i).optInt("reservationType", 0);
                AvailableLocation availableLocation = new AvailableLocation(roadNameAddress, jibunAddress, available, latitude, longitude, area, reservationType);
                availableLocationArrayList.add(availableLocation);
            }

            return availableLocationArrayList;
        }

        // This should not be reached. In the case it is reached, return empty object
        return availableLocationArrayList;
    }
}
