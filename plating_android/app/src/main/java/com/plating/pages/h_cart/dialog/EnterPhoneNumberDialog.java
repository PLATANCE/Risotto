package com.plating.pages.h_cart.dialog;

import android.content.Context;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;
import com.plating.R;
import com.plating.helperAPI.PhoneNumberAPI;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.pages.h_cart.CartActivity;

/**
 * Created by cheehoonha on 10/5/15.
 */
public class EnterPhoneNumberDialog {
    public static String LOG_TAG = "PlatingActivity.EnterPhoneNumberDialog";

    public static void showDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title("전화번호")
                .content("연락받을 수 있는 번호를 입력해주세요")
                .inputRangeRes(9, 11, R.color.orange_300)
                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED)
                .input("9자리 이상. 11자리 이하.", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        MixPanel.mixPanel_trackWithOutProperties("Add Phone Number");
                        String value = PhoneNumberAPI.convertToCorrectPhoneNumberFormat(input.toString());
                        ((CartActivity)context).EnterPhoneNumberDialog_Callback(value);
                    }
                }).show();
    }
}
