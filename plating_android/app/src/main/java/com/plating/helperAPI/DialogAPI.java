package com.plating.helperAPI;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.plating.R;

/**
 * Created by cheehoonha on 10/13/15.
 */
public class DialogAPI {

    public static void showDialog(Context context, String title, String message, String positiveText, String negativeText) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
                .positiveText(positiveText)
                .negativeText(negativeText)
                .positiveColorRes(R.color.colorPrimary)
                .neutralColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.colorPrimary)
                .show();
    }

}
