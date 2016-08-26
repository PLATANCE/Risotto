package com.plating.pages.d_menu_detail;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.PriceAPI;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.network.d_menu_detail.GetMenuDetailFromServer;
import com.plating.network.d_menu_detail.GetMoreReviewFromServer;
import com.plating.object.CustomerReview;
import com.plating.object.MenuInCart;
import com.plating.object.SingleMenu;
import com.plating.object_singleton.Cart;
import com.plating.pages.e_chef_detail.ChefDetailActivity;

import java.util.ArrayList;

public class MenuDetailActivity extends PlatingActivity {
    private int mMenuIdx;
    private int mMenuDIdx;
    private SingleMenu mDailyMenu;
    private int mStock;

    private TextView mMenuName;
    private TextView mMenuName_eng;
    private ImageView mMenuImage;
    private TextView mNumOfItemAddedToCart;
    private TextView mPrice;
    private TextView mPrice_alt;
    private LinearLayout RL_price_original;

    // Chef information
    private RelativeLayout mChefInformationLayout;
    private ImageView mChefImage;
    private TextView mChefName;
    private TextView mChefSummary;

    // Food information
    private TextView mDescription;
    private TextView mIngredients;
    private TextView mCalories;

    // Put to cart button
    private ImageButton mPutToCartButton;

    // Review
    private LinearLayout mCustomerReviewLayout;
    private TextView mNumOfReviewsTextView;
    private ImageView mReviewStar5;
    private int mNumberOfBestReviews;

    // Sold out or closed
    private LinearLayout mDailyMenuSoldOutLayout;
    private TextView mMenuStatusMainTextview;
    private TextView mMenuStatusSubTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_menu_detail_activity);

        Intent intent = getIntent();
        mMenuIdx = intent.getIntExtra(Constant.DAILY_MENU_ACTIVITY__MENU_IDX, -1);
        mMenuDIdx = intent.getIntExtra(Constant.DAILY_MENU_ACTIVITY__MENU_D_IDX, -1);
        mStock = intent.getIntExtra(Constant.DAILY_MENU_ACTIVITY__STOCK, -1);

        getAllViews();
        menuIsSoldOutOrClosed();
        setButtonClickListener();

        getMenuDetailFromServer(mMenuIdx);
    }

    public void getAllViews() {
        // Overview
        mMenuName = (TextView) findViewById(R.id.menu_name);
        mMenuName_eng = (TextView) findViewById(R.id.menu_name_eng);
        mMenuImage = (ImageView) findViewById(R.id.menu_image);
        mNumOfItemAddedToCart = (TextView) findViewById(R.id.num_of_item_added_to_cart);
        mPrice = (TextView) findViewById(R.id.menu_price);
        mPrice_alt = (TextView) findViewById(R.id.menu_price_alt);
        RL_price_original = (LinearLayout) findViewById(R.id.RL_menu_price_ori);

        //Soldout Or Closed
        mDailyMenuSoldOutLayout = (LinearLayout) findViewById(R.id.daily_menu_sold_out_layout);
        mMenuStatusMainTextview = (TextView) findViewById(R.id.menu_status_main_textview);
        mMenuStatusSubTextview = (TextView) findViewById(R.id.menu_status_sub_textview);

        // Chef information
        mChefInformationLayout = (RelativeLayout) findViewById(R.id.chef_information_layout);
        mChefImage = (ImageView) findViewById(R.id.chef_image);
        mChefName = (TextView) findViewById(R.id.chef_name);
        mChefSummary = (TextView) findViewById(R.id.chef_summary);

        // Food information
        mDescription = (TextView) findViewById(R.id.description);
        mIngredients = (TextView) findViewById(R.id.ingredients);
        mCalories = (TextView) findViewById(R.id.calories);

        // Add to cart button
        mPutToCartButton = (ImageButton) findViewById(R.id.put_to_cart_button);

        // Review elements
        mCustomerReviewLayout = (LinearLayout) findViewById(R.id.customer_review_layout);

        mNumOfReviewsTextView = (TextView) findViewById(R.id.num_of_reviews_textview);
        mReviewStar5 = (ImageView) findViewById(R.id.review_star_5);
    }

    public void menuIsSoldOutOrClosed() {
        if(mStock <= 0) {
            mPutToCartButton.setEnabled(false);
            mDailyMenuSoldOutLayout.setVisibility(View.VISIBLE);
            if(mStock == 0) {
                mMenuStatusMainTextview.setText("SOLD OUT!");
                mMenuStatusSubTextview.setText("금일 메뉴가 매진 되었습니다");
            } else if (mStock < 0) {
                mMenuStatusMainTextview.setText("주문 마감");
                mMenuStatusSubTextview.setText("오늘은 플레이팅 쉬는 날 입니다");
            }
        } else {
            mPutToCartButton.setEnabled(true);
            mDailyMenuSoldOutLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNumberOfItemAddedInCartTextView();
    }

    /***********************
     * * BUTTON CLICK  *****
     ***********************/
    public void setButtonClickListener() {
        mPutToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MixPanel.mixPanel_trackWithOutProperties("Add Item To Cart");
                onClickPutMenuToCart(null);
                updateNumberOfItemAddedInCartTextView();
            }
        });

        mChefInformationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixPanel.mixPanel_trackWithOutProperties("Show Chef Info");
                Log.d(LOG_TAG, "" + mDailyMenu.imageUrlChef2 + " " + mDailyMenu.nameChef + " " + mDailyMenu.career + " " + mDailyMenu.careerSumm);
                startChefActivity(mDailyMenu.imageUrlChef2, mDailyMenu.nameChef, mDailyMenu.career, mDailyMenu.careerSumm);
            }
        });
    }

    public void onClickPutMenuToCart(View view) {
        Cart.getCartInstance().addMenuToCart(mMenuDIdx, mMenuIdx, 1, mDailyMenu.price, mDailyMenu.altPrice, mDailyMenu.imageUrlMenu, mDailyMenu.nameMenu);
    }

    public void startChefActivity(String chefImageUrl, String chefName, String chefCareer, String chefCareerSummary) {
        Intent intent = new Intent(this, ChefDetailActivity.class);
        intent.putExtra(Constant.CHEF_IMAGE_URL, chefImageUrl);
        intent.putExtra(Constant.CHEF_NAME, chefName);
        intent.putExtra(Constant.CHEF_CAREER, chefCareer);
        intent.putExtra(Constant.CHEF_CAREER_SUMMARY, chefCareerSummary);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
    }

    /***********************
     * * NETWORK OPERATION *
     ***********************/
    public void getMenuDetailFromServer(int menuIdx) {
        GetMenuDetailFromServer.getDataFromServer(this, mRequestQueue, menuIdx);
    }

    public void getMenuDetailFromServer_Callback(SingleMenu singleMenu) {
        // Retain a copy of dailyMenu
        mDailyMenu = singleMenu;

        // General information
        mMenuName.setText(singleMenu.nameMenu.replace(".", " "));   // single line title for menu
        mMenuName_eng.setText(singleMenu.nameMenu_eng.replace(".", "\n"));
        if (singleMenu.price == singleMenu.altPrice) {
            RL_price_original.setVisibility(View.GONE);
        } else {
            RL_price_original.setVisibility(View.VISIBLE);
            mPrice.setPaintFlags(mPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        mPrice.setText(PriceAPI.intPriceToStringPriceWonTextFormat(singleMenu.price));
        mPrice_alt.setText(PriceAPI.intPriceToStringPriceWonTextFormat(singleMenu.altPrice));

        // Chef information
        mChefName.setText(singleMenu.nameChef);
        mChefSummary.setText(singleMenu.career);
        mChefSummary.setText(singleMenu.careerSumm);

        // Food information
        mDescription.setText(singleMenu.story);
        mIngredients.setText(singleMenu.ingredients);
        mCalories.setText("" + singleMenu.calories + " Kcal");

        // Set Review Rating
        mNumOfReviewsTextView.setText("(" + singleMenu.numOfReviews + ")");
        setReviewRatingStars(singleMenu.rating);
        addCustomerReview(singleMenu.reviewArrayList);

        // Remember the number of best reviews
        mNumberOfBestReviews = singleMenu.reviewArrayList.size();

        // Load food image
        VolleySingleton.getInstance().loadImageToImageView(mMenuImage, RequestURL.DAILY_MENU_IMAGE_URL + singleMenu.imageUrlMenu);
        // Load chef image
        VolleySingleton.getInstance().loadImageToImageView(mChefImage, RequestURL.CHEF_IMAGE_URL + singleMenu.imageUrlChef);
    }

    public void addCustomerReview(ArrayList<CustomerReview> customerReviewArrayList) {
        for(int i = 0; i < customerReviewArrayList.size(); i++) {
            View child = getLayoutInflater().inflate(R.layout.e_menu_detail_activity_review_row, null);
            TextView reviewDate = (TextView) child.findViewById(R.id.review_date);
            TextView review = (TextView) child.findViewById(R.id.review_textview);
            TextView reviewUserId = (TextView) child.findViewById(R.id.review_user_id);

            // Set data, comment, and user id
            reviewDate.setText(customerReviewArrayList.get(i).time);
            review.setText(customerReviewArrayList.get(i).comment);
            reviewUserId.setText(customerReviewArrayList.get(i).mobile);

            double rating = customerReviewArrayList.get(i).rating;
            // Star imageviews
            ArrayList<ImageView> starImageList = new ArrayList<ImageView>(5);
            starImageList.add((ImageView) child.findViewById(R.id.review_star_1));
            starImageList.add((ImageView) child.findViewById(R.id.review_star_2));
            starImageList.add((ImageView) child.findViewById(R.id.review_star_3));
            starImageList.add((ImageView) child.findViewById(R.id.review_star_4));
            starImageList.add((ImageView) child.findViewById(R.id.review_star_5));

            int cnt = (int)rating;
            double decimal = rating - cnt;
            for(int j = 0; j < cnt; j ++) {
                starImageList.get(j).setBackgroundResource(R.drawable.icon_star_filled_yellow);
            }
            if (decimal >= 0.75) {
                starImageList.get(cnt).setBackgroundResource(R.drawable.icon_star_filled_yellow);
            } else if (decimal < 0.75 && decimal >= 0.25) {
                starImageList.get(cnt).setBackgroundResource(R.drawable.icon_star_half_yellow);
            }
            /*
            ImageView reviewStar5 = (ImageView) child.findViewById(R.id.review_star_5);
            double rating = customerReviewArrayList.get(i).rating;
            if (rating >= 4.75) {
                reviewStar5.setBackgroundResource(R.drawable.icon_star_filled_yellow);
            } else if (rating < 4.75 && rating >= 4.25) {
                reviewStar5.setBackgroundResource(R.drawable.icon_star_half_yellow);
            } else if (rating < 4.25) {
                reviewStar5.setBackgroundResource(R.drawable.icon_star_empty_yellow);
            }
            */

            // add the view to the linear layout
            mCustomerReviewLayout.addView(child);
        }
    }

    public void onClickViewMoreOrLessReview(View view) {
        if(((TextView)view).getText().equals("리뷰 더 보기")) {
            MixPanel.mixPanel_trackWithOutProperties("Show More Reviews");
            getMoreReviewFromServer(mMenuIdx);
            ((TextView)view).setText("리뷰 숨기기");
        } else {
            MixPanel.mixPanel_trackWithOutProperties("Hide Reviews");
            // Do not remove the best available reviews.
            while(mCustomerReviewLayout.getChildCount() > mNumberOfBestReviews + 1) {
                mCustomerReviewLayout.removeViewAt(mCustomerReviewLayout.getChildCount() - 1);
            }
            ((TextView)view).setText("리뷰 더 보기");
        }
    }

    public void getMoreReviewFromServer(int menuIdx) {
        GetMoreReviewFromServer.getDataFromServer(this, mRequestQueue, menuIdx);
    }

    public void getMoreReviewFromServer_Callback(ArrayList<CustomerReview> customerReviewArrayList) {
        addCustomerReview(customerReviewArrayList);
    }

    public void setReviewRatingStars(double rating) {
        if (rating >= 4.75) {
            mReviewStar5.setBackgroundResource(R.drawable.icon_star_filled_yellow);
        } else if (rating < 4.75 && rating >= 4.25) {
            mReviewStar5.setBackgroundResource(R.drawable.icon_star_half_yellow);
        } else if (rating < 4.25) {
            mReviewStar5.setBackgroundResource(R.drawable.icon_star_empty_yellow);
        }
    }

    public void updateNumberOfItemAddedInCartTextView() {
        ArrayList<MenuInCart> menuInCartArrayList = Cart.getCartInstance().getMenuList();

        if (menuInCartArrayList.size() == 0) {
            mNumOfItemAddedToCart.setVisibility(View.GONE);
            return;
        }

        for (int i = 0; i < menuInCartArrayList.size(); i++) {
            if (mMenuIdx == menuInCartArrayList.get(i).menu_idx) {
                if (menuInCartArrayList.get(i).count > 0) {
                    // If visibility is gone, make it visible with fade-in animation
                    if (mNumOfItemAddedToCart.getVisibility() == View.GONE) {
                        mNumOfItemAddedToCart.setVisibility(View.VISIBLE);
                        Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.transition_fade_in_fast);
                        mNumOfItemAddedToCart.startAnimation(fadeInAnimation);
                    }
                    mNumOfItemAddedToCart.setText(menuInCartArrayList.get(i).count + " 개가 장바구니에 있습니다.");
                    return;
                }
            }

            // If there was no item found. This is why there is 'return' above.
            if (i == menuInCartArrayList.size() - 1) {
                mNumOfItemAddedToCart.setVisibility(View.GONE);
            }
        }
    }

}
