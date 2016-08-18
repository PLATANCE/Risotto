package com.plating.object;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Created by JudePark on 16. 8. 18..
 */

@Getter
@Log
@AllArgsConstructor
public class AnalyticsEvent {
    @NonNull
    private String eventName;

    @NonNull
    private Bundle eventArguments;
}
