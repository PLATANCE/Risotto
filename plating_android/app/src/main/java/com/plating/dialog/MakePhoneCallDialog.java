package com.plating.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;
import com.plating.R;
import com.plating.sdk_tools.mix_panel.MixPanel;

/**
 * Created by cheehoonha on 7/18/15.
 */
public class MakePhoneCallDialog {
    public static void showDialogForPhoneCall(final Context context, final String phoneNumber) {
        new MaterialDialog.Builder(context)
                .title("전화로 주문하기")
                .content("플레이팅으로 전화 하시겠습니까?")
                .positiveText("전화하기")
                .negativeText("취소")
                .positiveColorRes(R.color.colorPrimary)
                .neutralColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        MixPanel.mixPanel_trackWithOutProperties("Call Plating");
                        Intent intent = new Intent(Intent.ACTION_CALL);

                        intent.setData(Uri.parse("tel:" + phoneNumber.replaceAll("-", "")));
                        context.startActivity(intent);
                    }
                })
                .show();
    }
}
