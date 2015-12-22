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
import com.plating.network.p_write_review_activity.CancelWritingReview;
import com.plating.network.p_write_review_activity.GetReviewListFromServer;
import com.plating.network.p_write_review_activity.SubmitReview;
import com.plating.object.WriteReviewRow;
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


/*

    private void SubmitReview(final String order_d_idx, final String rating, final String value) {
        RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://api.plating.co.kr/submit_review",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("SubmitReview", "result : "+response);

                        AlertDialog.Builder alert = new AlertDialog.Builder(cx);
                        alert.setTitle("");
                        alert.setMessage("소중한 의견 감사합니다");
                        alert.setCancelable(false);

                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent;
                                intent = new Intent(cx, DailyMenuListActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                finish();
                            }
                        });
                        alert.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("order_idx", order_idx+"");
                params.put("order_d_idx", order_d_idx+"");
                params.put("rating", rating+"");
                params.put("comment", value+"");


                return params;
            }
        };
        queue.add(myReq);
    }
*/

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

        Log.d(LOG_TAG, "asdfasdf : "+order_d_idx+" / "+rating+" / "+comment);
        //438 / 4.0 / null

        submitReview(order_d_idx, rating, comment);
    }

    private void submitReview(final String orderDIdx, final String rating, final String comment) {
        SubmitReview.sendDataToServer(this, mRequestQueue, mOrderIdx, orderDIdx, rating, comment);
    }

    public void submitReview_callback() {
        Log.d(LOG_TAG, "SubmitReview_callback");

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
