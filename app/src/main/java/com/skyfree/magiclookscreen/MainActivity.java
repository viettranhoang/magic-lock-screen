package com.skyfree.magiclookscreen;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.skyfree.magiclookscreen.model.App;
import com.skyfree.magiclookscreen.receiver.ScreenOffAdminReceiver;
import com.skyfree.magiclookscreen.service.ExceptAppService;
import com.skyfree.magiclookscreen.service.FloatingPopupService;
import com.skyfree.magiclookscreen.service.SensorService;
import com.skyfree.magiclookscreen.service.TouchService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity{

    Locale mLocale;

    //database
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    //Cap quyen admin
    protected static final int REQUEST_ENABLE = 0;
    DevicePolicyManager mDevicePolicyManager;
    ComponentName mAdminComponent;

    //Giao dien
    @BindView(R.id.main_image_power)
    ImageView mImagePower;

    @BindView(R.id.main_switch_power)
    Switch mSwitchPower;

    @BindView(R.id.main_image_double_tap)
    ImageView mImageDoubleTap;

    @BindView(R.id.main_image_floating_popup)
    ImageView mImageFloatingPopup;

    @BindView(R.id.main_image_create_shortcut)
    ImageView mImageCreateShortcut;

    @BindView(R.id.main_image_vibrate)
    ImageView mImageVibrate;

    @BindView(R.id.main_image_except_app)
    ImageView mImageExceptApp;

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mToolbarLayout;

    @BindView(R.id.main_card_double_tap)
    CardView mCardDoubleTap;

    @BindView(R.id.main_card_floating_popup)
    CardView mCardFloatingPopup;

    @BindView(R.id.main_card_create_shortcut)
    CardView mCardCreateShortcut;

    @BindView(R.id.main_card_vibrate)
    CardView mCardVibrate;

    @BindView(R.id.main_card_except_app)
    CardView mCardExceptApp;

    private boolean checkPower = true;
    private boolean checkDoubleTap = true;
    private boolean checkFloatingPopup = true;
    private boolean checkCreateShortcut = true;
    private boolean checkVibrate = true;
    private boolean checkExceptApp = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        setPermission();
    }

    private void setPermission() {
        mAdminComponent = new ComponentName(MainActivity.this, ScreenOffAdminReceiver.class);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (!mDevicePolicyManager.isAdminActive(mAdminComponent)) {
            //ADMIN PERMISSION
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminComponent);
            startActivityForResult(intent, REQUEST_ENABLE);

            //OVERLAY PERMISSION
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }

            //USAGE_ACCESS_SETTINGS PERMISSION
            Intent usageAccessIntent = new Intent( Settings.ACTION_USAGE_ACCESS_SETTINGS );
            usageAccessIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity( usageAccessIntent );
        }
    }

   @OnClick(R.id.main_image_power)
   void onClickPower(View view) {
        Intent intent = new Intent(MainActivity.this, SensorService.class);

        if (checkPower == false) {
            stopService(intent);
            Utils.SENSOR_SERVICE = false;

            mImagePower.setBackgroundResource(R.drawable.circle_background_white);
            mImagePower.setImageResource(R.mipmap.ic_power_orange);
            mToolbarLayout.setAlpha((float) 0.7);
            mSwitchPower.setChecked(false);
            checkPower = true;

        }
        else {
            startService(intent);
            Utils.SENSOR_SERVICE = true;

            mImagePower.setBackgroundResource(R.drawable.circle_background_orange);
            mImagePower.setImageResource(R.mipmap.ic_power_white);
            mToolbarLayout.setAlpha((float) 1);
            mSwitchPower.setChecked(true);
            checkPower = false;
        }
   }

   @OnClick(R.id.main_image_vibrate)
   void onClickVibrate(View view) {

        if (checkVibrate == false) {
            OnOffScreen.isVibrator = false;

            mImageVibrate.setBackgroundResource(R.drawable.circle_background_yello_white);
            mImageVibrate.setColorFilter(R.color.yello);
            mCardVibrate.setAlpha((float) 0.5);
            checkVibrate = true;
        }
        else {
            OnOffScreen.isVibrator = true;

            mImageVibrate.setBackgroundResource(R.drawable.circle_background_yello);
            mImageVibrate.setColorFilter(Color.WHITE);
            mCardVibrate.setAlpha((float) 1);
            checkVibrate = false;
        }
   }

    @OnClick(R.id.main_image_create_shortcut)
    void onClickCreateShortcut(View view) {
        if (checkCreateShortcut == false) {
            mImageCreateShortcut.setBackgroundResource(R.drawable.circle_background_green_white);
            mImageCreateShortcut.setColorFilter(R.color.green);
            mCardCreateShortcut.setAlpha((float) 0.5);
            checkCreateShortcut = true;
        }
        else {
            createShortcutIcon();

            mImageCreateShortcut.setBackgroundResource(R.drawable.circle_background_green);
            mImageCreateShortcut.setColorFilter(Color.WHITE);
            mCardCreateShortcut.setAlpha((float) 1);
            checkCreateShortcut = false;
        }
   }

    @OnClick(R.id.main_image_floating_popup)
    void onClickFlaotingPopup(View view) {
        Intent intent = new Intent(MainActivity.this, FloatingPopupService.class);

        if (checkFloatingPopup == false) {
            stopService(intent);
            Utils.FLOATING_SERVICE = false;

            mImageFloatingPopup.setBackgroundResource(R.drawable.circle_background_pink_white);
            mImageFloatingPopup.setColorFilter(R.color.pink);
            mCardFloatingPopup.setAlpha((float) 0.5);
            checkFloatingPopup = true;
        }
        else {
            startService(intent);
            Utils.FLOATING_SERVICE = true;

            mImageFloatingPopup.setBackgroundResource(R.drawable.circle_background_pink);
            mImageFloatingPopup.setColorFilter(Color.WHITE);
            mCardFloatingPopup.setAlpha((float) 1);
            checkFloatingPopup = false;
        }
   }

    @OnClick(R.id.main_image_double_tap)
    void onClickDoubleTap(View view) {
        Intent intent = new Intent(MainActivity.this, TouchService.class);

        if (checkDoubleTap == false) {
            stopService(intent);
            Utils.TOUCH_SERVICE = false;

            mImageDoubleTap.setBackgroundResource(R.drawable.circle_background_purple_white);
            mImageDoubleTap.setColorFilter(R.color.deeppurple);
            mCardDoubleTap.setAlpha((float) 0.5);
            checkDoubleTap = true;
        }
        else {
            startService(intent);
            Utils.TOUCH_SERVICE = true;

            mImageDoubleTap.setBackgroundResource(R.drawable.circle_background_purple);
            mImageDoubleTap.setColorFilter(Color.WHITE);
            mCardDoubleTap.setAlpha((float) 1);
            checkDoubleTap = false;
        }
   }

    @OnClick(R.id.main_image_except_app)
    void onClickExceptApp(View view) {

        Intent intent = new Intent(this, ExceptAppService.class);

        if (checkExceptApp == false) {
            stopService(intent);

            mImageExceptApp.setBackgroundResource(R.drawable.circle_background_blue_white);
            mImageExceptApp.setColorFilter(R.color.blue);
            mCardExceptApp.setAlpha((float) 0.5);
            checkExceptApp = true;

        }
        else {
            startService(intent);
            chooseEceptApps();

            mImageExceptApp.setBackgroundResource(R.drawable.circle_background_blue);
            mImageExceptApp.setColorFilter(Color.WHITE);
            mCardExceptApp.setAlpha((float) 1);
            checkExceptApp = false;
        }
   }

    @OnCheckedChanged(R.id.main_switch_power)
    void onCheckedChangedPower(CompoundButton compoundButton, boolean b) {
        Intent intent = new Intent(MainActivity.this, SensorService.class);

        if (checkPower == false) {
            stopService(intent);
            Utils.SENSOR_SERVICE = false;

            mImagePower.setBackgroundResource(R.drawable.circle_background_white);
            mImagePower.setImageResource(R.mipmap.ic_power_orange);
            mToolbarLayout.setAlpha((float) 0.7);
            checkPower = true;

        }
        else {
            startService(intent);
            Utils.SENSOR_SERVICE = true;

            mImagePower.setBackgroundResource(R.drawable.circle_background_orange);
            mImagePower.setImageResource(R.mipmap.ic_power_white);
            mToolbarLayout.setAlpha((float) 1);
            checkPower = false;
        }
    }

   private void uninstallApp() {
        removePermission();
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + Utils.ID_APP));
        startActivity(intent);
   }

   private void removePermission() {
        mDevicePolicyManager.removeActiveAdmin(mAdminComponent);
   }


    private void chooseEceptApps() {
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        final ListView lv = view.findViewById(R.id.dialog_app_list);

        Utils.LISTAPP = getInstalledApps();
        loadData();
        ArrayAdapter<App> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Utils.LISTAPP);

        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        for (int i = 0; i < Utils.LISTAPP.size(); i++) {
            if (Utils.LISTAPP.get(i).getChoose())
                lv.setItemChecked(i, true);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
                App app = Utils.LISTAPP.get(position);
                app.setChoose(currentCheck);
                saveData();
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private List<App> getInstalledApps() {
        List<App> lstApps = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String appId = p.packageName;
                App app = new App(appName, appId);
                lstApps.add(app);
            }
        }
        return lstApps;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    public void saveData()  {
        mSharedPreferences = this.getSharedPreferences("LISTAPP", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        for (App app : Utils.LISTAPP) {
            mEditor.putBoolean(app.getId(), app.getChoose());
        }

        mEditor.apply();
    }

    private void loadData()  {
        SharedPreferences sharedPreferences= this.getSharedPreferences("LISTAPP", Context.MODE_PRIVATE);

        if(sharedPreferences!= null) {
            for (App app : Utils.LISTAPP) {
                app.setChoose(sharedPreferences.getBoolean(app.getId(), false));
            }
        }

    }

    private void createShortcutIcon(){

        Intent shortcutIntent = new Intent(getApplicationContext(), ShortcutActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Lock Screen");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_power_settings_new_black_48dp));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        getApplicationContext().sendBroadcast(addIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_ENABLE == requestCode) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_btn_language:

                break;

            case R.id.main_btn_remove_permission:
                removePermission();
                Toast.makeText(MainActivity.this, R.string.successful, Toast.LENGTH_SHORT).show();
                break;

            case R.id.main_btn_uninstall:
                uninstallApp();
                break;

            case R.id.language_vi:
                setLocale("vi");
                break;
            case R.id.language_en:
                setLocale("en");
                break;
            case R.id.language_es:
                setLocale("es");
                break;
            case R.id.language_pt:
                setLocale("pt");
                break;
            case R.id.language_th:
                setLocale("th");
                break;
            case R.id.language_ja:
                setLocale("ja");
                break;

        }
        return true;
    }


    public void setLocale(String lang) {
        mLocale = new Locale(lang);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = mLocale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        startActivity( new Intent(this, MainActivity.class));
    }
}

