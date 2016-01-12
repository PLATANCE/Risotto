package com.plating.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.network.VolleySingleton;
import com.plating.pages.p_write_review_activity.WriteReviewListActivity;
import com.plating.pages.s_refer.ReferActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;

/**
 * Created by home on 16. 1. 12..
 */
public class ReferDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ImageView image;
    //private ImageLoader imageLoader;
    private String imageUrl;
    private ImageButton bt_close;

    public ReferDialog(Context context, String imageUrl) {
        super(context);
        this.mContext = context;
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.s_dialog_layout);

        // control dialog size
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        getWindow().setLayout(height / 2, height / 2);

        // Set View
        image = (ImageView) findViewById(R.id.imageView);
        //imageLoader = VolleySingleton.getsInstance().getmImageLoader();
        bt_close = (ImageButton) findViewById(R.id.bt_close);

        // image load
        VolleySingleton.getsInstance().loadImageToImageView(image, imageUrl);

        // set Listener
        image.setOnClickListener(this);
        bt_close.setOnClickListener(this);

        this.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        if(v == image) {
            MixPanel.mixPanel_trackWithOutProperties("Click Image Dialog");
            ((WriteReviewListActivity)mContext).moveToReferActivity();
        } else if(v == bt_close) {
            MixPanel.mixPanel_trackWithOutProperties("Click Close Dialog");
            dismiss();
        }
    }
}
