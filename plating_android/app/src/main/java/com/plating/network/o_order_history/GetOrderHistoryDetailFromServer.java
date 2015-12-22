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
import com.plating.object.OrderDetail;
import com.plating.object.OrderHistory;
import com.plating.pages.o_order_history.OrderHistoryActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class GetOrderHistoryDetailFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetOrderHistoryDetailFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, int orderIdx) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(orderIdx),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        OrderHistory orderHistoryListRowArrayList = convertJsonToOrderHistoryList(response);
                        ((OrderHistoryActivity)context).getOrderHistoryFromServer_callback(orderHistoryListRowArrayList);
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
        return RequestURL.SERVER__GET_ORDER_HISTORY_DETAIL + "?order_idx=" + orderIdx;
    }

    private static OrderHistory convertJsonToOrderHistoryList(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToOrderHistoryList: Start");
        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();

        if (response != null || response.length() > 0) {
            int orderIdx = response.optInt("order_detail", 0);
            int totalPrice = response.optInt("total_price", 0);
            String timeSlot = response.optString("time_slot", "");
            int orderStatus = response.optInt("order_status", 2);
            String description = response.optString("description", "");
            int reviewStatus = response.optInt("review_status", 0);
            JSONArray orderHistoryDetail = response.optJSONArray("order_detail");
            for (int i = 0; i < orderHistoryDetail.length(); i++) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.menuName = orderHistoryDetail.optJSONObject(i).optString("name_menu", "");
                orderDetail.amount = orderHistoryDetail.optJSONObject(i).optInt("amount", -1);

                orderDetailArrayList.add(orderDetail);
            }

            OrderHistory orderHistory = new OrderHistory(orderIdx, orderStatus, description, totalPrice, timeSlot, reviewStatus, orderDetailArrayList);
            return orderHistory;
        }

        return new OrderHistory(0,0,"",0,"",0, null);
    }
}
