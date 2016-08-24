package com.plating.pages.h_cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.DialogAPI;
import com.plating.network.h_cart.SendAuthNumberToServer;
import com.plating.util.SVUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rooney on 16. 6. 9..
 */
public class InputAuthNumberActivity extends PlatingActivity {
    private EditText inputAuthEditText;
    private TextView mobileNumberTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_input_auth_number);

        inputAuthEditText = (EditText) findViewById(R.id.input_auth_edit_text);
        mobileNumberTextView = (TextView) findViewById(R.id.mobile_number_text);

        inputAuthEditText.requestFocus();
        getMobileNumber();

        inputAuthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int authNumberLength = s.toString().length();
                if(authNumberLength == 4) {
                    sendToAuthNumberToServer(s.toString());
                }
            }
        });
    }

    public void getMobileNumber() {
        Intent intent = getIntent();
        String message = intent.getStringExtra("phoneNumber");
        mobileNumberTextView.setText(message);
    }

    public void sendToAuthNumberToServer(String authNumber) {
        Log.d("InputAuthNumberActivity", "서버에게 인증번호를 전달하자!");
        SendAuthNumberToServer.sendDataToServer(mContext, mRequestQueue, authNumber);
    }

    public void sendToAuthNumberToServer_callback(boolean isSuccessful, String message) {
        Log.d("InputAuthNumberActivity", isSuccessful + message + "");
        if(!isSuccessful) {
            DialogAPI.showDialog(mContext, "인증 오류", message, "확인", null);
            inputAuthEditText.setText("");
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder
                    .setTitle("인증 성공")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(mContext, CartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            update_phone(mobileNumberTextView.getText().toString());
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    public void sendToAuthNumberToServer_callback(boolean isSuccessful, int switchUserIdx, String message) {
        Log.d("InputAuthNumberActivity", isSuccessful + message + "" + switchUserIdx);
        if(!isSuccessful) {
            DialogAPI.showDialog(mContext, "인증 오류", message, "확인", null);
            inputAuthEditText.setText("");
        } else {
            SVUtil.SetUserIdx(this, switchUserIdx);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder
                    .setTitle("인증 성공")
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(mContext, CartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            update_phone(mobileNumberTextView.getText().toString());
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void update_phone(final String phone_no) {
        Log.d("InputAuthNumberActivity", phone_no.toString());
        RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://api.plating.co.kr/update_info?phone_no=" + phone_no,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("update_phone", "result : " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("phone_no", phone_no + "");
                params.put("user_idx", SVUtil.getUserIdx(mContext) + "");

                return params;
            }
        };
        queue.add(myReq);
    }
}
