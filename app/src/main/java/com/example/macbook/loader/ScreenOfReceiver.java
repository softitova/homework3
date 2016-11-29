package com.example.macbook.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by macbook on 29.11.16.
 */

public class ScreenOfReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, SimpleService.class));
    }
}
