package com.plating.network.d_menu_detail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.network.RequestURL;
import com.plating.object.CustomerReview;
import com.plating.pages.d_menu_detail.MenuDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class GetMoreReviewFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetMoreReviewFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, final int menuIdx) {
        Log.d("", "menu_idx : " + menuIdx);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(menuIdx),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "GetMoreReviewFromServer.response: " + response.toString());
                        ArrayList<CustomerReview> customerReviewArrayList = convertJsonToReviews(response);
                        ((MenuDetailActivity) context).getMoreReviewFromServer_Callback(customerReviewArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "getMenuDetailFromServer.error: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl(int menu_idx) {
        return RequestURL.SERVER__GET_MORE_REVIEW_FROM_SERVER + "?menu_idx=" + menu_idx;
    }

    private static ArrayList<CustomerReview> convertJsonToReviews(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToReviews: Start");
        ArrayList<CustomerReview> customerReviewArrayList = new ArrayList<>();

        if (response != null || response.length() > 0) {
            JSONArray reviewArray = response.optJSONArray("review");
            for (int i = 0; i < reviewArray.length(); i++) {
                CustomerReview customerReview = new CustomerReview();
                customerReview.idx = reviewArray.optJSONObject(i).optInt("idx", -1);
                customerReview.rating = reviewArray.optJSONObject(i).optDouble("rating", 4.5);
                customerReview.comment = reviewArray.optJSONObject(i).optString("comment", "");
                customerReview.time = reviewArray.optJSONObject(i).optString("rated_time", "");
                customerReview.mobile = reviewArray.optJSONObject(i).optString("mobile", "");
                customerReviewArrayList.add(customerReview);
            }
        }

        // This should not be reached. In the case it is reached, return empty object
        return customerReviewArrayList;
    }
}
