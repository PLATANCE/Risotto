package com.plating.network.p_write_review_activity;

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

import org.json.JSONObject;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class CancelWritingReview {
    public static String LOG_TAG = Constant.APP_NAME + "CancelWritingReview";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, int orderIdx) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(orderIdx),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        // Don't do anything
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

    public static String getRequestUrl(int orderIdx) {
        return RequestURL.SERVER__CANCEL_WRITING_REVIEW + "?order_idx=" + orderIdx;
    }
}
