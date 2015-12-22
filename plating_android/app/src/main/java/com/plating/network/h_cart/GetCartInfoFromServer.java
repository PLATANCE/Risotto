package com.plating.network.h_cart;

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
import com.plating.object.PlaceOrderResult;
import com.plating.object_singleton.Cart;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by redjjol on 14/09/15.
 */
public class GetCartInfoFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetCartInfoFromServer";

    public static void sendDataToServer(final Context context, RequestQueue requestQueue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        PlaceOrderResult placeOrderResult = convertJsonToPlaceOrderResult(response);
//                        ((CartActivity) context).placeOrderToServer_Callback(placeOrderResult);
//                        ((CartActivity) context).getCartInfo_Callback(placeOrderResult);

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
        return RequestURL.SERVER__PLACE_ORDER;
    }

    public static JSONObject createJsonForPost() {
        Cart cart = Cart.getCartInstance();
        int menuIdx = cart.getMenuList().get(0).menu_idx;
        int orderAmount = cart.getMenuList().get(0).count;

        JSONObject json = new JSONObject();
        try {
            json.put("user_idx", 46);
            json.put("menu_idx", menuIdx);
            json.put("time_slot", 0);
            json.put("order_amount", orderAmount);
            json.put("credit_used", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private static PlaceOrderResult convertJsonToPlaceOrderResult(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToMenuDetail: Start");

        if (response != null || response.length() > 0) {
            String status = response.optString("status", "N/A");
            int orderIdx = response.optInt("order_idx", -1);

            return new PlaceOrderResult(status, orderIdx);
        }

        // This should not be reached. In the case it is reached, return empty object
        return new PlaceOrderResult();
    }
}
