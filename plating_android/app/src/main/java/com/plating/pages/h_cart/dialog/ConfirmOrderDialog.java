package com.plating.pages.h_cart.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.pages.h_cart.CartActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.sdk_tools.mix_panel.MixPanelProperty;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/5/15.
 */
public class ConfirmOrderDialog {
    public static String LOG_TAG = "PlatingActivity.ConfirmOrderDialog";
    public static Context mContext;

    private static AlertDialog mOrderConfirmDialog;
    private static CircularProgressButton mConfirmOrderButton;

    private static TextView deliveryTimeTextView;
    private static TextView addressTextView;
    private static TextView phoneNumberTextView;
    private static TextView totalPriceTextView;
    private static TextView payMethodTextView;
    private static TextView isCouponUsedTextView;

    public static void showDialog(final Context context, String deliveryTime, String address, String phoneNumber, String totalPrice, int freeCredit, int pay_method, int coupon_price) {
        mContext = context;
        mOrderConfirmDialog = new AlertDialog.Builder(context)
                .setView(((Activity)context).getLayoutInflater().inflate(R.layout.z_dialog_confirm_order, null))
                .create();
        mOrderConfirmDialog.show();

        // Set all textviews
        setAllTextViews(deliveryTime, address, phoneNumber, totalPrice, pay_method, coupon_price);


        // Set confirm order button
        setConfirmOrderButton();
    }

    public static void setAllTextViews(String deliveryTime, String address, String phoneNumber, String totalPrice, int pay_method, int coupon_price) {
        deliveryTimeTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.delivery_time);
        addressTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.address);
        phoneNumberTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.phone_number);
        totalPriceTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.total_price);
        payMethodTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.pay_method);
        isCouponUsedTextView = (TextView) mOrderConfirmDialog.findViewById(R.id.is_coupon_used);

        deliveryTimeTextView.setText(deliveryTime);
        addressTextView.setText(address);
        phoneNumberTextView.setText(phoneNumber);
        totalPriceTextView.setText(totalPrice);
        isCouponUsedTextView.setText((coupon_price > 0) ? "사용" : "사용안함");

        if(pay_method == 1) {
            payMethodTextView.setText(Constant.PAY_METHOD_CARD);
        } else if(pay_method == 2) {
            payMethodTextView.setText(Constant.PAY_METHOD_DIRECT_CARD);
        } else if(pay_method == 3) {
            payMethodTextView.setText(Constant.PAY_METHOD_DIRECT_CREDIT);
        }



    }

    public static void setConfirmOrderButton() {
        mConfirmOrderButton = (CircularProgressButton) mOrderConfirmDialog.findViewById(R.id.confirm_order_button);
        mConfirmOrderButton.setIndeterminateProgressMode(true); // turn on indeterminate progress
        mConfirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlaceOrder();
            }
        });
    }

    public static void onClickPlaceOrder() {
        // If user is placing order, don't place another order. This prevents double click.
        // Or if the button state is "order placed" or "error", prevent user from clicking the button
        if (mConfirmOrderButton.getProgress() == 50 || mConfirmOrderButton.getProgress() == 100 || mConfirmOrderButton.getProgress() == -1) {
            return;
        }

        // Send order. Deliverately delay for loading animation
        mConfirmOrderButton.setProgress(50);
        mOrderConfirmDialog.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
                if(payMethodTextView.getText().equals(Constant.PAY_METHOD_CARD)) {
                    mixPanelPropertyArrayList.add(new MixPanelProperty("Pay Method", "Card"));
                } else if(payMethodTextView.getText().equals(Constant.PAY_METHOD_DIRECT_CARD)) {
                    mixPanelPropertyArrayList.add(new MixPanelProperty("Pay Method", "Physical Card"));
                } else if(payMethodTextView.getText().equals(Constant.PAY_METHOD_DIRECT_CREDIT)) {
                    mixPanelPropertyArrayList.add(new MixPanelProperty("Pay Method", "Cash"));
                }

                mixPanelPropertyArrayList.add(new MixPanelProperty("Coupon", (isCouponUsedTextView.getText().equals("사용")) ? true : false));

                MixPanel.mixPanel_trackWithProperties("Confirm Order", mixPanelPropertyArrayList);
                ((CartActivity)mContext).place_order(mOrderConfirmDialog, mConfirmOrderButton);
            }
        }, 1000);
    }
}
