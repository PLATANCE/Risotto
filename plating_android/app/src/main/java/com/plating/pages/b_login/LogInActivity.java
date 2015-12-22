package com.plating.pages.b_login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.APIErrorResult;
import com.kakao.auth.Session;
import com.kakao.auth.SessionCallback;
import com.kakao.usermgmt.MeResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.b_login.KakaoSignUpRegistrationToPlatingServer;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.pages.z_other.ActWebView;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.sdk_tools.mix_panel.MixPanelProperty;
import com.plating.util.SVUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by redjjol on 17/09/15.
 */
public class LogInActivity extends PlatingActivity implements View.OnClickListener {
    private LoginButton Button_fb;
    private com.kakao.usermgmt.LoginButton Button_kakao;
    private ImageButton Btn_fb_fake, Btn_kakao_fake;

    private CallbackManager callbackManager;

    private String user_id, os_version, device, device_name;

    private String firstName, lastName, linkURL, refreshDate, email, birthday, gender;
    private String thumbnail_image, profile_image, nickname;

    private SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        os_version = Build.VERSION.SDK_INT + "";
        device = SVUtil.getDeviceName();
        device_name = SVUtil.getUsername(this);

        callbackManager = CallbackManager.Factory.create();

        Btn_fb_fake = (ImageButton) findViewById(R.id.act_login_fb_fake);
        Btn_kakao_fake = (ImageButton) findViewById(R.id.act_login_kakao_fake);
        Btn_fb_fake.setOnClickListener(this);
        Btn_kakao_fake.setOnClickListener(this);

        Button_fb = (LoginButton) findViewById(R.id.act_login_fb);

//        Session.getCurrentSession().implicitOpen();

        Button_kakao = (com.kakao.usermgmt.LoginButton) findViewById(R.id.act_login_kakao);

        List<String> permissions = new ArrayList<>();

        setKakaoAPICallBack();


        // If your app asks for
        // more than public_profile, email and user_friends,
        // Facebook must reviewArrayList it before you release it. See Review Guidelines.
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");

        Button_fb.setReadPermissions(permissions);

        Button_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("", "on Success : " + loginResult.getAccessToken().getUserId()); //user_id
                Log.d("", "on Success : " + loginResult.getAccessToken().getToken());
                Log.d("", "on Success : " + loginResult.getAccessToken().getLastRefresh()); // refresh_date
                Log.d("", "on Success : " + loginResult.getAccessToken());
                user_id = loginResult.getAccessToken().getUserId();
                refreshDate = loginResult.getAccessToken().getLastRefresh().toString();


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.d("", "object : " + object);

                                try {

                                    //{"id":"10203400413889597",
                                    // "last_name":"Son",
                                    // "first_name":"Taejeong Terry",
                                    // "gender":"male",
                                    // "email":"staejeong@gmail.com",
                                    // "age_range":{"min":21},
                                    // "name":"Taejeong Terry Son"}

                                    firstName = object.getString("first_name");
                                    lastName = object.getString("last_name");
                                    email = object.getString("email");
                                    gender = object.getString("gender");
                                    linkURL = object.getString("link");

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }

                                SignUp_fb();

                            }

                        }

                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, email, gender, age_range, first_name, last_name, link");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                ToastAPI.showToast("Canceled");
            }

            @Override
            public void onError(FacebookException error) {
                ToastAPI.showToast("Error");
                ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
                mixPanelPropertyArrayList.add(new MixPanelProperty("Reason", "(ERROR) FacebookAPI Error: " + error.toString()));
                MixPanel.mixPanel_trackWithProperties("Sign Up ERROR", mixPanelPropertyArrayList);
            }
        });
    }


    private void kakaoRequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                user_id = userProfile.getId() + "";
                thumbnail_image = userProfile.getThumbnailImagePath();
                profile_image = userProfile.getProfileImagePath();
                nickname = userProfile.getNickname();

                kakao_signUpRegistrationToPlatingServer();
            }

            @Override
            public void onNotSignedUp() {
                Log.d("", "onNotSignedUp");
            }

            @Override
            public void onSessionClosedFailure(APIErrorResult errorResult) {
                Log.d("", "onSessionClosedFailure error : " + errorResult);
                ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
                mixPanelPropertyArrayList.add(new MixPanelProperty("Reason", "(ERROR) KakaoAPI onSessionClosedFailure Error: " + errorResult.toString()));
                MixPanel.mixPanel_trackWithProperties("Sign Up ERROR", mixPanelPropertyArrayList);
            }

            @Override
            public void onFailure(APIErrorResult errorResult) {
                Log.d("", "onSessionClosedFailure : " + errorResult);
                ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
                mixPanelPropertyArrayList.add(new MixPanelProperty("Reason", "(ERROR) KakaoAPI onFailure Error: " + errorResult.toString()));
                MixPanel.mixPanel_trackWithProperties("Sign Up ERROR", mixPanelPropertyArrayList);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    public void setKakaoAPICallBack() {
        callback = new SessionCallback() {
            @Override
            public void onSessionOpened() {
                kakaoRequestMe();
            }

            @Override
            public void onSessionClosed(KakaoException exception) {
                if (exception != null) {
                    exception.printStackTrace();
                    ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
                    mixPanelPropertyArrayList.add(new MixPanelProperty("Reason", "(ERROR) KakaoAPI onSessionClosed Error: " + exception.toString()));
                    MixPanel.mixPanel_trackWithProperties("Sign Up ERROR", mixPanelPropertyArrayList);
                }
            }

            @Override
            public void onSessionOpening() {
            }
        };

        Session.getCurrentSession().addCallback(callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }


    /*********************
     * * BUTTONS *
     *********************/
    @Override
    public void onClick(View v) {
        if (v == Btn_fb_fake) {
            ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
            mixPanelPropertyArrayList.add(new MixPanelProperty("Type", "FacebookAPI"));
            MixPanel.mixPanel_trackWithProperties("Sign Up", mixPanelPropertyArrayList);
            Button_fb.performClick();
        } else if (v == Btn_kakao_fake) {
            ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
            mixPanelPropertyArrayList.add(new MixPanelProperty("Type", "KakaoAPI"));
            MixPanel.mixPanel_trackWithProperties("Sign Up", mixPanelPropertyArrayList);
            Button_kakao.performClick();
        }

    }

    public void onClick_openServiceTerm(View v) {
        Intent intent = new Intent(this, ActWebView.class);
        intent.putExtra("url", "http://api.plating.co.kr/app/term.html");
        startActivity(intent);
    }

    public void onClick_openPrivacyTerm(View v) {
        Intent intent = new Intent(this, ActWebView.class);
        intent.putExtra("url", "http://api.plating.co.kr/app/privacy.html");
        startActivity(intent);
    }


    /*********************
     * * NETWORK *
     *********************/
    private void SignUp_fb() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://api.plating.co.kr/signup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, "SignUp: Result = " + response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            String signUpOrLogIn = jo.getString("from");
                            JSONObject user_info_jo = jo.getJSONObject("user_info");
                            int user_idx = user_info_jo.getInt("user_idx");

                            SVUtil.SetUserIdx(mContext, user_idx);

                            // Do all MixPanel alias, identity, people stuff
                            String mixPanelIdentity = user_idx + "-" + firstName;
                            mixPanel_signUpOrLogIn("FacebookAPI", signUpOrLogIn, mixPanelIdentity);

                            startDailyMenuListActivity();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                Log.d("", "fb params : " + user_id + " / " + device + " / " + device_name + " / " +
                        firstName + " / " + lastName + " / " + linkURL + " / " + refreshDate + " / " + email);

                params.put("os_type", "android");
                params.put("login_type", "fb");
                params.put("user_id", user_id);
                params.put("push_token", SVUtil.GetGcmtoken(mContext));
                params.put("os_version", os_version);
                params.put("device", device);
                params.put("device_name", device_name);

                params.put("firstName", firstName);
                params.put("lastName", lastName);
                params.put("linkURL", linkURL);
                params.put("refreshDate", refreshDate);
                params.put("email", email);
                params.put("gender", gender);

                return params;
            }
        };
        queue.add(myReq);
    }

    public void kakao_signUpRegistrationToPlatingServer() {
        KakaoSignUpRegistrationToPlatingServer.sendDataToServer(this, mRequestQueue, "android", "kakao", user_id, SVUtil.GetGcmtoken(mContext), os_version, device, device_name, thumbnail_image, profile_image, nickname);
    }

    public void KakaoSignUpRegistrationToPlatingServer_callback(String signUpOrLogIn, int userIdx, String nickname) {
        SVUtil.SetUserIdx(this, userIdx);
        // Do all MixPanel alias, identity, people stuff
        String mixPanelIdentity = userIdx + "-" + nickname;
        mixPanel_signUpOrLogIn("KakaoAPI", signUpOrLogIn, mixPanelIdentity);

        // Go to DailyMenuListActivity
        startDailyMenuListActivity();
    }

    // ApiType is either KakaoAPI or FacebookAPI
    // signUpOrLoging = "u" if it is a login.  I.e: "u" stands for update
    public void mixPanel_signUpOrLogIn(String ApiType, String signUpOrLogin, String mixPanelUserIdentity) {
        Log.d(LOG_TAG, "mixPanel_signUpOrLogIn: " + "ApiType = " + ApiType + " signUpOrLogin = " + signUpOrLogin + " mixPanelUserIdentity = " + mixPanelUserIdentity);
        if (signUpOrLogin.equals("u"))
            signUpOrLogin = "Log In Success";
        else
            signUpOrLogin = "Sign Up Success";

        ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
        mixPanelPropertyArrayList.add(new MixPanelProperty("Type", ApiType));
        MixPanel.mixPanel_trackWithProperties(signUpOrLogin, mixPanelPropertyArrayList);

        // If this is a first time user, sign him up
        if (signUpOrLogin.equals("Sign Up Success"))
            MixPanel.getMixPanelInstance().alias(mixPanelUserIdentity, null);

        // Link the user with mixpanel identity
        MixPanel.getMixPanelInstance().identify(mixPanelUserIdentity);
        MixPanel.mixPanel_getPeople(mixPanelUserIdentity);

        // Apps Flyer
        MixPanel.getMixPanelInstance().getPeople().set("media_source", SVUtil.AppsFlyer_media_source(getApplicationContext()));
        MixPanel.getMixPanelInstance().getPeople().set("campaign", SVUtil.AppsFlyer_campaign(getApplicationContext()));

        SVUtil.CreatedAlias(getApplicationContext());
    }

    private void startDailyMenuListActivity() {
        Intent intent;
        intent = new Intent(this, DailyMenuListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
