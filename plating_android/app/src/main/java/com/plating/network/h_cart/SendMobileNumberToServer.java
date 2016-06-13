package com.plating.network.h_cart;

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
import com.plating.network.RequestURL;
import com.plating.object.PlaceOrderResult;
import com.plating.pages.h_cart.InputMobileForAuthActivity;
import com.plating.util.SVUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rooney on 16. 6. 10..
 */
public class SendMobileNumberToServer {
    public static String LOG_TAG = "SendMobileNumberToServer";
    public static void sendDataToServer(final Context context, RequestQueue requestQueue, String phoneNumber) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(context),
                createParamForPost(phoneNumber),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        boolean isSent = response.optBoolean("isSent");
                        boolean isServerError = response.optBoolean("isServerError");
                        String message = response.optString("message");
                        if(isSent && !isServerError) {
                            String phoneNumber = response.optString("phoneNumber");
                            ((InputMobileForAuthActivity)context).sendMobileNumberToServer_callback(phoneNumber);
                        } else {
                            ((InputMobileForAuthActivity)context).sendMobileNumberToServer_callback(isSent, isServerError, message);
                        }
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

    public static String getRequestUrl(Context context) {
        String url =  RequestURL.REQUEST_AUTH_NUMBER;
        url = url.replace("{userIdx}", Integer.toString(SVUtil.GetUserIdx(context)));
        Log.d(LOG_TAG, url);
        return url;
    }

    public static JSONObject createParamForPost(String mobileNumber) {
        JSONObject json = new JSONObject();
        try {
            json.put("phoneNumber", mobileNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
