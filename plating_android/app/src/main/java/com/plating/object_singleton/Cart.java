package com.plating.object_singleton;

import android.util.Log;

import com.plating.application.Constant;
import com.plating.object.MenuInCart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by cheehoonha on 9/4/15.
 */

// Singleton Cart class
public class Cart {
    public static Integer time_slot_idx;
    private static Cart cartInstance = null;
    private ArrayList<MenuInCart> mMenuList;

    // Cart contains menuInCart which containes menu and count value
    private Cart() {
        mMenuList = new ArrayList<MenuInCart>();
    }

    public static Cart getCartInstance() {
        if(cartInstance == null) {
            cartInstance = new Cart();
        }

        return  cartInstance;
    }

    public ArrayList<MenuInCart> getMenuList() {
        return mMenuList;
    }

    // Add 'count' number of menu to cart

    public void addMenuToCart(int menu_d_idx, int menu_idx, int count, int price, int alt_price, String image_url_menu, String name_menu) {
        for(int i = 0; i < mMenuList.size(); i++) {
            if(mMenuList.get(i).menu_idx == menu_idx) {
                mMenuList.get(i).addCount(count);
                return;
            }
        }
//        try {
//            JSONObject props = new JSONObject();
//            props.put("via", "kakao");
//
//            SVUtil.mixpanel(cx).track("signup", props);
//
//        } catch (JSONException e) {
//            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//        }
        mMenuList.add(new MenuInCart(menu_d_idx, menu_idx, count, price, alt_price, image_url_menu, name_menu));
    }

    // Remove a menuItem from cart. If the menu count is 0, remove the item and return the position (so that we can update the listview)
    public int removeMenuFromCart(int menu_idx) {
        // Remove an item from cart
        int i;
        for(i = 0; i < mMenuList.size(); i++) {
            if(mMenuList.get(i).menu_idx == menu_idx) {
                mMenuList.get(i).subtractCount();
                break;
            }
        }

        // This is to prevent fast click
        if(i == mMenuList.size())
            return -1;

        // If count is zero, remove the item from cart
        if(mMenuList.get(i).count == 0) {
            mMenuList.remove(i);
        }

        return i;
    }

    public int size() {
        int size = 0;
        for(int i = 0; i < mMenuList.size(); i++)
            size = size + mMenuList.get(i).count;

        return size;
    }

    public int getTotalPriceOfAllItems() {
        int totalPrice = 0;
        for(int i = 0; i < mMenuList.size(); i++)
            totalPrice = totalPrice + (mMenuList.get(i).price * mMenuList.get(i).count);

        return totalPrice;
    }

    // 깎아줄 금액 계산
    public int getTotalDiscountedPriceOfAllItems(int free_credit, int myPoint) {
        // 쿠폰이 없을 땐 altPrice
        // 쿠폰이 있을 땐 쿠폰개수만큼 차감

        Log.d("", "free_credit : "+free_credit);
        Log.d("", "mMenuList.size : "+mMenuList.size());


        int totalDiscountedPrice = 0;

        if (free_credit > 0) {

            Collections.sort(mMenuList, myComparator);

            // mMenuList에 menu와 그 수량이 순서대로 들어있다.
            //
            for (int i = 0; i < mMenuList.size(); i++) {
                if (free_credit == 0) {
                    totalDiscountedPrice += ((mMenuList.get(i).price - mMenuList.get(i).alt_price) * mMenuList.get(i).count);
                } else if (free_credit <= mMenuList.get(i).count) {
                    totalDiscountedPrice += (mMenuList.get(i).price * free_credit);
                    totalDiscountedPrice += ((mMenuList.get(i).price - mMenuList.get(i).alt_price) * (mMenuList.get(i).count - free_credit));
                    free_credit = 0;
                } else {
                    totalDiscountedPrice += (mMenuList.get(i).price * mMenuList.get(i).count);
                    free_credit -= mMenuList.get(i).count;
                }
            }
        } else {
            for (int i = 0; i < mMenuList.size(); i++) {
                Log.d("", "alt : "+mMenuList.get(i).alt_price);
                totalDiscountedPrice += ((mMenuList.get(i).price - mMenuList.get(i).alt_price) * mMenuList.get(i).count);
            }
        }

        totalDiscountedPrice += myPoint;

        return totalDiscountedPrice;
    }

    // 깎아줄 금액 계산
    public int getTotalDiscountedPriceOfAllItems(int free_credit, int myPoint, int coupon_price) {
        // 쿠폰이 없을 땐 altPrice
        // 쿠폰이 있을 땐 쿠폰개수만큼 차감

        Log.d("", "free_credit : "+free_credit);
        Log.d("", "mMenuList.size : "+mMenuList.size());


        int totalDiscountedPrice = 0;

        if (free_credit > 0) {

            Collections.sort(mMenuList, myComparator);

            // mMenuList에 menu와 그 수량이 순서대로 들어있다.
            //
            for (int i = 0; i < mMenuList.size(); i++) {
                if (free_credit == 0) {
                    totalDiscountedPrice += ((mMenuList.get(i).price - mMenuList.get(i).alt_price) * mMenuList.get(i).count);
                } else if (free_credit <= mMenuList.get(i).count) {
                    totalDiscountedPrice += (mMenuList.get(i).price * free_credit);
                    totalDiscountedPrice += ((mMenuList.get(i).price - mMenuList.get(i).alt_price) * (mMenuList.get(i).count - free_credit));
                    free_credit = 0;
                } else {
                    totalDiscountedPrice += (mMenuList.get(i).price * mMenuList.get(i).count);
                    free_credit -= mMenuList.get(i).count;
                }
            }
        } else {
            for (int i = 0; i < mMenuList.size(); i++) {
                Log.d("", "alt : "+mMenuList.get(i).alt_price);
                totalDiscountedPrice += ((mMenuList.get(i).price - mMenuList.get(i).alt_price) * mMenuList.get(i).count);
            }
        }

        if(coupon_price > 0) {
            totalDiscountedPrice += coupon_price;
        }

        totalDiscountedPrice += myPoint;

        return totalDiscountedPrice;
    }


    public int getPriceToPayWithoutPoint(){
        int totalAltPrice = 0;
        for(int i = 0 ; i < mMenuList.size(); i++){
            totalAltPrice += mMenuList.get(i).alt_price * mMenuList.get(i).count;
        }
        return totalAltPrice + Constant.DELIVERY_PRICE;
    }

    public int getAvailablePoint(int currentMyPoint) {
        return Math.min(getPriceToPayWithoutPoint(), currentMyPoint);
    }
    private final Comparator<MenuInCart> myComparator = new Comparator<MenuInCart>() {
        @Override
        public int compare(MenuInCart box, MenuInCart box2) {

            return box2.price - box.price;

        }
    };


    public void emptyCart() {
        mMenuList.clear();
    }
}
