package com.plating.pages.h_cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.DialogAPI;
import com.plating.network.h_cart.SendMobileNumberToServer;

/**
 * Created by Rooney on 16. 6. 9..
 */
public class InputMobileForAuthActivity extends PlatingActivity implements View.OnClickListener{
    private LinearLayout invalidCheckBtn;
    private EditText inputMobileEditText;
    private boolean isBlocking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_input_mobile_for_auth);
        invalidCheckBtn = (LinearLayout)findViewById(R.id.invalid_check_btn);
        inputMobileEditText = (EditText)findViewById(R.id.input_mobile_edit_text_view);
        invalidCheckBtn.setOnClickListener(this);
        inputMobileEditText.requestFocus();
    }

    public void sendMobileNumberToServer() {
        if(isBlocking) {
            return;
        }
        isBlocking = true;
        Log.d("InputMobileActivity", inputMobileEditText.getText().toString());
        String mobileNumber = inputMobileEditText.getText().toString();
        SendMobileNumberToServer.sendDataToServer(mContext, mRequestQueue, mobileNumber);
    }

    public void sendMobileNumberToServer_callback(boolean isSent, boolean isServerError, String message) {
        isBlocking = false;
        if(!isSent) {
            DialogAPI.showDialog(mContext, "전송 오류", message, "확인", null);
            inputMobileEditText.setText("");
        }
        if(isServerError) {
            DialogAPI.showDialog(mContext, "서버 오류", message, "확인", null);
            inputMobileEditText.setText("");
        }
    }

    public void sendMobileNumberToServer_callback(String phoneNumber) {
        isBlocking = false;
        Intent intent = new Intent(this, InputAuthNumberActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        if(v == invalidCheckBtn) {
            //SendMobileNumberToServer.getRequestUrl(this);
            sendMobileNumberToServer();
        }
    }
}
