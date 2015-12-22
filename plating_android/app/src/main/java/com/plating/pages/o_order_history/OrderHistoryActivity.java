package com.plating.pages.o_order_history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.PriceAPI;
import com.plating.object.OrderDetail;
import com.plating.object.OrderHistory;
import com.plating.network.o_order_history.GetOrderHistoryDetailFromServer;
import com.plating.pages.p_write_review_activity.WriteReviewListActivity;

import java.util.ArrayList;

public class OrderHistoryActivity extends PlatingActivity {
    private int mOrderIdx;

    private TextView mOrderStatus;
    private LinearLayout mOrderListLayout;
    private TextView mTotalPrice;
    private TextView mDeliveryTime;
    private RelativeLayout mPleaseWriteReviewLayout;
    private ImageView mPleaseWriteReviewStar;
    private TextView mPleaseWriteReviewTextView;
    private TextView mPleaseWriteReviewSubTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_a_order_history_activity);

        Intent intent = getIntent();
        mOrderIdx = intent.getIntExtra(Constant.ORDER_HISTORY_LIST_ACTIVITY__ORDER_HISTORY_ID, -1);

        getAllViews();
        setButtonClickListener();

        getOrderHistoryFromServer();
    }

    public void getAllViews() {
        mOrderStatus = (TextView) findViewById(R.id.order_status);
        mOrderListLayout = (LinearLayout) findViewById(R.id.order_history_order_list_layout);
        mTotalPrice = (TextView) findViewById(R.id.total_price);
        mDeliveryTime = (TextView) findViewById(R.id.delivery_time);

        mPleaseWriteReviewLayout = (RelativeLayout) findViewById(R.id.order_history_please_write_review_layout);
        mPleaseWriteReviewStar = (ImageView) findViewById(R.id.please_write_review_star);
        mPleaseWriteReviewTextView = (TextView) findViewById(R.id.please_write_review_text);
        mPleaseWriteReviewSubTextView = (TextView) findViewById(R.id.please_write_review_sub_text);
    }

    public void setButtonClickListener() {
        mPleaseWriteReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WriteReviewListActivity.class);
                intent.putExtra(Constant.ORDER_HISTORY_ACTIVITY__ORDER_HISTORY_ID, mOrderIdx);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
            }
        });
    }

    public void getOrderHistoryFromServer() {
        GetOrderHistoryDetailFromServer.getDataFromServer(this, mRequestQueue, mOrderIdx);
    }

    public void getOrderHistoryFromServer_callback(OrderHistory orderHistory) {
        // Fill in general information area
        mOrderStatus.setText(orderHistory.orderStatusDescription);
        mTotalPrice.setText(PriceAPI.intPriceToStringPriceWonSymbolFormat(orderHistory.totalPrice));
        mDeliveryTime.setText(orderHistory.deliveryTime);

        addOrderItemsToLayout(orderHistory.orderDetailArrayList);

        // Fill in review area
        if(orderHistory.reviewCompleted == 0) {
            mPleaseWriteReviewStar.setImageResource(R.drawable.review_start_unfilled);
            mPleaseWriteReviewTextView.setText("리뷰를 남겨주세요 :)");
            mPleaseWriteReviewSubTextView.setText("베스트 리뷰 작성자에게는 5인분 무료 시식권을!");
        } else if(orderHistory.reviewCompleted == 1) {
            mPleaseWriteReviewStar.setImageResource(R.drawable.icon_star_filled_yellow);
            mPleaseWriteReviewTextView.setText("리뷰를 남겨주셔서 감사합니다.");
            mPleaseWriteReviewSubTextView.setText("앞으로도 더욱 좋은 음식으로 보답하겠습니다.");
        }
    }

    public void addOrderItemsToLayout(ArrayList<OrderDetail> orderDetailArrayList) {
        for(int i = 0; i < orderDetailArrayList.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.n_a_order_history_detail_row, null);
            TextView menuName = (TextView) child.findViewById(R.id.menu_name);
            TextView menuAmount = (TextView) child.findViewById(R.id.menu_amount);

            // Set data, comment, and user id
            menuName.setText(orderDetailArrayList.get(i).menuName.replace(".", "\n"));
            menuAmount.setText(orderDetailArrayList.get(i).amount + " 인분");

            // add the view to the linear layout
            mOrderListLayout.addView(child);
        }
    }
}