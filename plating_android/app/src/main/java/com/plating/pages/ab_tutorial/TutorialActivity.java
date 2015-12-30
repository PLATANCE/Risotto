package com.plating.pages.ab_tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.sdk_tools.circleindicator.CircleIndicator;

/**
 * Created by home on 15. 12. 28..
 */
public class TutorialActivity extends PlatingActivity {
    TutorialPagerAdapter pagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ab_tutorial_activity);


        pagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
    }
}
