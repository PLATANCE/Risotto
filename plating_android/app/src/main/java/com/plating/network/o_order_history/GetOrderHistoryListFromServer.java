package com.plating.network.o_order_history;

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
import com.plating.object.OrderHistoryListRow;
import com.plating.pages.o_order_history.OrderHistoryListActivity;
import com.plating.util.SVUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class GetOrderHistoryListFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetOrderHistoryListFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.getUserIdx(context)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        ArrayList<OrderHistoryListRow> orderHistoryListRowArrayList = convertJsonToOrderHistoryList(response);
                        ((OrderHistoryListActivity)context).getOrderHistoryListFromServer_Callback(orderHistoryListRowArrayList);
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
        return RequestURL.SERVER__GET_ORDER_HISTORY + "?user_idx=" + userIdx;
    }

    private static ArrayList<OrderHistoryListRow> convertJsonToOrderHistoryList(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToOrderHistoryList: Start");
        ArrayList<OrderHistoryListRow> orderHistoryListRowArrayList = new ArrayList<>();

        if (response != null || response.length() > 0) {
            JSONArray orderHistoryArray = response.optJSONArray("order_history");
            for (int i = 0; i < orderHistoryArray.length(); i++) {
                OrderHistoryListRow orderHistoryListRow = new OrderHistoryListRow();
                orderHistoryListRow.idx = orderHistoryArray.optJSONObject(i).optInt("order_idx", -1);
                orderHistoryListRow.date = orderHistoryArray.optJSONObject(i).optString("request_date", "");
                orderHistoryListRow.address = orderHistoryArray.optJSONObject(i).optString("address", "") + "\n" + orderHistoryArray.optJSONObject(i).optString("address_detail", "");
                orderHistoryListRow.orderTime = orderHistoryArray.optJSONObject(i).optString("time_slot", "");

                orderHistoryListRowArrayList.add(orderHistoryListRow);
            }
        }

        return orderHistoryListRowArrayList;
    }
}
