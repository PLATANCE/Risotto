package com.plating.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.helperAPI.PhoneNumberAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rooney on 15. 12. 14..
 */
public class StartEventDialog extends Dialog implements View.OnClickListener {

    ImageView image;
    String imageUrl;
    ImageLoader imageLoader;
    ImageButton bt_close;
    TextView textview_close_again;


    Context mContext;
    int redirect;
    SharedPreferences.Editor editor;

    public StartEventDialog(Context context, String imageUrl, int redirect, SharedPreferences.Editor editor) {
        super(context);
        this.mContext = context;
        this.imageUrl = imageUrl;
        this.redirect = redirect;
        this.editor = editor;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.a_event_dialog);

        // control dialog size
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int height = metrics.heightPixels;

        getWindow().setLayout(height / 2, height / 2);


        // Set View
        image = (ImageView) findViewById(R.id.imageView);
        imageLoader = VolleySingleton.getsInstance().getmImageLoader();
        bt_close = (ImageButton) findViewById(R.id.bt_close);
        textview_close_again = (TextView) findViewById(R.id.textview_close_again);

        // image load
        VolleySingleton.getsInstance().loadImageToImageView(image, imageUrl);

        // set Listener
        image.setOnClickListener(this);
        bt_close.setOnClickListener(this);
        textview_close_again.setOnClickListener(this);

        this.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onClick(View v) {
        if (v == bt_close) {
            MixPanel.mixPanel_trackWithOutProperties("Click Close Dialog");
            dismiss();
        } else if (v == textview_close_again) {
            MixPanel.mixPanel_trackWithOutProperties("Click Not to Show Today");
            setDateToSharedPrefference();
            dismiss();
        } else if (v == image) {
            MixPanel.mixPanel_trackWithOutProperties("Detail Event Dialog");
            dismiss();
            if (redirect == 1) {
                Intent intent = new Intent(mContext, MyCouponListActivity.class);
                mContext.startActivity(intent);
            }
        }
    }

    public void setDateToSharedPrefference() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        editor.putString("time", date);
        editor.commit();
    }
}
