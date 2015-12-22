package com.plating.object;

/**
 * Created by cheehoonha on 5/15/15.
 */
public class MenuInCart {
//    public DailyMenu menu;
    public int menu_d_idx;
    public int menu_idx;
    public int count = 0;
    public int price;
    public int alt_price;
    public String image_url_menu;
    public String name_menu;

    public MenuInCart(int menu_d_idx, int menu_idx, int count, int price, int alt_price, String image_url_menu, String name_menu) {
        this.menu_d_idx = menu_d_idx;
        this.menu_idx = menu_idx;
        this.count = count;
        this.price = price;
        this.alt_price = alt_price;
        this.image_url_menu = image_url_menu;
        this.name_menu = name_menu;
    }

    public void addCount(int count) {
        this.count = this.count + count;
    }

    public void subtractCount() {
        count--;
    }
}
