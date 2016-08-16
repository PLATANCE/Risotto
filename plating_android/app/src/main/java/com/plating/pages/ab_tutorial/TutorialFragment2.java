package com.plating.pages.ab_tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plating.R;
import com.plating.application.PlatingFragment;
import com.plating.sdk_tools.mix_panel.MixPanel;

/**
 * Created by home on 15. 12. 29..
 */
public class TutorialFragment2 extends PlatingFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sendLogEventToFirebase("Tutorial", "Tutorial 2");
        return inflater.inflate(R.layout.ab_tutorial_fragment_2, container, false);
    }

    public static final TutorialFragment2 newInstance() {
        TutorialFragment2 fragment2 = new TutorialFragment2();
//        MixPanel.mixPanel_trackWithOutProperties("(SCREEN) Tutorial 2");
        Bundle args = new Bundle();
        args.putInt("num", 2);
        fragment2.setArguments(args);
        return fragment2;
    }
}
