package com.plating.pages.ab_tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by home on 15. 12. 29..
 */
public class TutorialPagerAdapter extends FragmentStatePagerAdapter {

    public TutorialPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0 : return TutorialFragment1.newInstance();
            case 1 : return TutorialFragment2.newInstance();
            case 2 : return TutorialFragment3.newInstance();
            default : return null;
        }
    }
}
