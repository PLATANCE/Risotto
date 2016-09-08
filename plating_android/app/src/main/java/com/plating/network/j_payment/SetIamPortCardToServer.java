package com.plating.network.j_payment;

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
import com.plating.pages.i_set_location.AddressListActivity;
import com.plating.pages.j_payment.AddIamPortCardActivity;
import com.plating.util.SVUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rooney on 16. 9. 6..
 */
public class SetIamPortCardToServer {
    public static String LOG_TAG = Constant.APP_NAME + "SetIamPortCardToServer";

    public static void sendDataToServer(final Context context, RequestQueue requestQueue, String cardNumber, String expiry, String birth, String passwordPre2Digit) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(cardNumber, expiry, birth, passwordPre2Digit, SVUtil.GetUserIdx(context)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        int code = response.optInt("code", 0);
                        String message = response.optString("message", "");
                        ((AddIamPortCardActivity)context).registCard_Callback(code, message);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "sendDataToServer.error: " + error.toString());
                        Toast.makeText(context, Constant.IAMPORTCARD_REGIST_ERROR, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl() {
        return RequestURL.CREATE_BILL_KEY;
    }

    public static JSONObject createJsonForPost(String cardNumber, String expiry, String birth, String passwordPre2Digit, int userIdx) {
        JSONObject json = new JSONObject();
        try {
            json.put("cardNumber", cardNumber);
            json.put("expiry", expiry);
            json.put("birth", birth);
            json.put("passwordPre2Digit", passwordPre2Digit);
            json.put("userIdx", userIdx);
            Debug.d(LOG_TAG, "createJsonForPost: " + json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
