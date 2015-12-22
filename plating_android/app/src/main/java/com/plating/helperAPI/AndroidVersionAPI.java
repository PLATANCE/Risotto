package com.plating.helperAPI;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.plating.application.MyApplication;

/**
 * Created by cheehoonha on 10/9/15.
 */
public class AndroidVersionAPI {
    public static int getVersionCode() {
        Context context = MyApplication.getAppContext();
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("AndroidVersionAPI", "Error: " + e.toString());
        }

        int versionNumber = packageInfo.versionCode;

        return versionNumber;
    }

    public static String getVersionName() {
        Context context = MyApplication.getAppContext();
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("AndroidVersionAPI", "Error: " + e.toString());
        }

        String versionName = packageInfo.versionName;

        return versionName;
    }
}
