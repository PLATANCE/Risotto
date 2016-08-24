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
import com.plating.object.MenuInCart;
import com.plating.object.PlaceOrderResult;
import com.plating.object_singleton.Cart;
import com.plating.util.SVUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class PlaceOrderToServer {
    public static String LOG_TAG = Constant.APP_NAME + "PlaceOrderToServer";
    private static Context cx;

    public static void sendDataToServer(final Context context, RequestQueue requestQueue) {
        cx = context;
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
        String menu_idx = "";
        String order_amount = "";
        for (MenuInCart menu : cart.getMenuList()) {
            menu_idx += menu.menu_idx;

            menu_idx += "|";
            order_amount += menu.count;
            order_amount += "|";
        }
        menu_idx.substring(0, menu_idx.length()-1);
        order_amount.substring(0, order_amount.length()-1);

//        int menuIdx = cart.getMenuList().get(0).menu.idx;
//        int orderAmount = cart.getMenuList().get(0).count;

        JSONObject json = new JSONObject();
        try {
            json.put("user_idx", SVUtil.getUserIdx(cx));
            json.put("menu_idx", menu_idx);
            json.put("time_slot", Cart.time_slot_idx);
            json.put("order_amount", order_amount);
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
