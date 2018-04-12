package com.skyfree.magiclookscreen;

import android.app.Activity;
import android.content.Intent;

import com.skyfree.magiclookscreen.service.ShortcutService;

public class ShortcutActivity extends Activity {

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(ShortcutActivity.this, ShortcutService.class);
        startService(intent);
        stopService(intent);
        finish();
    }
}
