package com.plating.pages.i_set_location.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.plating.R;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.pages.i_set_location.SetLocationActivityVer2;
import com.plating.util.SVUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheehoonha on 10/5/15.
 */
public class SetLocationDialog {
    public static String LOG_TAG = "PlatingActivity.SetLocationDialog";
    private static AlertDialog mOrderConfirmDialog;
    private static EditText mSetLocationEditText;
    private static TextView mAreaNameTextView;
    private static Context mContext;

    public static void showDialog(final Context context, String areaName) {
        mContext = context;
        // Set Dialog
        mOrderConfirmDialog = new AlertDialog.Builder(context)
                .setView(((Activity)context).getLayoutInflater().inflate(R.layout.z_dialog_set_location_activity, null))
                        .create();

        mOrderConfirmDialog.show();

        setTitleNameWithArea(areaName);
        setEditText(context);
        setButtonClickListeners();
    }

    public static void setTitleNameWithArea(String areaName) {
        // Set Area TextView
        mAreaNameTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.area_name_text_view);
        mAreaNameTextView.setText("서울특별시 강남구 " + areaName);
    }

    public static void setEditText(Context context) {
        // Set EditText
        mSetLocationEditText = (EditText) mOrderConfirmDialog.findViewById(R.id.location_edit_text);

        // Open EditText Automatically
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

/*
        mSetLocationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mOrderConfirmDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
*/

        mSetLocationEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    saveAndSendAddressToServer();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public static void setButtonClickListeners() {
        // Set cancel button
        Button cancelButton = (Button) mOrderConfirmDialog.findViewById(R.id.dialog_set_location_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrderConfirmDialog.dismiss();
            }
        });

        // Set confirm button
        Button confirmButton = (Button) mOrderConfirmDialog.findViewById(R.id.dialog_set_location_confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndSendAddressToServer();
                mOrderConfirmDialog.dismiss();
            }
        });
    }

    public static void saveAndSendAddressToServer() {
        String area = mAreaNameTextView.getText().toString();
        String address = mSetLocationEditText.getText().toString();

        UpdateAddress(area, address, 0, 0, true);
        MixPanel.mixPanel_trackWithOutProperties("(Action) SetLocation - " + area + " " + address);
    }


    private static void UpdateAddress(final String address, final String detail, final double lat, final double lon, final boolean isAvailable) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://api.plating.co.kr/update_info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SVUtil.SetDeliveryAreaStatus(mContext, isAvailable);
//                        ((SetLocationActivity) mContext).startDailyMenuListActivity();
//                        ((SetLocationActivity) mContext).finish();
                        ((SetLocationActivityVer2) mContext).AddressUpdateFinished();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_idx", SVUtil.getUserIdx(mContext)+"");
                params.put("address", address);
                params.put("address_detail", detail);
                params.put("delivery_available", isAvailable? "1":"0");
                params.put("lat", lat+"");
                params.put("lon", lon+"");


                return params;
            }
        };
        queue.add(myReq);
    }
}
