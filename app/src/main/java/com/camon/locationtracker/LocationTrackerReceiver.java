package com.camon.locationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by camon on 2015-10-07.
 */
public class LocationTrackerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, LocationTrackerService.class);
        context.startService(i);
    }
}
