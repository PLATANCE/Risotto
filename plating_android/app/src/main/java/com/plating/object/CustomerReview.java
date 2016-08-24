package com.plating.object;

import lombok.Data;

/**
 * Created by cheehoonha on 10/12/15.
 */
@Data
public class CustomerReview {
    public int idx;
    public double rating;
    public String comment;
    public String time;
    public String mobile;
}
