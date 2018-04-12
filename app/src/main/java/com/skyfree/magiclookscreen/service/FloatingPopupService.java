package com.skyfree.magiclookscreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.danialgoodwin.globaloverlay.GlobalOverlay;
import com.skyfree.magiclookscreen.OnOffScreen;
import com.skyfree.magiclookscreen.R;

/**
 * Created by Admin on 16/3/2018.
 */

public class FloatingPopupService extends Service {

    private GlobalOverlay mGlobalOverlay;
    ImageView view;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalOverlay = new GlobalOverlay(this);

        view = new ImageView(this);
        view.setImageResource(R.drawable.ic_all_out_black_24dp);
        mGlobalOverlay.addOverlayView(view, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnOffScreen.offScreen(FloatingPopupService.this);
                stopSelf(); // Stop service not needed.
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.setVisibility(View.GONE);

    }
}