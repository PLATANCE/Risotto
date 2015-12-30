package com.plating.pages.n_navigation_drawer;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plating.R;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.object.NavigationDrawerMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class LeftNavigationDrawerFragment extends android.app.Fragment {

    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationDrawerListAdapter adapter;

    public LeftNavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.n_left_navigation_drawer_fragment, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.left_navigation_drawer_list);
        adapter = new NavigationDrawerListAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public static List<NavigationDrawerMenu> getData() {
        List<NavigationDrawerMenu> data = new ArrayList<>();
        String[] titles = {"친구 초대", "프로모션 코드 등록", "내 쿠폰함", "주문 내역", "고객 지원", "리뷰 써주기", "Plating 이란?"};
        String[] moreInfos = {"1만원+1만원 무료쿠폰", "", "", "", "", "", ""};

        int[] icons = {
                R.drawable.nav_icon_share_gray_100,
                R.drawable.nav_icon_promotion_gray_100,
                R.drawable.nav_icon_coupon_gray_100,
                R.drawable.nav_icon_receipt_gray_100,
                R.drawable.nav_icon_headset_gray_100,
                R.drawable.nav_icon_pencil_gray_100,
                R.drawable.nav_icon_restaurant_gray_100};

        for(int i = 0; i < titles.length; i++) {
            NavigationDrawerMenu menu = new NavigationDrawerMenu();
            menu.iconId = icons[i];
            menu.title = titles[i];
            menu.moreInfo = moreInfos[i];
            data.add(menu);
        }

        return data;
    }

    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
        // Draw drawer layout
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                MixPanel.mixPanel_trackWithOutProperties("Open Navigation Drawer");
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                MixPanel.mixPanel_trackWithOutProperties("Close Navigation Drawer");
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
