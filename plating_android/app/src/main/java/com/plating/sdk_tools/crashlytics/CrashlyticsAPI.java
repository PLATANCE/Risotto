package com.plating.sdk_tools.crashlytics;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.BuildConfig;
import com.crashlytics.android.core.CrashlyticsCore;
import com.plating.application.Constant;
import com.plating.application.MyApplication;

import io.fabric.sdk.android.Fabric;

/**
 * Created by cheehoonha on 10/13/15.
 */
public class CrashlyticsAPI {
    public static String LOG_TAG = Constant.APP_NAME + "CrashlyticsAPI";

    public static void disableCrashlyticsForDevelopmentMode() {
        Log.d(LOG_TAG, "disableCrashlyticsForDevelopmentMode");
        // Set up Crashlytics, disabled for debug builds
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
        Fabric.with(MyApplication.getAppContext(), new Crashlytics.Builder().core(core).build());
    }
}
