package com.plating.pages.h_cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.object.MenuInCart;
import com.plating.object_singleton.Cart;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/6/14.
 */
public class CartMenuListAdapter extends RecyclerView.Adapter<CartMenuListAdapter.MenuViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "RestaurantListAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<MenuInCart> data = new ArrayList<MenuInCart>();

    public ImageLoader imageLoader;

    public CartMenuListAdapter(Context context) {
        Log.d(LOG_TAG, "Start: RestaurantListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = Cart.getCartInstance().getMenuList();

        imageLoader = VolleySingleton.getsInstance().getmImageLoader();
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");

        View view = inflater.inflate(R.layout.h_cart_menu_list_row, viewGroup, false);
        MenuViewHolder holder = new MenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, int position) {

        MenuInCart menuInCart = data.get(position);
//        DailyMenu menuItem = menuInCart.menu;

        // Load image with volley for food image
        // Handle the case of error, and restaurant.imageReference == null
        if (menuInCart.image_url_menu != null) {
            imageLoader.get(RequestURL.DAILY_MENU_IMAGE_URL + menuInCart.image_url_menu, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    viewHolder.foodImage.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }

        viewHolder.menuDescription.setText(menuInCart.name_menu);
        viewHolder.menuPrice.setText("" + menuInCart.price);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView menuDescription;
        TextView menuPrice;

        // Number of items
        ImageButton itemCountMinusButton;
        TextView itemCount;
        ImageButton itemCountPlusButton;

        public MenuViewHolder(View itemView) {
            super(itemView);
            foodImage = (ImageView) itemView.findViewById(R.id.food_image);
            menuDescription = (TextView) itemView.findViewById(R.id.menu_description);
            menuPrice = (TextView) itemView.findViewById(R.id.menu_price);

            // Number of items
            itemCountMinusButton = (ImageButton) itemView.findViewById(R.id.row_item_count_minus_button);
            itemCount = (TextView) itemView.findViewById(R.id.row_item_count);
            itemCountPlusButton = (ImageButton) itemView.findViewById(R.id.row_item_count_plus_button);

            itemCountMinusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(itemCount.getText().toString());
                    if (count == 0) {
                        return;
                    } else {
                        count--;
                        itemCount.setText(Integer.toString(count));
                    }
                }
            });

            itemCountPlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = Integer.parseInt(itemCount.getText().toString());
                    count++;
                    itemCount.setText(Integer.toString(count));
                }
            });
        }
    }
}
