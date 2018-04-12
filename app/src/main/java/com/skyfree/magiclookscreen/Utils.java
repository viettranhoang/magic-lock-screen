package com.skyfree.magiclookscreen;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.skyfree.magiclookscreen.model.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/3/2018.
 */

public class Utils {
    public static boolean FLOATING_SERVICE = false;
    public static boolean SENSOR_SERVICE = false;
    public static boolean TOUCH_SERVICE = false;
    public static List<App> LISTAPP = new ArrayList<>();
    public static String ACTION_UNREGISTER_LISTENER = "unregister_listener";
    public static String ACTION_REGISTER_LISTENER = "register_listener";
    public static String ID_APP = "com.skyfree.magiclookscreen";

}
