package com.plating.pages.a_splash.dialog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.plating.pages.a_splash.SplashActivity;

/**
 * Created by cheehoonha on 7/18/15.
 */
public class UpdateDialog {
    public static void showDialogForUpdate(final Context context) {
        new MaterialDialog.Builder(context)
                .title("플레이팅 업데이트")
                .content("플레이팅이 더 좋아집니다!")
                .positiveText("업데이트 하기")
                .negativeText("취소")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        ((SplashActivity) context).finish();   // Quit app
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        super.onNeutral(dialog);
                        ((SplashActivity) context).finish();   // Quit app
                    }
                })
                .show();
    }
}
