package com.plating.pages.r_coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.network.p_write_review_activity.SubmitReview;
import com.plating.network.r_coupon.GetCouponAvailableFromServer;
import com.plating.object.CouponListRow;
import com.plating.sdk_tools.mix_panel.MixPanel;

import java.util.ArrayList;

/**
 * Created by Rooney on 15. 12. 11..
 */
public class MyCouponListAdapter extends RecyclerView.Adapter<MyCouponListAdapter.MenuViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "MyCouponListAdapter";

    private Context mContext;
    private LayoutInflater inflater;

    public ArrayList<CouponListRow> data = new ArrayList<>();

    public MyCouponListAdapter(Context context, ArrayList<CouponListRow> data) {
        Log.d(LOG_TAG, "Start: MyCouponListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");

        View view = inflater.inflate(R.layout.r_coupon_list_row, viewGroup, false);
        MenuViewHolder holder = new MenuViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, int position) {
        CouponListRow couponListRow = data.get(position);

        VolleySingleton.getsInstance().loadImageToImageView(viewHolder.coupon_image, RequestURL.COUPON_IMAGE_URL + couponListRow.getImage_url_coupon());

        // When activity starts from intent, set the Button(bt_use) in visible
        Bundle extras = ((MyCouponListActivity) mContext).getIntent().getExtras();
        if (extras != null) {
            if (extras.getBoolean("btVisible")) {
                viewHolder.bt_area_layout.setVisibility(View.VISIBLE);
                viewHolder.coupon_image.setEnabled(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void updateData(ArrayList<CouponListRow> data) {
        this.data = data;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout bt_area_layout;
        ImageView coupon_image;
        TextView bt_use;
        //ImageLoader imageLoader;

        public MenuViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            coupon_image = (ImageView) itemView.findViewById(R.id.coupon_image);
            coupon_image.setOnClickListener(this);
            coupon_image.setEnabled(false);
            bt_area_layout = (RelativeLayout) itemView.findViewById(R.id.bt_area_layout);

            bt_use = (TextView) itemView.findViewById(R.id.bt_use);
            bt_use.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MixPanel.mixPanel_trackWithOutProperties("Select Coupon");
            int position = getAdapterPosition();

            ((MyCouponListActivity) mContext).sendDataToServer(data.get(position).getIdx());

        }
    }
}
