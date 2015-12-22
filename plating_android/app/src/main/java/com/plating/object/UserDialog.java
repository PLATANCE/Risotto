package com.plating.object;

/**
 * Created by home on 15. 12. 15..
 */
public class UserDialog {
    private int coupon_idx;
    private int is_used;
    private String image_url_dialog;

    public int getCoupon_idx() {
        return coupon_idx;
    }

    public void setCoupon_idx(int coupon_idx) {
        this.coupon_idx = coupon_idx;
    }

    public int getIs_used() {
        return is_used;
    }

    public void setIs_used(int is_used) {
        this.is_used = is_used;
    }

    public String getImage_url_dialog() {
        return image_url_dialog;
    }

    public void setImage_url_dialog(String image_url_dialog) {
        this.image_url_dialog = image_url_dialog;
    }
}
