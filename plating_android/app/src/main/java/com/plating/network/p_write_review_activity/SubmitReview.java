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
import com.plating.pages.p_write_review_activity.WriteReviewListActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by redjjol on 14/09/15.
 */
public class SubmitReview {
    public static String LOG_TAG = Constant.APP_NAME + "GetCartInfoFromServer";

    public static void sendDataToServer(final Context context, RequestQueue requestQueue, int orderIdx ,String orderDIdx, String rating, String comment) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(orderIdx, orderDIdx, rating, comment),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        ((WriteReviewListActivity)context).submitReview_callback();
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
        return "http://api.plating.co.kr/submit_review";
    }

    public static JSONObject createJsonForPost(int orderIdx ,String orderDIdx, String rating, String comment) {

        JSONObject json = new JSONObject();
        try {
            json.put("order_idx", orderIdx);
            json.put("order_d_idx", orderDIdx);
            json.put("rating", rating);
            json.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
/*
    private static PlaceOrderResult convertJsonToPlaceOrderResult(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToMenuDetail: Start");

        if (response != null || response.length() > 0) {
            String status = response.optString("status", "N/A");
            int orderIdx = response.optInt("order_idx", -1);

            return new PlaceOrderResult(status, orderIdx);
        }

        // This should not be reached. In the case it is reached, return empty object
        return new PlaceOrderResult();
    }*/
}
