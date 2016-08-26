package com.plating.pages.c_daily_menu_list;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.helperAPI.PriceAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.object.DailyMenu;
import com.plating.object.MenuInCart;
import com.plating.object_singleton.Cart;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.object.MixPanelProperty;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/6/14.
 */
public class DailyMenuListAdapter extends RecyclerView.Adapter<DailyMenuListAdapter.MenuViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "RestaurantListAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<DailyMenu> data = new ArrayList<DailyMenu>();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public ImageLoader imageLoader;

    public DailyMenuListAdapter(Context context, ArrayList<DailyMenu> data) {
        Log.d(LOG_TAG, "Start: RestaurantListAdapter");

        mContext = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);

        imageLoader = VolleySingleton.getInstance().getmImageLoader();
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");
        View view;
        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.d_daily_menu_list_row, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.d_daily_menu_list_header, viewGroup, false);
        }
        MenuViewHolder holder = new MenuViewHolder(view, viewType);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder: Start");
        if (getItemViewType(position) == TYPE_ITEM) {
            DailyMenu dailyMenu = data.get(position - 1);

            // Load image with volley for food image
            VolleySingleton.getInstance().loadImageToImageView(viewHolder.menuImage, RequestURL.DAILY_MENU_IMAGE_URL + dailyMenu.imageUrlMenu);
            if (dailyMenu.is_event == 0) {
                viewHolder.imageView_event_tag.setVisibility(View.GONE);
            }

            // Load image with volley for chef image
            VolleySingleton.getInstance().loadImageToImageView(viewHolder.chefImage, RequestURL.CHEF_IMAGE_URL + dailyMenu.imageUrlChef);

            if (dailyMenu.stock <= 0) {
                viewHolder.putToCartButton.setEnabled(false);
                viewHolder.menuSoldOutImageLayout.setVisibility(View.VISIBLE);
                if (dailyMenu.stock == 0) {
                    viewHolder.menuStatusMainTextview.setText("SOLD OUT!");
                    viewHolder.menuStatusSubTextview.setText("금일 메뉴가 매진 되었습니다");
                } else if (dailyMenu.stock < 0) {
                    viewHolder.menuStatusMainTextview.setText("주문 마감");
                    viewHolder.menuStatusSubTextview.setText("오늘은 플레이팅 쉬는 날 입니다");
                }
            } else {
                viewHolder.putToCartButton.setEnabled(true);
                viewHolder.menuSoldOutImageLayout.setVisibility(View.GONE);
            }

            viewHolder.menuDescription.setText(dailyMenu.nameMenu.replace(".", "\n"));
            viewHolder.chefName.setText(dailyMenu.nameChef);

            viewHolder.menuPrice.setText(PriceAPI.intPriceToStringPriceWonTextFormat(dailyMenu.price));
            viewHolder.menuPrice_alt.setText(PriceAPI.intPriceToStringPriceWonTextFormat(dailyMenu.altPrice));

            if (dailyMenu.price == dailyMenu.altPrice) {
                viewHolder.RL_menu_price_alt.setVisibility(View.GONE);

            } else {
                viewHolder.RL_menu_price_alt.setVisibility(View.VISIBLE);
                viewHolder.menuPrice.setPaintFlags(viewHolder.menuPrice.getPaddingBottom() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            viewHolder.putToCartButton.setTag(position);

            viewHolder.numOfReviewsTextView.setText("(" + dailyMenu.numOfReviews + ")");
            setReviewRatingStars(viewHolder, dailyMenu.rating);

            // Update "Number of item in cart" textview. Yellow color
            viewHolder.updateNumberOfItemAddedInCartTextView();
        } else {
            String url = RequestURL.BANNER_IMAGE_URL + "admin_banner.png";
            VolleySingleton.getInstance().getRequestQueue().getCache().remove(url);

            VolleySingleton.getInstance().getmImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Log.d(LOG_TAG, response.toString());
                    viewHolder.imageView_banner.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    viewHolder.imageView_banner.setVisibility(View.GONE);
                    Log.d(LOG_TAG, error.toString());
                }
            });

        //loadImageToImageView(viewHolder.imageView_banner, url);

        //viewHolder.imageView_banner.setVisibility(View.GONE);

    }
}


    public void setReviewRatingStars(final MenuViewHolder viewHolder, double rating) {
        if (rating >= 4.6) {
            viewHolder.reviewStar5.setBackgroundResource(R.drawable.icon_star_filled_yellow);
        } else if (rating < 4.6 && rating >= 4.2) {
            viewHolder.reviewStar5.setBackgroundResource(R.drawable.icon_star_half_yellow);
        } else if (rating < 4.2) {
            viewHolder.reviewStar5.setBackgroundResource(R.drawable.icon_star_empty_yellow);
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public void updateData(ArrayList<DailyMenu> dailyMenus) {
        this.data = dailyMenus;
    }

class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ImageView menuImage;
    LinearLayout menuSoldOutImageLayout;
    TextView menuStatusMainTextview;
    TextView menuStatusSubTextview;
    TextView numOfItemAddedToCart;
    ImageView chefImage;
    TextView menuDescription;
    TextView chefName;
    TextView menuPrice, menuPrice_alt;
    LinearLayout RL_menu_price_alt;
    ImageButton putToCartButton;
    ImageView imageView_banner;
    ImageView imageView_event_tag;

    TextView numOfReviewsTextView;

    // Ratings & Review
    ImageView reviewStar1;
    ImageView reviewStar2;
    ImageView reviewStar3;
    ImageView reviewStar4;
    ImageView reviewStar5;

    public MenuViewHolder(View itemView, int viewType) {
        super(itemView);
        if (viewType == TYPE_ITEM) {
            itemView.setOnClickListener(this);
            menuImage = (ImageView) itemView.findViewById(R.id.menu_image);
            imageView_event_tag = (ImageView) itemView.findViewById(R.id.imageView_event_tag);
            menuSoldOutImageLayout = (LinearLayout) itemView.findViewById(R.id.daily_menu_sold_out_layout);
            menuStatusMainTextview = (TextView) itemView.findViewById(R.id.menu_status_main_textview);
            menuStatusSubTextview = (TextView) itemView.findViewById(R.id.menu_status_sub_textview);
            numOfItemAddedToCart = (TextView) itemView.findViewById(R.id.num_of_item_added_to_cart);
            chefImage = (ImageView) itemView.findViewById(R.id.chef_image);
            menuDescription = (TextView) itemView.findViewById(R.id.menu_description);
            chefName = (TextView) itemView.findViewById(R.id.chef_name);
            menuPrice = (TextView) itemView.findViewById(R.id.menu_price);
            menuPrice_alt = (TextView) itemView.findViewById(R.id.menu_price_alt);
            RL_menu_price_alt = (LinearLayout) itemView.findViewById(R.id.menu_price_altRL);
            putToCartButton = (ImageButton) itemView.findViewById(R.id.put_to_cart_button);

            numOfReviewsTextView = (TextView) itemView.findViewById(R.id.num_of_reviews_textview);
            reviewStar1 = (ImageView) itemView.findViewById(R.id.review_star_1);
            reviewStar2 = (ImageView) itemView.findViewById(R.id.review_star_2);
            reviewStar3 = (ImageView) itemView.findViewById(R.id.review_star_3);
            reviewStar4 = (ImageView) itemView.findViewById(R.id.review_star_4);
            reviewStar5 = (ImageView) itemView.findViewById(R.id.review_star_5);

            putToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MixPanel.mixPanel_trackWithOutProperties("Add Item To Cart");
                    int position = getAdapterPosition() - 1;
                    int count = 1;

                    ((DailyMenuListActivity) mContext).putMenuToCart(position, count);

                    // Show how many items are in cart
                    updateNumberOfItemAddedInCartTextView();
                }
            });
        } else {
            imageView_banner = (ImageView) itemView.findViewById(R.id.imageView_banner);
            imageView_banner.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageView_banner) {
            ((DailyMenuListActivity) mContext).moveToBannerActivity();
        } else {
            int position = getAdapterPosition() - 1;
            ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
            mixPanelPropertyArrayList.add(new MixPanelProperty("Position", position + 1));
            mixPanelPropertyArrayList.add(new MixPanelProperty("Menu Name", data.get(position).nameMenu));
            MixPanel.mixPanel_trackWithProperties("Show Menu Detail", mixPanelPropertyArrayList);

            ((DailyMenuListActivity) mContext).startMenuActivity(data.get(position).idx, data.get(position).menu_d_idx, data.get(position).stock);
        }
    }


    public void updateNumberOfItemAddedInCartTextView() {
        int position = getAdapterPosition() - 1;

        ArrayList<MenuInCart> menuInCartArrayList = Cart.getCartInstance().getMenuList();

        if (menuInCartArrayList.size() == 0) {
            numOfItemAddedToCart.setVisibility(View.GONE);
            return;
        }

        for (int i = 0; i < menuInCartArrayList.size(); i++) {
            if (data.get(position).idx == menuInCartArrayList.get(i).menu_idx) {
                if (menuInCartArrayList.get(i).count > 0) {
                    // If visibility is gone, make it visible with fade-in animation
                    if (numOfItemAddedToCart.getVisibility() == View.GONE) {
                        numOfItemAddedToCart.setVisibility(View.VISIBLE);
                        Animation fadeInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.transition_fade_in_fast);
                        numOfItemAddedToCart.startAnimation(fadeInAnimation);
                    }
                    numOfItemAddedToCart.setText(menuInCartArrayList.get(i).count + " 개가 장바구니에 있습니다.");
                    return;
                }
            }

            // If there was no item found. This is why there is 'return' above.
            if (i == menuInCartArrayList.size() - 1) {
                numOfItemAddedToCart.setVisibility(View.GONE);
            }
        }
    }
}
}
