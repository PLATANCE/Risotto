package com.plating.application;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plating.pages.ab_tutorial.TutorialActivity;

/**
 * Created by JudePark on 16. 8. 16..
 */
public class PlatingFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();

    public void sendLogEventToFirebase(
            @Nullable String itemName,
            @Nullable String value) {
        Bundle args = new Bundle();
        args.putString(FirebaseAnalytics.Param.ITEM_NAME, itemName);
        args.putString(FirebaseAnalytics.Param.VALUE, value);
        ((TutorialActivity) getActivity()).mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, args);
        Log.d(TAG, "sendLogEventToFirebase");
    }

}
