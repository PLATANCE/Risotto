package com.plating.helperAPI;

/**
 * Created by cheehoonha on 7/18/15.
 */
public class PriceAPI {
    // Return correct price format with 원
    public static String intPriceToStringPriceWonTextFormat(int price) {
        String priceString = Integer.toString(price);
        if(priceString.length() > 3) {
            priceString = priceString.substring(0,priceString.length() - 3) + "," + priceString.substring(priceString.length()-3, priceString.length());
        }
        return priceString + "원";
    }

    // Return correct price format with ₩
    public static String intPriceToStringPriceWonSymbolFormat(int price) {
        String priceString = Integer.toString(price);
        if(priceString.length() > 3) {
            priceString = priceString.substring(0,priceString.length() - 3) + "," + priceString.substring(priceString.length()-3, priceString.length());
        }
        return "₩" + priceString;
    }
}
