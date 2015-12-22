package com.plating.network.b_login;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.pages.b_login.LogInActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by redjjol on 14/09/15.
 */

public class KakaoSignUpRegistrationToPlatingServer {
    public static String LOG_TAG = Constant.APP_NAME + "KakaoSignUpRegistrationToPlatingServer";

    public static void sendDataToServer(final Context context, RequestQueue requestQueue, String osName, String loginType, String userId, String gcmToken, String osVersion, String device, String deviceName, String thumbnailImage, String profileImage, final String nickname) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getRequestUrl(),
                createJsonForPost(osName, loginType, userId, gcmToken, osVersion, device, deviceName, thumbnailImage, profileImage, nickname),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Debug.d(LOG_TAG, "sendDataToServer.response: " + response.toString());
                        String signUpOrLogIn = response.optString("from");
                        int userIdx = response.optJSONObject("user_info").optInt("user_idx");

                        ((LogInActivity) context).KakaoSignUpRegistrationToPlatingServer_callback(signUpOrLogIn, userIdx, nickname);
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
        return "http://api.plating.co.kr/signup";
    }

    public static JSONObject createJsonForPost(String osName, String loginType, String userId, String gcmToken, String osVersion, String device, String deviceName, String thumbnailImage, String profileImage, String nickname) {
        JSONObject json = new JSONObject();
        try {
            json.put("os_type", osName);
            json.put("login_type", loginType);
            json.put("user_id", userId);
            json.put("push_token", gcmToken);
            json.put("os_version", osVersion);
            json.put("device", device);
            json.put("device_name", deviceName);
            json.put("thumbnail_image", thumbnailImage);
            json.put("profile_image", profileImage);
            json.put("nickname", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}


// OLD CODE FROM TERRY
/*
    private void SignUp_kakao() {
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                "http://api.plating.co.kr/signup",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, "response: " + response.toString());
                        String signUpOrLogIn = response.optString("from");
                        int user_idx = response.optJSONObject("user_info").optInt("user_idx");

                        SVUtil.SetUserIdx(mContext, user_idx);

                        // Do all MixPanel alias, identity, people stuff
                        String mixPanelIdentity = user_idx + "-" + nickname;
                        mixPanel_signUpOrLogIn("KakaoAPI", signUpOrLogIn, mixPanelIdentity);

                        // Go to DailyMenuListActivity
                        startDailyMenuListActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, "(ERROR) SignUp_kakao: " + error.toString());
                    }
                }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("os_type", "android");
                params.put("login_type", "kakao");
                params.put("user_id", user_id);
                params.put("push_token", SVUtil.GetGcmtoken(mContext));
                params.put("os_version", os_version);
                params.put("device", device);
                params.put("device_name", device_name);
                params.put("thumbnail_image", thumbnail_image);
                params.put("profile_image", profile_image);
                params.put("nickname", nickname);

                Log.d(LOG_TAG, "Register User To Plating Server: " + params.toString());

                return params;
            }
        };
        mRequestQueue.add(myReq);
    }
*/
