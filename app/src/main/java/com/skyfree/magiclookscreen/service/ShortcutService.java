package com.skyfree.magiclookscreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.skyfree.magiclookscreen.OnOffScreen;

/**
 * Created by Admin on 9/3/2018.
 */

public class ShortcutService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        OnOffScreen.offScreen(ShortcutService.this);
        return  START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
