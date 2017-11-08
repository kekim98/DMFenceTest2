package com.dmlab.bawoori.dmlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dmlab.bawoori.dmlib.service.DMLocationService;

public class BootCompReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, DMLocationService.class);
            context.startService(i);
        }
    }
}
