package com.plating.object;

/**
 * Created by cheehoonha on 9/12/15.
 */
public class AvailableLocation {
    public String roadNameAddress;
    public String jibunAddress;
    public boolean available;
    public double latitude, longitude;
    public String area;
    public int reservationType;

    public AvailableLocation() {

    }

    public AvailableLocation(String roadNameAddress, String jibunAddress, boolean available, double latitude, double longitude, String area, int reservationType) {
        this.roadNameAddress = roadNameAddress;
        this.jibunAddress = jibunAddress;
        this.available = available;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
        this.reservationType = reservationType;
    }
}
