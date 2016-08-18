package com.plating.pages.ab_tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plating.R;

/**
 * Created by home on 15. 12. 29..
 */
public class TutorialFragment1 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ab_tutorial_fragment_1, container, false);
    }

    public static final TutorialFragment1 newInstance() {
        TutorialFragment1 fragment1 = new TutorialFragment1();
//        MixPanel.mixPanel_trackWithOutProperties("(SCREEN) Tutorial 1");
        Bundle args = new Bundle();
        args.putInt("num", 1);
        fragment1.setArguments(args);

        return fragment1;
    }


}


  