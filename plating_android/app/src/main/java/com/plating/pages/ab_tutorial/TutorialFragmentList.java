package com.plating.pages.ab_tutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plating.R;
import com.plating.sdk_tools.circleindicator.CircleIndicator;

/**
 * Created by home on 15. 12. 29..
 */
public class TutorialFragmentList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ab_tutorial_fragment, container, false);
    }
/*
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewPager);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewpager.setAdapter(new TutorialPagerAdapter(this.getContext()));
        indicator.setViewPager(viewpager);
    }
    */
}
