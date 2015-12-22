package com.plating.network.c_daily_menu_list;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.RequestURL;
import com.plating.object.CouponListRow;
import com.plating.object.DailyMenu;
import com.plating.object.UserDialog;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.util.SVUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by home on 15. 12. 15..
 */
public class GetDialogFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetDialogFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.GetUserIdx(context)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());

                        if (response != null || response.length() > 0) {
                            boolean usedCoupon = response.optBoolean("usedCoupon", false);
                            String image_url_dialog = response.optString("image_url_dialog", "");
                            int redirect = response.optInt("redirect", -1);
                            ((DailyMenuListActivity)context).getDialogFromServer_Callback(usedCoupon, image_url_dialog, redirect);
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
        return RequestURL.SERVER__GET_DIALOG + "?user_idx=" + userIdx;
    }
}
