package com.plating.pages.ab_tutorial;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.sdk_tools.circleindicator.CircleIndicator;

/**
 * Created by home on 15. 12. 28..
 */
public class TutorialActivity extends PlatingActivity {
    TutorialPagerAdapter pagerAdapter;
    ViewPager viewPager;
    CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ab_tutorial_activity);


        pagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);
    }
}
