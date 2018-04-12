package com.skyfree.magiclookscreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rvalerio.fgchecker.AppChecker;
import com.skyfree.magiclookscreen.Utils;
import com.skyfree.magiclookscreen.model.App;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 10/3/2018.
 */

public class ExceptAppService extends Service {
    private boolean check = false;
    private Timer mTimer;

    private String appIsRunning = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mTimer =  new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                appIsRunning = new AppChecker().getForegroundApp(getApplicationContext());

                stopServiceWithExceptApp();
                startServiceWithExceptApp();
            }
        },1000,2000);

        return START_STICKY;
    }

    private void startServiceWithExceptApp() {

        if (!checkExceptAppIsRunning() && check) {
            if (Utils.FLOATING_SERVICE) startService(new Intent(getApplicationContext(), FloatingPopupService.class));
            if (Utils.SENSOR_SERVICE) startService(new Intent(getApplicationContext(), SensorService.class));
            check = false;
        }
    }

    private boolean checkExceptAppIsRunning() {

        for (App app : Utils.LISTAPP) {
            if (app.getChoose() && appIsRunning.contains(app.getId())) {
                return true;
            }
        }
        return false;
    }

    private void stopServiceWithExceptApp() {

        if (checkExceptAppIsRunning()) {
            if (Utils.FLOATING_SERVICE) stopService(new Intent(getApplicationContext(), FloatingPopupService.class));
            if (Utils.SENSOR_SERVICE) stopService(new Intent(getApplicationContext(), SensorService.class));
            check = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


