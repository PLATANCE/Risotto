package com.plating.object;

/**
 * Created by cheehoonha on 10/24/15.
 */
public class WriteReviewRow {
    public int idx;
    public String menuName;
    public String menuImageUrl;
    public float rating;
    public String comment;

    public WriteReviewRow() {

    }

    public WriteReviewRow(String menuName, String menuImageUrl, float rating, String comment) {
        this.menuName = menuName;
        this.menuImageUrl = menuImageUrl;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "WriteReviewRow{" +
                "idx=" + idx +
                ", menuName='" + menuName + '\'' +
                ", menuImageUrl='" + menuImageUrl + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
