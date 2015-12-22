package com.plating.object;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/20/15.
 */
public class OrderHistory {
    public int idx;
    public int orderStatus;
    public String orderStatusDescription;
    public int totalPrice;
    public String deliveryTime;
    public int reviewCompleted;
    public ArrayList<OrderDetail> orderDetailArrayList;

    public OrderHistory(int idx, int orderStatus, String orderStatusDescription, int totalPrice, String deliveryTime, int reviewCompleted, ArrayList<OrderDetail> orderDetailArrayList) {
        this.idx = idx;
        this.orderStatus = orderStatus;
        this.orderStatusDescription = orderStatusDescription;
        this.totalPrice = totalPrice;
        this.deliveryTime = deliveryTime;
        this.reviewCompleted = reviewCompleted;
        this.orderDetailArrayList = orderDetailArrayList;
    }
}
