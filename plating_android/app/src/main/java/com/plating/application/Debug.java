package com.plating.application;

import android.util.Log;

/**
 * Created by cheehoonha on 7/15/15.
 */
public class Debug {
    public static boolean debugEnabled = true;

    public static void d(String first, String second) {
        if(debugEnabled)
            Log.d(first, second);
    }
}
