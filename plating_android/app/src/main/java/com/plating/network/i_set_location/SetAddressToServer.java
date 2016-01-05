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
import com.plating.pages.i_set_location.AddressListActivity;
import com.plating.pages.i_set_location.AddressListAdapter;
import com.plating.util.SVUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by home on 16. 1. 4..
 */
public class SetAddressToServer {
    public static String LOG_TAG = Constant.APP_NAME + "SetAddressToServer";

    public static void sendDataToServer(final Context context, RequestQueue requestQueue, int idx, String mode) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(idx, SVUtil.GetUserIdx(context), mode),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        ((AddressListActivity)context).updateAddress_Callback(response.optString("result", ""));
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
        return RequestURL.SERVER__UPDATE_ADDRESS;
    }

    public static JSONObject createJsonForPost(int idx, int user_idx, String mode) {
        JSONObject json = new JSONObject();
        try {
            json.put("idx", idx);
            json.put("user_idx", user_idx);
            json.put("mode", mode);
            Debug.d(LOG_TAG, "createJsonForPost: " + json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
