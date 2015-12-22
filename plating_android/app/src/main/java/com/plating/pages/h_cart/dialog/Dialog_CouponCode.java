package com.plating.pages.h_cart.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.plating.R;
import com.plating.pages.q_promotion.RegisterPromotionActivity;

/**
 * Created by redjjol on 18/11/2015.
 */
public class Dialog_CouponCode {
//    public static String LOG_TAG = "PlatingActivity.EnterPhoneNumberDialog";

    public static void showDialog(final Context context) {
        new MaterialDialog.Builder(context)
                .title("쿠폰코드")
                .content("등록하신 쿠폰은 결제할 때 사용됩니다.")
                .positiveColorRes(R.color.colorPrimary)
                .neutralColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.colorPrimary)


                //.inputRangeRes(9, 11, R.color.orange_300)
//                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
//                        MixPanel.mixPanel_trackWithOutProperties("Add Phone Number");
                        //String value = PhoneNumberAPI.convertToCorrectPhoneNumberFormat(input.toString());
                        String value = input.toString();
                        ((RegisterPromotionActivity) context).CouponCodeDialog_Callback(value);
                    }
                }).show();
    }
}
