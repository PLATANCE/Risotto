package com.plating.helperAPI;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.plating.application.MyApplication;

/**
 * Created by cheehoonha on 10/7/15.
 */
public class KeyboardAPI {
    public static InputMethodManager openKeyboardForecefully() {
        InputMethodManager imm = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        return imm;
    }

}
