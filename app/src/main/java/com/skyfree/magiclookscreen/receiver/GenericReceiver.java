package com.skyfree.magiclookscreen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.skyfree.magiclookscreen.Utils;
import com.skyfree.magiclookscreen.service.SensorService;

/**
 * Created by Admin on 2/3/2018.
 */

public class GenericReceiver extends BroadcastReceiver {
    public static boolean isScreenOn = true;

    public GenericReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SensorService.class);
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
                isScreenOn = true;
//                i.setAction(Utils.ACTION_UNREGISTER_LISTENER);
                context.startService(i);
                break;
            case Intent.ACTION_SCREEN_OFF:
                isScreenOn = false;
//                i.setAction(Utils.ACTION_REGISTER_LISTENER);
                context.startService(i);
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                context.startService(i);
                break;
        }
    }
}
