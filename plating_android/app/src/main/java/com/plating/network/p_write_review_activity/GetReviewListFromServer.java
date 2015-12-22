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
import com.plating.object.WriteReviewRow;
import com.plating.pages.p_write_review_activity.WriteReviewListActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class GetReviewListFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetReviewListFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, int orderIdx) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(orderIdx),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        ArrayList<WriteReviewRow> writeReviewRowArrayList = convertJsonToWriteReviewList(response);
                        ((WriteReviewListActivity)context).getReviewListFromServer_callback(writeReviewRowArrayList);
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
        String url ="http://api.plating.co.kr/review/written_by_user?order_idx="+ orderIdx;
        return url;
    }

    private static ArrayList<WriteReviewRow> convertJsonToWriteReviewList(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToWriteReviewList: Start");
        ArrayList<WriteReviewRow> writeReviewRowArrayList = new ArrayList<>();

        if (response != null || response.length() > 0) {
            JSONArray writeReviewList = response.optJSONArray("review_list");
            for (int i = 0; i < writeReviewList.length(); i++) {
                WriteReviewRow writeReviewRow = new WriteReviewRow();
                writeReviewRow.idx = writeReviewList.optJSONObject(i).optInt("idx", 0);
                writeReviewRow.menuName = writeReviewList.optJSONObject(i).optString("name_menu", "");
                writeReviewRow.menuImageUrl = writeReviewList.optJSONObject(i).optString("image_url_menu", "");
                writeReviewRow.rating = (float) (writeReviewList.optJSONObject(i).optDouble("rating", 0));
                writeReviewRow.comment = writeReviewList.optJSONObject(i).optString("comment", "");

                writeReviewRowArrayList.add(writeReviewRow);
            }
        }
        return writeReviewRowArrayList;
    }
}
