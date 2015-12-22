package com.plating.object;

/**
 * Created by cheehoonha on 10/20/15.
 */
public class OrderHistoryListRow {
    public int idx;
    public String date;
    public String address;
    public String orderTime;

    public OrderHistoryListRow() {

    }

    public OrderHistoryListRow(int idx, String date, String address, String orderTime) {
        this.idx = idx;
        this.date = date;
        this.address = address;
        this.orderTime = orderTime;
    }
}
