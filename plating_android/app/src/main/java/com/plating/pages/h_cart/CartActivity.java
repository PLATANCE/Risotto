package com.plating.pages.h_cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.CircularProgressButton;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.DialogAPI;
import com.plating.helperAPI.PhoneNumberAPI;
import com.plating.helperAPI.PriceAPI;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.network.r_coupon.GetMyCouponListFromServer;
import com.plating.object.CouponListRow;
import com.plating.object.MenuInCart;
import com.plating.object_singleton.Cart;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.pages.h_cart.dialog.ConfirmOrderDialog;
import com.plating.pages.h_cart.dialog.EnterPhoneNumberDialog;
import com.plating.pages.i_set_location.AddressListActivity;
import com.plating.pages.i_set_location.SetLocationActivity;
import com.plating.pages.j_payment.AddCreditCardActivity;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.object.MixPanelProperty;
import com.plating.util.SVUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends PlatingActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 10;
    private TextView mCartTotalPrice;
    private TextView mDiscountedPrice;
    private TextView mDeliveryFee;
    private TextView mTotalPaymentFee;
    private TextView mAddress;
    private TextView mPhoneNumber, mRecipient;
    private TextView mDeliveryTime, TV_card;
    private TextView messageText;

    private RelativeLayout RL_discount;
    private RelativeLayout RL_coupon;

    private String card_no;

    private LinearLayout cart_delivery_detail_layout;
    private LinearLayout cart_payment_detail_layout;
    private LinearLayout cart_cutlery_detail_layout;
    private LinearLayout messageBox;
    private RelativeLayout IB_time, IB_phone, IB_address, IB_payment, IB_addcard, IB_recipient;
    private Button Btn_order;
    private ImageView mobileArrowImageview;

    // Radio Button For payment
    private RadioGroup radio_pay_group;
    private RadioButton radio_pay_card, radio_pay_direct_card, radio_pay_direct_credit;

    // Radio Button For Cutlery
    private RadioGroup radio_cutlery_group;
    private RadioButton radio_cutlery_yes, radio_cutlery_no;
    private int include_cutlery;

    private ArrayList<TimeSlotBox> time_slot_list;

    private int selected_time_slot, selectedRecipient;
    private int free_credit, deliveryFee, myPoint;

    private int total_price, bk_len;
    private boolean delivery_available, card_exist;
    private int credit_used;

    private ImageButton bt_add_coupon;
    private int coupon_price;
    private int coupon_idx;

    public Context cx;

    public String LOG_TAG;
    public int pay_method;
    public int min_total_price;

    public String message;
    public boolean canOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_cart_activity);

        free_credit = 0;
        cx = this;

        LOG_TAG = getClass().getSimpleName();

        getAllViews();
        setMenuListView();

        getCouponListFromServer();
    }

    public void getAllViews() {
        // Layouts regarding Cart information (i.e: price, discounted price, total price, and so on)
        mCartTotalPrice = (TextView) findViewById(R.id.cart_total_price);
        mDiscountedPrice = (TextView) findViewById(R.id.discounted_price);
        mDeliveryFee = (TextView) findViewById(R.id.delivery_fee);
        mTotalPaymentFee = (TextView) findViewById(R.id.total_payment_fee);
        messageText = (TextView) findViewById(R.id.messageText);

        RL_discount = (RelativeLayout) findViewById(R.id.RL_discount);

        // coupon button layout
        RL_coupon = (RelativeLayout) findViewById(R.id.RL_coupon);

        cart_payment_detail_layout = (LinearLayout) findViewById(R.id.cart_payment_detail_layout);
        messageBox = (LinearLayout) findViewById(R.id.messageBox);

        // Textviews regarding delivery information (address, phone number, credit card, and so on)
        mAddress = (TextView) findViewById(R.id.set_user_address_textview);
        mPhoneNumber = (TextView) findViewById(R.id.set_user_phone_number_textview);
        mRecipient = (TextView) findViewById(R.id.set_recipient_textview);
        mDeliveryTime = (TextView) findViewById(R.id.set_delivery_time_textview);
        TV_card = (TextView) findViewById(R.id.set_credit_card_textview);


        // Radio Button For Setting Payment
        radio_pay_group = (RadioGroup) findViewById(R.id.radio_pay_group);
        radio_pay_card = (RadioButton) findViewById(R.id.radio_pay_card);
        radio_pay_card.setOnClickListener(this);
        radio_pay_direct_card = (RadioButton) findViewById(R.id.radio_pay_direct_card);
        radio_pay_direct_card.setOnClickListener(this);
        radio_pay_direct_credit = (RadioButton) findViewById(R.id.radio_pay_direct_credit);
        radio_pay_direct_credit.setOnClickListener(this);

        // Radio Button for Setting Cutlery
        radio_cutlery_group = (RadioGroup) findViewById(R.id.radio_cutlery_group);
        radio_cutlery_yes = (RadioButton) findViewById(R.id.radio_cutlery_yes);
        radio_cutlery_no = (RadioButton) findViewById(R.id.radio_cutlery_no);

        // initailze radio_cutlery_yes to check
        radio_cutlery_yes.setChecked(true);

        // Buttons for setting user information (address, phone number, credit card, and so on)
        IB_address = (RelativeLayout) findViewById(R.id.set_user_address_layout);
        IB_address.setOnClickListener(this);
        IB_phone = (RelativeLayout) findViewById(R.id.set_user_phone_number_layout);
        IB_phone.setOnClickListener(this);
        IB_time = (RelativeLayout) findViewById(R.id.set_delivery_time_layout);
        IB_time.setOnClickListener(this);
        IB_payment = (RelativeLayout) findViewById(R.id.set_way_of_payment_layout);
        IB_addcard = (RelativeLayout) findViewById(R.id.set_credit_card_layout);
        IB_addcard.setOnClickListener(this);
        IB_recipient = (RelativeLayout) findViewById(R.id.set_recipient_layout);
        IB_recipient.setOnClickListener(this);


        // Add Coupon Button
        bt_add_coupon = (ImageButton) findViewById(R.id.bt_add_coupon);
        bt_add_coupon.setOnClickListener(this);

        // mobile Arrow Image
        mobileArrowImageview = (ImageView) findViewById(R.id.mobile_arrow_imageview);

        // Place order button
        Btn_order = (Button) findViewById(R.id.cart_activity_orderButton);
        Btn_order.setOnClickListener(this);

    }

    public void setMenuListView() {
        ArrayList<MenuInCart> cartMenuList = Cart.getCartInstance().getMenuList();

        for (int i = 0; i < cartMenuList.size(); i++) {
            final MenuInCart menuInCart = cartMenuList.get(i);

            LinearLayout layout = (LinearLayout) findViewById(R.id.cart_menu_list);
            View child = getLayoutInflater().inflate(R.layout.h_cart_menu_list_row, null);

            final ImageView foodImage = (ImageView) child.findViewById(R.id.food_image);
            TextView menuDescription = (TextView) child.findViewById(R.id.menu_description);

            RelativeLayout RL_original_price = (RelativeLayout) child.findViewById(R.id.RL_original_price);
            TextView menuPrice = (TextView) child.findViewById(R.id.menu_price);

            TextView menuPrice_alt = (TextView) child.findViewById(R.id.menu_price_alt);

            // Number of items
            ImageButton itemCountMinusButton = (ImageButton) child.findViewById(R.id.row_item_count_minus_button);
            final TextView itemCount = (TextView) child.findViewById(R.id.row_item_count);
            ImageButton itemCountPlusButton = (ImageButton) child.findViewById(R.id.row_item_count_plus_button);


            // Load image with volley for food image
            // Handle the case of error, and restaurant.imageReference == null
            VolleySingleton.getsInstance().loadImageToImageView(foodImage, RequestURL.DAILY_MENU_IMAGE_URL + menuInCart.image_url_menu);

            menuDescription.setText(menuInCart.name_menu.replace(".", "\n"));
            menuPrice.setText(PriceAPI.intPriceToStringPriceWonTextFormat(menuInCart.price));
            menuPrice_alt.setText(PriceAPI.intPriceToStringPriceWonTextFormat(menuInCart.alt_price));


            if (menuInCart.price == menuInCart.alt_price) {
                RL_original_price.setVisibility(View.GONE);
            } else {
                RL_original_price.setVisibility(View.VISIBLE);
            }

            itemCount.setText("" + menuInCart.count);

            itemCountMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MixPanel.mixPanel_trackWithOutProperties("Decrease Amount");
                    int count = Integer.parseInt(itemCount.getText().toString());
                    if (count == 0) {
                        return;
                    } else {
                        count--;
                        itemCount.setText(Integer.toString(count));
                        Cart.getCartInstance().removeMenuFromCart(menuInCart.menu_idx);
                        setCartInformation();
                    }
                }
            });

            itemCountPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MixPanel.mixPanel_trackWithOutProperties("Increase Amount");
                    int count = Integer.parseInt(itemCount.getText().toString());
                    count++;
                    itemCount.setText(Integer.toString(count));
                    Cart.getCartInstance().addMenuToCart(menuInCart.menu_d_idx, menuInCart.menu_idx, 1, menuInCart.price, menuInCart.alt_price, menuInCart.image_url_menu, menuInCart.name_menu);
                    setCartInformation();
                }
            });
            // add the view to the linear layout
            layout.addView(child);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getCartInformation();
        enableOrDisablePlaceOrderButton();
    }


    /*
     * 2014.12.09 결제 기능 추가
     * By Rooney
     */
    @Override
    public void onClick(View view) {
        boolean flag = true;
        if (view == IB_time) {
            MixPanel.mixPanel_trackWithOutProperties("Show Delivery Time Slot");

            showTimeSlot();
        } else if(view == IB_recipient) {
            MixPanel.mixPanel_trackWithOutProperties("Show Recipient");
            showRecipient();
        } else if (view == Btn_order) {

            Cart cart = Cart.getCartInstance();

            // 서버에서 원하는 결제 최소 금액보다 결제 금액이 작을 경우, 다이얼로그 출력
            if (min_total_price > cart.getTotalPriceOfAllItems()) {
                DialogAPI.showDialog(this, "최소결제액 미달", "합계가 " + min_total_price + "원 미만인 장바구니는 주문이 불가합니다. 다른 요리를 추가하세요.", "확인", null);
                return;
            }


            int freeCredit = (free_credit <= cart.size()) ? free_credit : cart.size();
            credit_used = freeCredit;
            String deliveryTime = mDeliveryTime.getText().toString();
            String address = mAddress.getText().toString();
            String phoneNumber = mRecipient.getText().toString();
            String totalPrice = PriceAPI.intPriceToStringPriceWonSymbolFormat(total_price);
            // 결제 수단 추가
            int selectedId = radio_pay_group.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            pay_method = Integer.parseInt((String) radioButton.getTag());

            ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();

            if (pay_method == 1) {
                mixPanelPropertyArrayList.add(new MixPanelProperty("Pay Method", "Card"));
            } else if (pay_method == 2) {
                mixPanelPropertyArrayList.add(new MixPanelProperty("Pay Method", "Physical Card"));
            } else if (pay_method == 3) {
                mixPanelPropertyArrayList.add(new MixPanelProperty("Pay Method", "Cash"));
            }

            include_cutlery = Integer.parseInt((String) findViewById(radio_cutlery_group.getCheckedRadioButtonId()).getTag());
            mixPanelPropertyArrayList.add(new MixPanelProperty("Coupon", (coupon_price > 0) ? true : false));


            MixPanel.mixPanel_trackWithProperties("Place Order", mixPanelPropertyArrayList);


            Log.d(LOG_TAG, "pay_method :" + pay_method);

            if (delivery_available) {
                ConfirmOrderDialog.showDialog(this, deliveryTime, address, phoneNumber, totalPrice, freeCredit, pay_method, coupon_price, include_cutlery);
            }


        } else if (view == IB_phone) {
            if (mPhoneNumber.getText().equals(Constant.PLEASE_ENTER_PHONE_NUMBER_NEW)) {
                MixPanel.mixPanel_trackWithOutProperties("Edit Phone Number");
                Intent intent = new Intent(mContext, InputMobileForAuthActivity.class);
                startActivity(intent);
            }
        } else if (view == IB_address) {
            MixPanel.mixPanel_trackWithOutProperties("Edit Address");

            Intent intent;
            if (mAddress.getText().equals(Constant.PLEASE_ENTER_ADDRESS)) {
                intent = new Intent(mContext, SetLocationActivity.class);
            } else {
                intent = new Intent(mContext, AddressListActivity.class);
            }
            startActivity(intent);
        } else if (view == IB_addcard) {
            MixPanel.mixPanel_trackWithOutProperties("Add Credit Card");

            Intent intent = new Intent(mContext, AddCreditCardActivity.class);
            startActivity(intent);
        } else if (view == radio_pay_card) {
            if (cart_payment_detail_layout.getChildCount() == 1) {
                View child = getLayoutInflater().inflate(R.layout.h_regist_card_layout, null);
                IB_addcard = (RelativeLayout) child.findViewById(R.id.set_credit_card_layout);
                IB_addcard.setOnClickListener(this);
                TV_card = (TextView) child.findViewById(R.id.set_credit_card_textview);
                TV_card.setText(card_no);
                cart_payment_detail_layout.addView(child);
            }
            enableOrDisablePlaceOrderButton();
        } else if (view == radio_pay_direct_card) {
            if (cart_payment_detail_layout.getChildCount() == 2) {
                cart_payment_detail_layout.removeViewAt(1);
            }
            enableOrDisablePlaceOrderButton();
        } else if (view == radio_pay_direct_credit) {
            if (cart_payment_detail_layout.getChildCount() == 2) {
                cart_payment_detail_layout.removeViewAt(1);
            }
            enableOrDisablePlaceOrderButton();
        } else if (view == bt_add_coupon) {
            MixPanel.mixPanel_trackWithOutProperties("Click Use Coupon");
            Intent intent = new Intent(this, MyCouponListActivity.class);
            intent.putExtra("btVisible", true);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                coupon_price = bundle.getInt("coupon_price");
                coupon_idx = bundle.getInt("coupon_idx");
                String msg = bundle.getString("msg");

                setCartInformation();
                ToastAPI.showToast(msg);
            }
        }
    }

    public void EnterPhoneNumberDialog_Callback(String phoneNumber) {
        mRecipient.setText(phoneNumber);
    }

    private void showRecipient() {
        String[] array = {"본인", "아니요, 지인이 받습니다"};
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("어느 분이 음식을 받습니까?");
        ab.setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CartActivity", i + "");
                selectedRecipient = i;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CartActivity", selectedRecipient + "");
                // 지인을 선택하면
                if (selectedRecipient == 1) {
                    // 지인 전화번호 입력하는 dialog 를 출력
                    EnterPhoneNumberDialog.showDialog(mContext);
                    selectedRecipient = 0;
                } else {
                    mRecipient.setText("본인");
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CartActivity", i + "");
            }
        });
        ab.show();
    }

    private void showTimeSlot() {
        if (selected_time_slot == -1) {
            Toast.makeText(getApplicationContext(), "금일 주문이 마감되었습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<String> list = new ArrayList<>();
        for (TimeSlotBox box : time_slot_list) {
            list.add(box.time_slot);
        }

        String[] mStringArray = new String[list.size()];
        mStringArray = list.toArray(mStringArray);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("배달시간");
        ab.setSingleChoiceItems(mStringArray, selected_time_slot, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("", "index : " + i);
                selected_time_slot = i;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Cart.time_slot_idx = time_slot_list.get(selected_time_slot).idx;
                Log.d("", "index : " + Cart.time_slot_idx);
                mDeliveryTime.setText(time_slot_list.get(selected_time_slot).time_slot);
                enableOrDisablePlaceOrderButton();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enableOrDisablePlaceOrderButton();
            }
        });
        ab.show();
    }

    public static class TimeSlotBox {
        String time_slot;
        String time_utc;

        int idx;
    }

    public void setCartInformation() {
        Log.d(LOG_TAG, "setCartInformation : start");
        Cart cart = Cart.getCartInstance();
        mCartTotalPrice.setText(PriceAPI.intPriceToStringPriceWonSymbolFormat(cart.getTotalPriceOfAllItems()));
        int cartTotal = cart.getTotalPriceOfAllItems();

        // Delivery Fee
        if (deliveryFee == 0) {
            mDeliveryFee.setText("(이벤트) 무료");
        } else {
            mDeliveryFee.setText(PriceAPI.intPriceToStringPriceWonSymbolFormat(deliveryFee));
        }


        // 쿠폰 추가 시, RL_discount visible
        //Log.d(LOG_TAG, "setCartInformation : coupon_price" + coupon_price);
        if (cart.getTotalDiscountedPriceOfAllItems(free_credit, myPoint, coupon_price) > 0) {
            RL_discount.setVisibility(View.VISIBLE);

            // 할인 금액이, 합계 + 배달비 보다 크면, 합계 + 배달비를 아니면 할인 금액을 보여준다.
            if (cart.getTotalDiscountedPriceOfAllItems(free_credit, myPoint, coupon_price) > (cart.getTotalPriceOfAllItems() + deliveryFee)) {
                mDiscountedPrice.setText(PriceAPI.intPriceToStringPriceWonSymbolFormat((cart.getTotalPriceOfAllItems() + deliveryFee)));
            } else {
                mDiscountedPrice.setText(PriceAPI.intPriceToStringPriceWonSymbolFormat(cart.getTotalDiscountedPriceOfAllItems(free_credit, myPoint, coupon_price)));
            }
        } else {
            RL_discount.setVisibility(View.GONE);
        }


        total_price = cart.getTotalPriceOfAllItems() - cart.getTotalDiscountedPriceOfAllItems(free_credit, myPoint, coupon_price) + deliveryFee;
        if (total_price < 0) {
            total_price = 0;
        }
        mTotalPaymentFee.setText(PriceAPI.intPriceToStringPriceWonSymbolFormat(total_price));

    }

    private void get_my_point_from_server() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.plating.co.kr/promo/point?user_idx=" + SVUtil.getUserIdx(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jo = new JSONObject(response);

                            myPoint = jo.getInt("point");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    /***********************
     * * NETWORK OPERATION *
     ***********************/
    public void getCouponListFromServer() {
        GetMyCouponListFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getCouponListFromServer_Callback(ArrayList<CouponListRow> couponListRowArrayList) {
        Log.d(LOG_TAG, "getCouponListFromServer_Callback: size = " + couponListRowArrayList.size());
        if (couponListRowArrayList.size() > 0) {
            RL_coupon.setVisibility(View.VISIBLE);
        }
    }


    public void place_order(final AlertDialog orderConfirmDialog, final CircularProgressButton confirmOrderButton) {
        final StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://api.plating.co.kr/place_order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("place_order", "result : " + response);

                        String status = "";
                        String description = "";
                        int order_idx = 0;
                        ArrayList<String> oos_list = new ArrayList<>();

                        try {
                            JSONObject jo = new JSONObject(response);
                            status = jo.getString("status");
                            description = jo.getString("description");
                            if (status.equals("done")) {
                                order_idx = jo.getInt("order_idx");
                            } else if (status.equals("oos")) {
                                JSONArray oos_ja = jo.getJSONArray("oos_list");
                                for(int i = 0; i < oos_ja.length(); i++) {
                                    oos_list.add(oos_ja.getString(i));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (status.equals("done")) {

                            SVUtil.SetOrderIdx(cx, order_idx);
                            Cart.getCartInstance().emptyCart();

                            // Change button state to "complete"
                            confirmOrderButton.setProgress(100);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Dismiss the dialog.
                                    orderConfirmDialog.dismiss();

                                    // Start new activity
                                    Intent intent = new Intent(cx, DailyMenuListActivity.class);
                                    intent.putExtra(Constant.ORDER_PLACED, true);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }, 1000);

                        } else if (status.equals("oos")) {
                            String msg = "";

                            for (String item : oos_list) {
                                msg += item;
                                msg += "\n";
                            }

                            confirmOrderButton.setProgress(-1);
                            confirmOrderButton.setErrorText("재고 부족");
                            DialogAPI.showDialog(orderConfirmDialog.getContext(), "재고 부족", msg, "확인", null);
                            orderConfirmDialog.setCancelable(true);
                        } else if (status.equals("fail_to_pay")) {
                            confirmOrderButton.setProgress(-1);
                            confirmOrderButton.setErrorText("결제가 정상적으로 이루어지지 않았습니다.");
                            DialogAPI.showDialog(orderConfirmDialog.getContext(), "결제 실패", description, "확인", null);
                            orderConfirmDialog.setCancelable(true);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        confirmOrderButton.setProgress(-1);
                        confirmOrderButton.setErrorText("네트워크 상태가 불안정합니다.");
                        orderConfirmDialog.setCancelable(true);
                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Cart cart = Cart.getCartInstance();
                String menu_d_idx = "";
                String order_amount = "";
                for (MenuInCart menu : cart.getMenuList()) {
                    menu_d_idx += menu.menu_d_idx;
                    menu_d_idx += "|";
                    order_amount += menu.count;
                    order_amount += "|";
                }

                menu_d_idx = menu_d_idx.substring(0, menu_d_idx.length() - 1);
                order_amount = order_amount.substring(0, order_amount.length() - 1);

                // recipient가 본인이면 내 연락처를, 아니면 recipient 의 텍스트를
                String recipient = mRecipient.getText().toString();
                String mobile = (recipient.equals("본인")) ? mPhoneNumber.getText().toString() : recipient;

                params.put("user_idx", SVUtil.getUserIdx(cx) + "");
                params.put("time_slot", Cart.time_slot_idx + "");
                params.put("menu_d_idx", menu_d_idx);
                params.put("order_amount", order_amount);
                params.put("credit_used", credit_used + "");
                params.put("point", Integer.toString(Cart.getCartInstance().getAvailablePoint(myPoint, deliveryFee, coupon_price)) + "");
                params.put("total_price", total_price + "");
                params.put("pay_method", pay_method + "");
                params.put("mobile", mobile + "");

                // update coupon_txn set is_used = 1 where user_idx = 1471 and coupon_idx = coupon_idx
                params.put("coupon_idx", Integer.toString(coupon_idx));
                params.put("include_cutlery", Integer.toString(include_cutlery));

                Log.d(LOG_TAG, "PARAMETERS = " + params.toString());
                return params;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        myReq.setRetryPolicy(policy);
        mRequestQueue.add(myReq);
    }

    public void getCartInformation() {
        RequestQueue queue = com.android.volley.toolbox.Volley.newRequestQueue(this);
        String url = "http://api.plating.co.kr/cart_info?user_idx=" + SVUtil.getUserIdx(cx) + "&coupon_idx=" + coupon_idx;
        Log.d(LOG_TAG, url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG, "result : " + response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            JSONObject my_info = jo.getJSONObject("my_info");
                            min_total_price = jo.getInt("min_total_price");
                            bk_len = jo.getInt("bk_len");
                            card_no = jo.getString("card_no");
                            // user has already registerd credit card, inflate view and check radio button valued 'CARD'

                            TV_card.setText(card_no);
                            radio_pay_card.setChecked(true);


                            deliveryFee = jo.getInt("delivery_fee");

                            String address = (my_info.getString("address_detail").equals("")) ? my_info.getString("address") : my_info.getString("address") + "\n" + my_info.getString("address_detail");
                            Log.d(LOG_TAG, "ADDRESS: " + address);
                            mAddress.setText(address);
                            int available = my_info.getInt("delivery_available");
                            if (available == 1) {
                                delivery_available = true;
                            } else {
                                delivery_available = false;
                            }

                            free_credit = my_info.getInt("free_credit");
                            myPoint = my_info.getInt("point");
                            String phoneNumber = my_info.getString("mobile");
                            if (my_info.getString("mobile") != "null") {
                                if(my_info.getString("mobile").equals(Constant.PLEASE_ENTER_PHONE_NUMBER)) {
                                    mPhoneNumber.setText(Constant.PLEASE_ENTER_PHONE_NUMBER_NEW);
                                } else {
                                    mPhoneNumber.setText(my_info.getString("mobile"));
                                    mobileArrowImageview.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                mPhoneNumber.setText(PhoneNumberAPI.getPhoneNumber());
                            }

                            time_slot_list = new ArrayList<>();

                            JSONArray time_slot_ja = jo.getJSONArray("time_slot");
                            TimeSlotBox box = null;
                            for (int i = 0; i < time_slot_ja.length(); i++) {
                                box = new TimeSlotBox();
                                box.idx = time_slot_ja.getJSONObject(i).getInt("idx");
                                box.time_slot = time_slot_ja.getJSONObject(i).getString("time_slot");

                                time_slot_list.add(box);
                            }

                            if (time_slot_list.size() == 0) {
                                mDeliveryTime.setText(Constant.DELIVERY_IS_FINISHED);
                                selected_time_slot = -1;
                            } else {
                                mDeliveryTime.setText(Constant.PLEASE_ENTER_DELIVERY_TIME);
                                mDeliveryTime.setTextColor(getResources().getColor(R.color.orange_300));
                                IB_time.setEnabled(true);
                                selected_time_slot = 1;
                            }

                            message = jo.getString("message");
                            canOrder = jo.getBoolean("can_order");
                            if(!canOrder) {
                                messageBox.setVisibility(View.VISIBLE);
                                messageText.setText(message);
                            } else {
                                messageBox.setVisibility(View.GONE);
                                messageText.setText(Constant.PLEASE_ENTER_DELIVERY_TIME);
                            }

                            setCartInformation();
                            enableOrDisablePlaceOrderButton();

                            ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
                            boolean hasInsertedAddress = (address.equals(Constant.PLEASE_ENTER_ADDRESS) ? false : true);
                            mixPanelPropertyArrayList.add(new MixPanelProperty("Address", hasInsertedAddress));
                            if (hasInsertedAddress) {
                                mixPanelPropertyArrayList.add(new MixPanelProperty("Address Detail", address));
                            }
                            mixPanelPropertyArrayList.add(new MixPanelProperty("Coverage", (hasInsertedAddress && delivery_available) ? true : false));
                            mixPanelPropertyArrayList.add(new MixPanelProperty("Choose Covered Address", delivery_available ? true : false));

                            boolean hasInsertedPhoneNumber = (phoneNumber.equals(Constant.PLEASE_ENTER_PHONE_NUMBER) ? false : true);
                            mixPanelPropertyArrayList.add(new MixPanelProperty("Phone Number", hasInsertedPhoneNumber));
                            boolean hasInsertedCard = (card_no.equals(Constant.PLEASE_ENTER_CREDIT_CARD) ? false : true);
                            mixPanelPropertyArrayList.add(new MixPanelProperty("Credit Card", hasInsertedCard));

                            MixPanel.mixPanel_trackWithProperties("Cart Info", mixPanelPropertyArrayList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }


    public void colorUnfilledDeliveryInformationTextView() {
        if (mAddress.getText().equals(Constant.PLEASE_ENTER_ADDRESS)) {
            mAddress.setTextColor(getResources().getColor(R.color.orange_300));
        } else {
            mAddress.setTextColor(getResources().getColor(R.color.grey_600));
        }
        if (mPhoneNumber.getText().equals(Constant.PLEASE_ENTER_PHONE_NUMBER_NEW)) {
            mPhoneNumber.setTextColor(getResources().getColor(R.color.orange_300));
        } else {
            mPhoneNumber.setTextColor(getResources().getColor(R.color.grey_600));
        }
        if (mDeliveryTime.getText().equals(Constant.PLEASE_ENTER_DELIVERY_TIME) || mDeliveryTime.getText().equals(Constant.DELIVERY_IS_FINISHED)) {
            mDeliveryTime.setTextColor(getResources().getColor(R.color.orange_300));
        } else {
            mDeliveryTime.setTextColor(getResources().getColor(R.color.grey_600));
        }

        if (TV_card.getText().equals(Constant.PLEASE_ENTER_CREDIT_CARD)) {
            TV_card.setTextColor(getResources().getColor(R.color.orange_300));
        } else {
            TV_card.setTextColor(getResources().getColor(R.color.grey_600));
        }
    }

    public void enableOrDisablePlaceOrderButton() {

        colorUnfilledDeliveryInformationTextView();
        boolean isEnables = true;
        boolean radioChecked = false;

        for (int i = 0; i < radio_pay_group.getChildCount(); i++) {
            RadioButton rb = (RadioButton) radio_pay_group.getChildAt(i);
            if (rb.isChecked()) {
                radioChecked = true;
            }
        }
        Log.d(LOG_TAG, "radioChecked : " + radioChecked);
        // Enable or disable the button
        if (mAddress.getText().equals(Constant.PLEASE_ENTER_ADDRESS)) {
            Btn_order.setEnabled(false);
            Btn_order.setText("주소를 입력해주세요");
            isEnables = false;
        } else if (mPhoneNumber.getText().equals(Constant.PLEASE_ENTER_PHONE_NUMBER_NEW)) {
            Btn_order.setEnabled(false);
            Btn_order.setText(Constant.PLEASE_ENTER_PHONE_NUMBER_NEW);
            isEnables = false;
        } else if (mDeliveryTime.getText().equals(Constant.PLEASE_ENTER_DELIVERY_TIME)) {
            Btn_order.setEnabled(false);
            Btn_order.setText("배달 시간을 선택해 주세요");
            isEnables = false;
        }  else if (mDeliveryTime.getText().equals(Constant.DELIVERY_IS_FINISHED)) {
            Btn_order.setEnabled(false);
            Btn_order.setText(Constant.DELIVERY_IS_FINISHED);
            isEnables = false;
        } else if (!radioChecked) {
            Btn_order.setEnabled(false);
            Btn_order.setText("결제 수단을 선택해 주세요");
            isEnables = false;
        } else if (radio_pay_card.isChecked()) {
            if (TV_card.getText().equals(Constant.PLEASE_ENTER_CREDIT_CARD)) {
                Btn_order.setEnabled(false);
                Btn_order.setText("카드를 등록해주세요");
                isEnables = false;
            }
        }
        if (isEnables) {
            Btn_order.setEnabled(true);
            Btn_order.setText("주문하기");
        }
        /*
        if (mAddress.getText().equals(Constant.PLEASE_ENTER_ADDRESS) ||
                mPhoneNumber.getText().equals(Constant.PLEASE_ENTER_PHONE_NUMBER) ||
                mDeliveryTime.getText().equals(Constant.PLEASE_ENTER_DELIVERY_TIME) ||
                mDeliveryTime.getText().equals(Constant.DELIVERY_IS_FINISHED)
                //|| TV_card.getText().equals(Constant.PLEASE_ENTER_CREDIT_CARD)
                ) {
            Btn_order.setEnabled(false);
            Btn_order.setText("배달 정보를 입력해 주세요");
        }  else if() {

        } else {
            Btn_order.setEnabled(true);
            Btn_order.setText("주문하기");
        }
        */


    }
}
