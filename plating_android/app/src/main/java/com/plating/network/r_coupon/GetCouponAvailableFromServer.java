package com.plating.network.r_coupon;

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
import com.plating.object_singleton.Cart;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.util.SVUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rooney on 16. 1. 12..
 */
public class GetCouponAvailableFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetCouponAvailableFromServer";

    public static void sendDataToServer(final Context context, RequestQueue requestQueue, int coupon_idx) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(SVUtil.getUserIdx(context), coupon_idx),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());

                        int user_idx = response.optInt("user_idx", -1);
                        int coupon_idx = response.optInt("coupon_idx", -1);
                        boolean available = response.optBoolean("available", false);
                        int sale_price = response.optInt("sale_price", -1);
                        String msg = response.optString("msg", "");
                        ((MyCouponListActivity) context).sendDataToServer_callback(user_idx, coupon_idx, available, sale_price, msg);
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
        return "http://api.plating.co.kr/coupon/coupon_available";
    }

    public static JSONObject createJsonForPost(int user_idx, int coupon_idx) {

        JSONObject json = new JSONObject();
        Cart cart = Cart.getCartInstance();
        try {
            json.put("user_idx", user_idx);
            json.put("coupon_idx", coupon_idx);
            json.put("menu_idx", cart.setMenuIdxParam());
            json.put("menu_amount", cart.setMenuAmountParam());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, json.toString());
        return json;
    }
}
