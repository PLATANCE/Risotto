package com.plating.helperAPI;

import android.widget.Toast;

import com.plating.application.MyApplication;

/**
 * Created by cheehoonha on 10/13/15.
 */
public class ToastAPI {
    public static void showToast(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }
}
