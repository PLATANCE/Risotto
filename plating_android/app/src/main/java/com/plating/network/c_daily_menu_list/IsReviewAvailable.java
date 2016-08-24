package com.plating.network.c_daily_menu_list;

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
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.util.SVUtil;

import org.json.JSONObject;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class IsReviewAvailable {
    public static String LOG_TAG = Constant.APP_NAME + "isReviewAvailable";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.getUserIdx(context)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        if (response != null || response.length() > 0) {
                            boolean available = response.optBoolean("available", false);
                            int userIdx = response.optInt("order_idx", -1);

                            ((DailyMenuListActivity) context).showWriteReviewActivityIfAvailable_callback(available, userIdx);
                        }
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

    public static String getRequestUrl(int userIdx) {
        return RequestURL.SERVER__IS_REVIEW_AVAILABLE + "?user_idx=" + userIdx;
    }
}
