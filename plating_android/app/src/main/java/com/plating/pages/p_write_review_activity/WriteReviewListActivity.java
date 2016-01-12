package com.plating.pages.p_write_review_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.dialog.ReferDialog;
import com.plating.network.RequestURL;
import com.plating.network.p_write_review_activity.CancelWritingReview;
import com.plating.network.p_write_review_activity.GetReviewListFromServer;
import com.plating.network.p_write_review_activity.SubmitReview;
import com.plating.object.WriteReviewRow;
import com.plating.pages.s_refer.ReferActivity;
import com.plating.util.PreCachingLayoutManager;

import java.util.ArrayList;

/**
 * Created by redjjol on 15/09/15.
 */
public class WriteReviewListActivity extends PlatingActivity {
    private int mOrderIdx;

    private RecyclerView mRecyclerView;
    private WriteReviewListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_write_review_list_activity);

        Intent intent = getIntent();
        mOrderIdx = intent.getIntExtra(Constant.ORDER_HISTORY_ACTIVITY__ORDER_HISTORY_ID, -1);

        getAllViews();
        setUpRecyclerView();

        getReviewListFromServer();
    }

    public void getAllViews() {
        // Recycler view for list of restaurants
        mRecyclerView = (RecyclerView) findViewById(R.id.review_list_recycler_view);
        mRecyclerView.setLayoutManager(new PreCachingLayoutManager(this));
    }

    public void setUpRecyclerView() {
        mAdapter = new WriteReviewListAdapter(this, new ArrayList<WriteReviewRow>());
        mRecyclerView.setAdapter(mAdapter);
    }

    /***********************
     * * NETWORK OPERATION *
     ***********************/
    private void getReviewListFromServer() {
        GetReviewListFromServer.getDataFromServer(this, mRequestQueue, mOrderIdx);
    }

    public void getReviewListFromServer_callback(ArrayList<WriteReviewRow> writeReviewRowArrayList) {
        Log.d(LOG_TAG, writeReviewRowArrayList.toString());
        mAdapter.updateData(writeReviewRowArrayList);
        mAdapter.notifyDataSetChanged();
    }


    public void onClick_finishWritingReview(View v) {
        ArrayList<WriteReviewRow> allReviewsWrittenSoFar = mAdapter.getAllReviewsWrittenSoFar();

        String order_d_idx = "";
        String rating = "";
        String comment = "";
        for (int i = 0; i < allReviewsWrittenSoFar.size(); i++) {
            if (allReviewsWrittenSoFar.get(i).rating != 0) {
                order_d_idx += allReviewsWrittenSoFar.get(i).idx + "|";
                rating += allReviewsWrittenSoFar.get(i).rating + "|";
                Log.d(LOG_TAG, "asdfasdf : "+allReviewsWrittenSoFar.get(i).comment);
                //
                if (allReviewsWrittenSoFar.get(i).comment != null) {
                    comment += allReviewsWrittenSoFar.get(i).comment;
                }

                comment += "|";
            }
        }

        // If there was no review written, just return
        if(order_d_idx.equals(""))
            return;

        order_d_idx = order_d_idx.substring(0, order_d_idx.length() - 1);
        rating = rating.substring(0, rating.length() - 1);
        comment = comment.substring(0, comment.length() - 1);

        Log.d(LOG_TAG, "asdfasdf : " + order_d_idx + " / " + rating + " / " + comment);
        //438 / 4.0 / null

        submitReview(order_d_idx, rating, comment);
    }

    private void submitReview(final String orderDIdx, final String rating, final String comment) {
        SubmitReview.sendDataToServer(this, mRequestQueue, mOrderIdx, orderDIdx, rating, comment);
    }

    public void submitReview_callback() {
        Log.d(LOG_TAG, "SubmitReview_callback");

        // 2016.01.11 rating 이 4.0 이상이면, 친구 초대 하라는 메세지 다이얼로그!
        if(getMaxRating() >=  4.0) {
            ReferDialog dialog = new ReferDialog(this, RequestURL.DIALOG_IMAGE_URL + "refer_dialog.png");
            dialog.show();

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("");
            alert.setMessage("소중한 의견 감사합니다");
            alert.setCancelable(false);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();
                }
            });
            alert.show();
        }
    }

    public void moveToReferActivity() {
        Intent intent = new Intent(this, ReferActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_slide_in_from_left, R.anim.transition_slide_out_to_right);
    }

    public double getMaxRating() {
        double maxRating = 0.0f;
        ArrayList<WriteReviewRow> allReviewsWrittenSoFar = mAdapter.getAllReviewsWrittenSoFar();
        for(int i = 0;  i < allReviewsWrittenSoFar.size(); i++) {
            if(allReviewsWrittenSoFar.get(i).rating > maxRating) {
                maxRating = allReviewsWrittenSoFar.get(i).rating;
            }
        }
        return maxRating;
    }

    @Override
    public void onClickNavigateBack(View view) {
        CancelWritingReview.getDataFromServer(this, mRequestQueue, mOrderIdx);
        super.onClickNavigateBack(view);
    }

    @Override
    public void onBackPressed() {
        CancelWritingReview.getDataFromServer(this, mRequestQueue, mOrderIdx);
        super.onBackPressed();
    }
}
