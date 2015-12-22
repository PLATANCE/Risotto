package com.plating.object;

/**
 * Created by home on 15. 12. 11..
 */
public class CouponListRow {
    private int idx;
    private String name;
    private String txt;
    private int price;
    private String image_url_coupon;
    private String exp_date;
    private int is_available;
    private int is_used;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage_url_coupon() {
        return image_url_coupon;
    }

    public void setImage_url_coupon(String image_url_coupon) {
        this.image_url_coupon = image_url_coupon;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public int getIs_available() {
        return is_available;
    }

    public void setIs_available(int is_available) {
        this.is_available = is_available;
    }

    public int getIs_used() {
        return is_used;
    }

    public void setIs_used(int is_used) {
        this.is_used = is_used;
    }
}
