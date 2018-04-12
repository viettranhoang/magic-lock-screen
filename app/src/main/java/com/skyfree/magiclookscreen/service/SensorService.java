package com.skyfree.magiclookscreen.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.skyfree.magiclookscreen.R;
import com.skyfree.magiclookscreen.OnOffScreen;
import com.skyfree.magiclookscreen.receiver.GenericReceiver;


/**
 * Created by Admin on 2/3/2018.
 */

public class SensorService extends Service implements SensorEventListener {
    private static final int NOTIFICATION_ID = 1;
    private static final int SAMPLING_PERIOD_MICROSECOND = 5;

    //sensor
    private Sensor mProximitySensor;
    private SensorManager mSensorManager;

    //BroadcastReceiver
    private GenericReceiver mGenericReceiver;

    private float mMaxRange;
    private boolean mPendingWakeUp;

    public SensorService(){
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        //init BroadcastReceiver
        mGenericReceiver = new GenericReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(mGenericReceiver, filter);

        //init sensor
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mMaxRange = mProximitySensor.getMaximumRange();

        //notification
        Notification notification = new Notification.Builder(getApplication())
                .setSmallIcon(R.drawable.noti_icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_message))
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case Constants.ACTION_UNREGISTER_LISTENER:
//                    mSensorManager.unregisterListener(this, mProximitySensor);
                    mSensorManager.registerListener(this, mProximitySensor,
                            SAMPLING_PERIOD_MICROSECOND);
                    break;
                case Constants.ACTION_REGISTER_LISTENER:
                    mSensorManager.registerListener(this, mProximitySensor,
                            SAMPLING_PERIOD_MICROSECOND);
                    break;

            }
        }*/
        mSensorManager.registerListener(this, mProximitySensor,
                SAMPLING_PERIOD_MICROSECOND);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this, mProximitySensor);
        unregisterReceiver(mGenericReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float currentRange = sensorEvent.values[0];
        if (mMaxRange > 0.0F) {
            float checkRange = (mMaxRange * 2.0F) / 3F;
            if (currentRange <= checkRange) {
                mPendingWakeUp = true;
            }
            if (mPendingWakeUp && currentRange > checkRange) {
                mPendingWakeUp = false;
                wakePhoneUp();
            }
        } else {
            if (currentRange <= 2.0F) {
                mPendingWakeUp = true;
            }
            if (mPendingWakeUp && currentRange > 2.0F) {
                mPendingWakeUp = false;
                wakePhoneUp();
            }
        }

    }

    private void wakePhoneUp() {
        OnOffScreen.onScreen(SensorService.this);

        if (GenericReceiver.isScreenOn) {
            OnOffScreen.offScreen(SensorService.this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
