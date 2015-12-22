package com.plating.object;

/**
 * Created by cheehoonha on 9/12/15.
 */
public class PlaceOrderResult {
    public String status;
    public int orderIdx;

    public PlaceOrderResult() {

    }

    public PlaceOrderResult(String status, int orderIdx) {
        this.status = status;
        this.orderIdx = orderIdx;
    }
}
