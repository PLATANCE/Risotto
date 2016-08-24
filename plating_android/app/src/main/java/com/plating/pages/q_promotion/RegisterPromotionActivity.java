package com.plating.pages.q_promotion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.pages.h_cart.dialog.Dialog_CouponCode;
import com.plating.util.SVUtil;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by redjjol on 18/11/2015.
 */
public class RegisterPromotionActivity extends PlatingActivity implements View.OnClickListener{

    TextView TV_point;
    Button Btn_register_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_coupon);

        TV_point = (TextView) findViewById(R.id.TV_point);
        Btn_register_coupon = (Button) findViewById(R.id.Btn_register_coupon);
        Btn_register_coupon.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        get_my_point_from_server();
    }

    @Override
    public void onClick(View v) {
        if (v == Btn_register_coupon) {
            Dialog_CouponCode.showDialog(this);
        }
    }

    public void CouponCodeDialog_Callback(String value){


        try {
            String text = URLEncoder.encode(value, "utf-8");
            promo_code_register(text);
        } catch (Exception e) {

        }


    }

    private void promo_code_register(String code) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.plating.co.kr//promo/register?" +
                "user_idx="+ SVUtil.getUserIdx(getApplicationContext()) +
                "&code="+code;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jo = new JSONObject(response);
                            Boolean isValidCode = jo.getBoolean("isValidCode");
                            String title = jo.getString("title");
                            String message = jo.getString("message");

                            if (isValidCode) {
                                String btn1_text = jo.getString("btn1_text");
                                //String btn2_text = jo.getString("btn2_text");




                                new MaterialDialog.Builder(mContext)
                                        .title(title)
                                        .content(message)
                                        .positiveText(btn1_text)
                                        //e.negativeText(btn2_text)
                                        .positiveColorRes(R.color.colorPrimary)
                                        .neutralColorRes(R.color.colorPrimary)
                                        .negativeColorRes(R.color.colorPrimary)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                                get_my_point_from_server();
                                            }
                                        })
                                        .show();

                            } else {
                                SVUtil.showDialog2(mContext, title, message);
                            }



                        } catch(Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }

    private void get_my_point_from_server() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://api.plating.co.kr/promo/point?user_idx="+ SVUtil.getUserIdx(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jo = new JSONObject(response);
                            String point_to_text = jo.getString("point_to_text");

                            TV_point.setText(point_to_text);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);



    }


}
