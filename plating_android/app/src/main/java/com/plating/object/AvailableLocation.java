package com.plating.object;

/**
 * Created by cheehoonha on 9/12/15.
 */
public class AvailableLocation {
    public String name;
    public boolean available;
    public double lat, lon;

    public AvailableLocation() {

    }

    public AvailableLocation(String name, boolean available, double lat, double lon) {
        this.name = name;
        this.available = available;
        this.lat = lat;
        this.lon = lon;
    }
}
