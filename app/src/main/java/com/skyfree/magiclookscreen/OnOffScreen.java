package com.skyfree.magiclookscreen;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;

import com.skyfree.magiclookscreen.receiver.ScreenOffAdminReceiver;

import static android.content.ContentValues.TAG;

/**
 * Created by ATW on 7/3/2018.
 */

public class OnOffScreen {
    //off
    static DevicePolicyManager policyManager;
    static ComponentName adminReceiver;

    //on
    private static PowerManager.WakeLock mWakeLock;

    public static boolean isVibrator = false;


    public OnOffScreen(){
    }

    public static void offScreen(Service service) {
        adminReceiver = new ComponentName(service, ScreenOffAdminReceiver.class);
        policyManager = (DevicePolicyManager) service.getSystemService(Context.DEVICE_POLICY_SERVICE);

        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            runVibrate(service);
            policyManager.lockNow();
        } else {
            Log.d(TAG, "dont lock");
        }
    }

    public static void onScreen(Service service) {
        PowerManager powerManager = (PowerManager) service.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "wake_up_tag");

        mWakeLock.acquire();
        mWakeLock.release();
        runVibrate(service);

    }

    public static void runVibrate(Service service) {
        if (isVibrator) {
            Vibrator v = (Vibrator) service.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }

    }
}
