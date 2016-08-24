package com.plating.network.r_coupon;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.network.RequestURL;
import com.plating.object.CouponListRow;
import com.plating.pages.h_cart.CartActivity;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.util.SVUtil;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by home on 15. 12. 11..
 */
public class GetMyCouponListFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetMyCouponListFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.getUserIdx(context)),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        ArrayList<CouponListRow> couponListRowArrayList = convertJsonToCouponList(response);

                        Debug.d(LOG_TAG, "context: " + context.getClass().getSimpleName());
                        if(context instanceof MyCouponListActivity) {
                            ((MyCouponListActivity) context).getCouponListFromServer_Callback(couponListRowArrayList);
                        } else if(context instanceof CartActivity) {
                            ((CartActivity) context).getCouponListFromServer_Callback(couponListRowArrayList);
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
        return RequestURL.SERVER__GET_COUPON_LIST + "?user_idx=" + userIdx;
    }

    private static ArrayList<CouponListRow> convertJsonToCouponList(JSONArray response) {
        Debug.d(LOG_TAG, "convertJsonToCouponList: Start");
        ArrayList<CouponListRow> couponListRowArrayList = new ArrayList<>();

        if (response != null || response.length() > 0) {

            for (int i = 0; i < response.length(); i++) {
                CouponListRow couponListRow = new CouponListRow();

                couponListRow.setIdx(response.optJSONObject(i).optInt("idx", -1));
                couponListRow.setName(response.optJSONObject(i).optString("name", ""));
                couponListRow.setTxt(response.optJSONObject(i).optString("txt", ""));
                couponListRow.setPrice(response.optJSONObject(i).optInt("price", -1));
                couponListRow.setExp_date(response.optJSONObject(i).optString("exp_date", ""));
                couponListRow.setImage_url_coupon(response.optJSONObject(i).optString("image_url_coupon", ""));

                couponListRowArrayList.add(couponListRow);
            }
        }

        return couponListRowArrayList;
    }
}
