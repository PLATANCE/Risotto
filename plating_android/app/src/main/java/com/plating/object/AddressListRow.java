package com.plating.object;

/**
 * Created by Rooney on 16. 1. 4..
 */
public class AddressListRow {
    private int idx;
    private String address;
    private String address_detail;
    private int in_use;
    private int delivery_available;
    private String reservation_type;
    private double lat;
    private double lon;
    private String area;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public void setArea(String area) { this.area = area; }

    public int getIn_use() {
        return in_use;
    }

    public void setIn_use(int in_use) {
        this.in_use = in_use;
    }

    public int getDelivery_available() {
        return delivery_available;
    }

    public void setDelivery_available(int delivery_available) {
        this.delivery_available = delivery_available;
    }

    public String getReservation_type() {
        return reservation_type;
    }

    public void setReservation_type(String reservation_type) {
        this.reservation_type = reservation_type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getArea() {
        return area;
    }
}
