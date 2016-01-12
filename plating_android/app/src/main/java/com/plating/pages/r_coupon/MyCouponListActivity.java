package com.plating.pages.r_coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.r_coupon.GetCouponAvailableFromServer;
import com.plating.network.r_coupon.GetMyCouponListFromServer;
import com.plating.object.CouponListRow;
import com.plating.object_singleton.Cart;
import com.plating.util.PreCachingLayoutManager;

import java.util.ArrayList;

/**
 * Created by Rooney on 15. 12. 11..
 */
public class MyCouponListActivity extends PlatingActivity {
    private RecyclerView mRecyclerView;
    private MyCouponListAdapter mAdapter;
    private TextView coupon_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_coupon_list_activity);

        getAllViews();

        setUpRecyclerView();

        getCouponListFromServer();

    }

    public void getAllViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.coupon_list_recycler_view);
        mRecyclerView.setLayoutManager(new PreCachingLayoutManager(this));

        coupon_count = (TextView) findViewById(R.id.coupon_count);

    }

    public void setUpRecyclerView() {
        mAdapter = new MyCouponListAdapter(this, new ArrayList<CouponListRow>());
        mRecyclerView.setAdapter(mAdapter);
    }


    public void getCouponListFromServer() {
        GetMyCouponListFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getCouponListFromServer_Callback(ArrayList<CouponListRow> couponListRowArrayList) {
        Log.d(LOG_TAG, "getCouponListFromServer_Callback: size = " + couponListRowArrayList.size());
        mAdapter.updateData(couponListRowArrayList);
        mAdapter.notifyDataSetChanged();
        if(couponListRowArrayList.size() > 0) {
            coupon_count.setText("사용 가능 쿠폰 : " + couponListRowArrayList.size() + "개");
        } else {
            coupon_count.setText("보유한 쿠폰이 없습니다.");
        }

    }

    public void sendDataToServer(int coupon_idx) {
        GetCouponAvailableFromServer.sendDataToServer(this, mRequestQueue, coupon_idx);
    }

    public void sendDataToServer_callback(int user_idx, int coupon_idx, boolean available, int sale_price, String msg) {
        Log.d(LOG_TAG, "user_idx = " + user_idx + " coupon_idx = " + coupon_idx + " available = " + available + " sale_price = " + sale_price + " msg = " + msg);
        // if coupon is not available, show toast msg else setResult coupon_idx, user_idx, sale_price, and show toast msg
        if(available) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putInt("coupon_price", sale_price);
            bundle.putInt("coupon_idx", coupon_idx);
            bundle.putString("msg", msg);

            intent.putExtras(bundle);

            setResult(RESULT_OK, intent);
            finish();
        } else {
            ToastAPI.showToast(msg);
        }
    }
}
