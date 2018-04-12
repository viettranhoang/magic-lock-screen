package com.skyfree.magiclookscreen.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rvalerio.fgchecker.AppChecker;
import com.skyfree.magiclookscreen.OnOffScreen;
import com.skyfree.magiclookscreen.Utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ATW on 7/3/2018.
 */

public class TouchService extends Service implements View.OnTouchListener {
    private Button mButton;

    private long firstClick;

    //check only home screen is running TouchService
    private String appIsRunning = "";

    private boolean checkLauncher = true; // check man hinh chinh co ten la "launcher", neu false thi la "home"

    private boolean checkTouchService = false;

    @Override
    public IBinder
    onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mButton = new Button(this);
        mButton.setLayoutParams(new LinearLayout.LayoutParams(1, 1));
        mButton.setOnTouchListener(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                1,1,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(mButton, params);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //check only home screen is running TouchService
        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                appIsRunning = new AppChecker().getForegroundApp(getApplicationContext());
                if (appIsRunning.contains("home")) checkLauncher = false;

                if ((!appIsRunning.contains("launcher") && checkLauncher) || (!appIsRunning.contains("home") && !checkLauncher)) {
                    stopService(new Intent(getApplicationContext(), TouchService.class));
                    checkTouchService = true;
                }

                if (checkTouchService && Utils.TOUCH_SERVICE) {
                    if ((appIsRunning.contains("launcher") && checkLauncher) || (appIsRunning.contains("home") && !checkLauncher)) {
                        startService(new Intent(getApplicationContext(), TouchService.class));
                        checkTouchService = false;
                    }
                }

            }
        },1000,2000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mButton != null)
        {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mButton);
            mButton = null;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (firstClick>0){

            if (System.currentTimeMillis()-firstClick<300){

                OnOffScreen.offScreen(TouchService.this);
                firstClick=0;

                return false;
            }
        }

        firstClick = System.currentTimeMillis();

        return false;
    }
}
