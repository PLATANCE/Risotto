package com.plating.pages.a_splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.gcm.RegistrationIntentService;
import com.plating.helperAPI.AndroidVersionAPI;
import com.plating.object.AnalyticsEvent;
import com.plating.pages.a_splash.dialog.UpdateDialog;
import com.plating.network.a_splash.CheckForAppUpdate;
import com.plating.pages.ab_tutorial.TutorialActivity;
import com.plating.pages.b_login.LogInActivity;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.util.SVUtil;

public class SplashActivity extends PlatingActivity {

    // Duration of splash screen
    private final int SPLASH_DISPLAY_LENGTH = 800;

    // Google play service constant
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private AnalyticsEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifeCycle", "SplashActivity onCreate");
        setContentView(R.layout.a_welcome_activity);

        MixPanel.mixPanel_orderPeopleListByLatestUsage();
        registerGCM();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get current GPS location of the user
        updateGpsLocationOnce();

        // Check for any app updates.
        // If there is no update, start the next activity
        checkForAppUpdateAndStartNextActivityIfThereIsNoUpdateAvailable();

        Bundle params = new Bundle();
        params.putString("activity", "SplashActivity");

        event = new AnalyticsEvent("splash", params);
        sendLogEventToFirebase(event);

    }

    // CHECK FOR UPDATE
    public void checkForAppUpdateAndStartNextActivityIfThereIsNoUpdateAvailable() {
        Log.d(LOG_TAG, "Android Version Code: " + AndroidVersionAPI.getVersionCode());
        int androidVersionCode = AndroidVersionAPI.getVersionCode();
        CheckForAppUpdate.getDataFromServer(this, mRequestQueue, androidVersionCode);
    }

    public void checkForAppUpdate_ResultSuccessCallback(boolean updateExists) {
        Log.d(LOG_TAG, "Update Exists: " + updateExists);
        if (updateExists) {
            UpdateDialog.showDialogForUpdate(this);
        } else {
            // Start Next Activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNextActivity();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    // START NEXT ACTIVITY
    public void startNextActivity() {
        // If user exits app by pressing the back button while loading, do not continue
        if(mContext == null)
            return;

        // if user is logged in, start dailyMenuList activity
        // Otherwise, start Sign up activity
        if (SVUtil.GetUserIdx(mContext) == 0) {
            /*
             * Tutorial Activity Start
             */
//            showTutorial();
//            임시적으로 지리산과 아이들을 보이지 않도록 한다
//            TODO: 추후에 튜토리얼을 바꿔야한다.
            ShowLoginPage();
        } else {
            ShowDailyMenuList();
        }
    }

    private void showTutorial() {
        Intent intent = new Intent(mContext, TutorialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);
    }

    private void ShowLoginPage() {
        Intent intent = new Intent(mContext, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom);
    }

    public void ShowDailyMenuList() {
        Intent intent = new Intent(this, DailyMenuListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    /**********
     * * GCM  **
     **********/
    public void registerGCM() {
        if (SVUtil.GetGcmtoken(mContext).equals("")) {
            getInstanceIdToken();
        }
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    /********************
     * * PLAY SERVICE  **
     ********************/
    // Check is play service is available
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
