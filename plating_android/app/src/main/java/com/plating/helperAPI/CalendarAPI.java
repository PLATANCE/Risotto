package com.plating.helperAPI;

/**
 * Created by cheehoonha on 10/14/15.
 */
public class CalendarAPI {

    public static String convertIntDayToStringDay(int day) {
        if(day == 0)
            return "일";
        else if(day == 1)
            return "월";
        else if(day == 2)
            return "화";
        else if(day == 3)
            return "수";
        else if(day == 4)
            return "목";
        else if(day == 5)
            return "금";
        else if(day == 6)
            return "토";

        return "N/A";
    }
}
