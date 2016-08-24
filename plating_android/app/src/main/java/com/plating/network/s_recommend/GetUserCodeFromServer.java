package com.plating.network.s_recommend;

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
import com.plating.pages.s_refer.ReferActivity;
import com.plating.util.SVUtil;

import org.json.JSONObject;

/**
 * Created by home on 15. 12. 21..
 */
public class GetUserCodeFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetUserCodeFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.getUserIdx(context)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        String str = convertJsonToString(response);
                        Debug.d(LOG_TAG, "convertJsonToString: " + str);
                        ((ReferActivity) context).getUserCodeFromServer_Callback(str);

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
        return RequestURL.SERVER__GET_USERCODE + "?user_idx=" + userIdx;
    }

    private static String convertJsonToString(JSONObject response) {
        Debug.d(LOG_TAG, "convertJsonToCouponList: Start");
        String returnStr = "";

        if (response != null || response.length() > 0) {

            for (int i = 0; i < response.length(); i++) {
                returnStr = response.optString("user_code", "");
            }
        }

        return returnStr;
    }
}
