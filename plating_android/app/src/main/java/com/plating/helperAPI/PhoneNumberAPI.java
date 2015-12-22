package com.plating.helperAPI;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.plating.application.Constant;
import com.plating.application.MyApplication;

/**
 * Created by cheehoonha on 10/6/15.
 */
public class PhoneNumberAPI {
    public static String LOG_TAG = Constant.APP_NAME + "PhoneNumberAPI";

    public static String getPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) MyApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();
        Log.d(LOG_TAG, "getPhoneNumber: Phone Number = " + phoneNumber);

        if(phoneNumber == null || phoneNumber == "") {
            phoneNumber = "전화번호를 입력해주세요.";
        }

        return convertToCorrectPhoneNumberFormat(phoneNumber);
    }

    // Different carriers have different phone number format
    public static String convertToCorrectPhoneNumberFormat(String phoneNumber) {
        if(phoneNumber.length() < 11) {
            return phoneNumber;
        } else if(phoneNumber.length() == 11) { // SKT
            phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(phoneNumber.length() - 8, phoneNumber.length() - 4) + "-" + phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
        } else if(phoneNumber.substring(0,3).equals("+82")) { // KT
            phoneNumber = phoneNumber.replace("+82", "0");
            phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(phoneNumber.length() - 8, phoneNumber.length() - 4) + "-" + phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length());
        }

        return phoneNumber;
    }
}
