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
public class GetPolicyReferPointFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetPolicyReferPointFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getRequestUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        String numReferPoint = response.optString("numReferPoint", "");
                        String korReferPoint = response.optString("korReferPoint", "");
                        ((ReferActivity) context).getPolicyReferPointFromServer_Callback(numReferPoint, korReferPoint);

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

    public static String getRequestUrl() {
        return RequestURL.SERVER__GET_POLICY_REFER_POINT;
    }
}
