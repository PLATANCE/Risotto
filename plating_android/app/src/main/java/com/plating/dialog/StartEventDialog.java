package com.plating.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.helperAPI.PhoneNumberAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;

/**
 * Created by Rooney on 15. 12. 14..
 */
public class StartEventDialog extends Dialog implements View.OnClickListener {

    ImageView image;
    String imageUrl;
    ImageLoader imageLoader;
    ImageButton imageButton;
    Context mContext;
    int redirect;

    public StartEventDialog(Context context, String imageUrl, int redirect) {
        super(context);
        this.mContext = context;
        this.imageUrl = imageUrl;
        this.redirect = redirect;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.a_event_dialog);

        // Set View
        image = (ImageView) findViewById(R.id.imageView);
        imageLoader = VolleySingleton.getsInstance().getmImageLoader();
        imageButton = (ImageButton) findViewById(R.id.bt_close);

        // Image Load
        VolleySingleton.getsInstance().loadImageToImageView(image, imageUrl);

        // set Listener
        image.setOnClickListener(this);
        imageButton.setOnClickListener(this);

        this.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        if(v == imageButton) {
            dismiss();
            MixPanel.mixPanel_trackWithOutProperties("Close Event Dialog");
        } else if(v == image) {
            dismiss();
            MixPanel.mixPanel_trackWithOutProperties("Detail Event Dialog");
            if(redirect == 1) {
                Intent intent = new Intent(mContext, MyCouponListActivity.class);
                mContext.startActivity(intent);
            }
        }
    }
}
