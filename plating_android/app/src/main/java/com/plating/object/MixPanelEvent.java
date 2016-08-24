package com.plating.object;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JudePark on 16. 8. 24..
 */
public class MixPanelEvent {
    private ArrayList<MixPanelProperty> eventInfo;
    private JSONObject properties;

    public MixPanelEvent(ArrayList<MixPanelProperty> eventInfo) {
        this.eventInfo = eventInfo;
        this.properties = new JSONObject();
    }

    public JSONObject getProperties() {
        for (MixPanelProperty event: eventInfo) {
            try {
                properties.put(event.getEventName(), event.getEventObject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
