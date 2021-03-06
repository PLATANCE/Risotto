package com.plating.pages.c_daily_menu_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.dialog.StartEventDialog;
import com.plating.helperAPI.CalendarAPI;
import com.plating.helperAPI.DialogAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.network.c_daily_menu_list.GetDialogFromServer;
import com.plating.network.c_daily_menu_list.IsReviewAvailable;
import com.plating.network.c_daily_menu_list.getMenuListFromServer;
import com.plating.network.i_set_location.GetAddressInUseFromServer;
import com.plating.object.AddressListRow;
import com.plating.object.DailyMenu;
import com.plating.object_singleton.Cart;
import com.plating.pages.d_menu_detail.MenuDetailActivity;
import com.plating.pages.i_set_location.AddressListActivity;
import com.plating.pages.i_set_location.SetLocationActivity;
import com.plating.pages.n_navigation_drawer.LeftNavigationDrawerFragment;
import com.plating.pages.p_write_review_activity.WriteReviewListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.util.PreCachingLayoutManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class DailyMenuListActivity extends PlatingActivity implements Spinner.OnItemSelectedListener {
    // Left navigation drawer
    private DrawerLayout mDrawerLayout;
    private LeftNavigationDrawerFragment mLeftNavigationDrawerFragment;
    private LinearLayout mOpenFutureDateSelectionLinearLayout;
    private LinearLayout mFutureDateListLayout;
    private TextView mTotalNumberOfItemsInCart;
    private RecyclerView mRecyclerView;
    private DailyMenuListAdapter mAdapter;
    private GoogleApiClient mGoogleApiClient;
    private int mSort;
    final static int SORT_NOTHING = 0;
    final static int SORT_POPULAR = 1;
    final static int SORT_PRICE_LOW = 2;

    // Arraylist for keeping daily menu. When a user press on "장바구니" button, this list gets used.
    private ArrayList<DailyMenu> mDailyMenuArrayList;

    private StartEventDialog startEventDialog;

    // SharedPreference to manage dialog status
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;

    AddressListRow addressListRow;
    TextView text_my_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_daily_menu_list_activity);

        // Dropdown sort menu
        Spinner spinner = (Spinner) findViewById(R.id.spinner_sort);
        spinner.setOnItemSelectedListener(this);
        ArrayList<String> sortItem = new ArrayList<String>();
        sortItem.add("정렬");
        sortItem.add("인기도 높은 순");
        sortItem.add("가격 낮은 순");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortItem);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        setUpLeftNavigationDrawer();

        getAllViews();

        setUpRecyclerView();

        setOnClickListeners();

        setFutureMenuDateLayout();

        //getMenuListFromServer();

        getAddressInUseFromServer();

        showOrderConfirmedMarkLayoutIfOrderIsPlaced();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();

        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                    @Override
                    public void onResult(@NonNull AppInviteInvitationResult appInviteInvitationResult) {
                        if (appInviteInvitationResult.getStatus().isSuccess()) {
                            Intent intent = appInviteInvitationResult.getInvitationIntent();
                            String deepLink = AppInviteReferral.getDeepLink(intent);
                        } else {
                            Log.d("DailyMenuListActivity", "getInvitation: no deep link found.");
                        }
                    }
                });

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        mSort = position;
        if(mSort != SORT_NOTHING) {
            if(mSort == SORT_POPULAR) {
                Collections.sort(mAdapter.data, new Comparator<DailyMenu>() {
                    public int compare(DailyMenu c1, DailyMenu c2) {
                        if (c1.numOfReviews > c2.numOfReviews) return -1;
                        if (c1.numOfReviews < c2.numOfReviews) return 1;
                        return 0;
                    }
                });
            }
            else if(mSort == SORT_PRICE_LOW) {
                Collections.sort(mAdapter.data, new Comparator<DailyMenu>() {
                    public int compare(DailyMenu c1, DailyMenu c2) {
                        if (c1.price < c2.price) return -1;
                        if (c1.price > c2.price) return 1;
                        return 0;
                    }
                });
            }
            mAdapter.updateData(mAdapter.data);
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void getAddressInUseFromServer() {
        GetAddressInUseFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getAddressInUseFromServer_Callback(AddressListRow addressListRow) {
        this.addressListRow = addressListRow;
        Log.d(LOG_TAG, "PlatingActivity.getMenuListFromServer" + addressListRow.getAddress() + addressListRow.toString());
        if (addressListRow.getAddress() == null) {
            text_my_address.setText("먼저, 배달 가능 지역을 확인해주세요 :)");
            getMenuListFromServer();
        } else {
            text_my_address.setText(addressListRow.getAddress() + " " + addressListRow.getAddress_detail());
            getMenuListFromServer(addressListRow.getArea());
        }
    }


    public void moveToAddressListActivity(View v) {
        MixPanel.mixPanel_trackWithOutProperties("Edit Address");
        Intent intent;
        if (addressListRow.getAddress() == null) {
            intent = new Intent(mContext, SetLocationActivity.class);
        } else {
            intent = new Intent(mContext, AddressListActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.transition_slide_in_from_bottom, R.anim.transition_slide_out_to_top);
    }


    public void setUpLeftNavigationDrawer() {
        // Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        // Left navigation drawer setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftNavigationDrawerFragment = (LeftNavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer_left);
        mLeftNavigationDrawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    public void openNavigationDrawerIfUserHasJustPutAnOrder() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void getAllViews() {
        // Tool bar
        mOpenFutureDateSelectionLinearLayout = (LinearLayout) findViewById(R.id.future_date_list_layout);
        mFutureDateListLayout = (LinearLayout) findViewById(R.id.future_date_list_layout);
        mTotalNumberOfItemsInCart = (TextView) findViewById(R.id.total_number_of_item_in_cart);

        text_my_address = (TextView) findViewById(R.id.text_my_address);

        // Recycler view for list of restaurants
        mRecyclerView = (RecyclerView) findViewById(R.id.menu_list_recycler_view);
        mRecyclerView.setLayoutManager(new PreCachingLayoutManager(this));
    }

    public void setOnClickListeners() {
        mOpenFutureDateSelectionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFutureDateListLayout.getVisibility() == View.INVISIBLE) {
                    mFutureDateListLayout.setVisibility(View.VISIBLE);
                } else if (mFutureDateListLayout.getVisibility() == View.VISIBLE) {
                    mFutureDateListLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void setFutureMenuDateLayout() {
        int numberOfFutureDates = 4;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int date = calendar.get(Calendar.DATE);

        for (int i = 0; i < numberOfFutureDates; i++) {
            View child = getLayoutInflater().inflate(R.layout.d_daily_menu_list_future_menu, null);
            TextView futureMenuDay = (TextView) child.findViewById(R.id.future_menu_day);
            TextView futureMenuDate = (TextView) child.findViewById(R.id.future_menu_date);
            ImageView futureMenuSelectedCircleImageview = (ImageView) child.findViewById(R.id.future_menu_selected_circle_imageview);

            // Set data, comment, and user id
            futureMenuDay.setText(CalendarAPI.convertIntDayToStringDay(day + i));
            futureMenuDate.setText(date + "");

            if (i == 0) {
                futureMenuSelectedCircleImageview.setVisibility(View.VISIBLE);
                futureMenuDate.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            // add the view to the linear layout
            mFutureDateListLayout.addView(child);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        showWriteReviewActivityIfAvailable();
        getAddressInUseFromServer();

        // Do this, so that it shows correct number of items in cart for each item.
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void moveToBannerActivity() {
        Intent intent = new Intent(this, MainBannerActivity.class);
        startActivity(intent);
    }


    /***********************
     * * NETWORK OPERATION *
     ***********************/
    public void getMenuListFromServer() {
        getMenuListFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getMenuListFromServer(String area) {
        getMenuListFromServer.getDataFromServer(this, mRequestQueue, area);
    }


    public void getMenuListFromServer_Callback(ArrayList<DailyMenu> dailyMenuArrayList) {
        mDailyMenuArrayList = dailyMenuArrayList;
        mAdapter.updateData(dailyMenuArrayList);
        mAdapter.notifyDataSetChanged();

        getDialogFromServer();
    }

    public void getDialogFromServer() {
        GetDialogFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getDialogFromServer_Callback(Boolean usedCoupon, String image_url_dialog, int redirect) {
        // if coupon is not used, dialog show!
        if (usedCoupon) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(new Date());
            String time = sharedPreferences.getString("time", "");
            Log.d(LOG_TAG, "date :" + date + " time : " + time);
            if (!time.equals(date)) {
                startEventDialog = new StartEventDialog(this, RequestURL.DIALOG_IMAGE_URL + image_url_dialog, redirect, editor);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MixPanel.mixPanel_trackWithOutProperties("Show Event Dialog");
                        startEventDialog.show();
                    }
                }, 600);
            }
        }
    }


    public void setUpRecyclerView() {
        // Set recyclerview, which is the listview in this case
        mAdapter = new DailyMenuListAdapter(this, new ArrayList<DailyMenu>());
        mRecyclerView.setAdapter(mAdapter);

        // GitHub: thehighestend
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void startMenuActivity(int menuId, int menuDIdx, int stock) {
        Intent intent = new Intent(mContext, MenuDetailActivity.class);
        intent.putExtra(Constant.DAILY_MENU_ACTIVITY__MENU_IDX, menuId);
        intent.putExtra(Constant.DAILY_MENU_ACTIVITY__MENU_D_IDX, menuDIdx);
        intent.putExtra(Constant.DAILY_MENU_ACTIVITY__STOCK, stock);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
    }

    /*********************
     * * BUTTONS *
     *********************/
    public void putMenuToCart(int position, int count) {
        DailyMenu dailyMenu = mDailyMenuArrayList.get(position);
        Cart.getCartInstance().addMenuToCart(dailyMenu.menu_d_idx, dailyMenu.idx, count, dailyMenu.price, dailyMenu.altPrice, dailyMenu.imageUrlMenu, dailyMenu.nameMenu);
    }

    /*********************
     * * SYSTEM OPERATION *
     *********************/
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
            Cart.getCartInstance().emptyCart();
        }
    }

    // Open Left Navigation Drawer
    public void onClickShowLeftNavigationDrawer(View view) {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }


    // Briefly show "Item Added" layout. Show for about 1 second.
    public void showOrderConfirmedMarkLayoutIfOrderIsPlaced() {
        Log.d(LOG_TAG, "showItemAddedCheckMarkLayout");

        // If order is placed, show check mark.
        // If not, don't show anything
        Intent intent = getIntent();
        boolean orderPlaced = intent.getBooleanExtra(Constant.ORDER_PLACED, false);
        if (!orderPlaced)
            return;

        final LinearLayout orderConfirmedMarkLayout = (LinearLayout) findViewById(R.id.order_confirmed_mark);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                orderConfirmedMarkLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(500);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                orderConfirmedMarkLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(3500);
        fadeOut.setDuration(500);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        orderConfirmedMarkLayout.startAnimation(animation);
    }


    /****************/
    /*** NETWORK ****/
    /****************/
    public void showWriteReviewActivityIfAvailable() {
        // Give this a delay because there can be
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                IsReviewAvailable.getDataFromServer(mContext, mRequestQueue);
            }
        }, 400);
    }

    public void showWriteReviewActivityIfAvailable_callback(boolean available, int orderIdx) {
        Log.d(LOG_TAG, "Available: " + available + " orderIdx: " + orderIdx);
        if (available && mContext != null) {
            Intent intent = new Intent(mContext, WriteReviewListActivity.class);
            intent.putExtra(Constant.ORDER_HISTORY_ACTIVITY__ORDER_HISTORY_ID, orderIdx);
            startActivity(intent);
            overridePendingTransition(R.anim.transition_slide_in_from_bottom, R.anim.transition_nothing);
//            overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
        }
    }


}
