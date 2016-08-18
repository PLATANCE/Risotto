package com.plating.pages.ab_tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.object.AnalyticsEvent;
import com.plating.pages.b_login.LogInActivity;

/**
 * Created by home on 15. 12. 29..
 */
public class TutorialFragment3 extends Fragment implements View.OnClickListener {

    ImageButton imageView_showLogin;
    private AnalyticsEvent event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.ab_tutorial_fragment_3, container, false);

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "finished_tutorial");
        event = new AnalyticsEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, params);
        ((PlatingActivity) getActivity()).sendLogEventToFirebase(event);
        imageView_showLogin = (ImageButton) view.findViewById(R.id.imageView_showLogin);
        imageView_showLogin.setOnClickListener(this);

        return view;
    }

    public static final TutorialFragment3 newInstance() {
        TutorialFragment3 fragment3 = new TutorialFragment3();
//        MixPanel.mixPanel_trackWithOutProperties("(SCREEN) Tutorial 3");
        Bundle args = new Bundle();
        args.putInt("num", 3);
        fragment3.setArguments(args);
        return fragment3;
    }

    @Override
    public void onClick(View v) {
        if(v == imageView_showLogin) {
//            MixPanel.mixPanel_trackWithOutProperties("Click Start Plating");
//            sendLogEventToFirebase("Click Sign Up", "Click Start Plating");
            startLoginActivity();
        }
    }

    public void startLoginActivity() {
        Log.d("TutorialFragment3", getActivity().getClass().getName());

        Intent intent = new Intent(getActivity(), LogInActivity.class);
        startActivity(intent);
    }
}
