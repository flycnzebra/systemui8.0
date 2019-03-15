package com.android.systemui.jancar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.systemui.R;
import com.jancar.JancarManager;

/**
 * A dialog that provides controls for adjusting the screen brightness.
 */
public class JancarMenu extends Activity {
    private Handler mHander = new Handler(Looper.getMainLooper());
    private Runnable hideUI = new Runnable() {
        public void run() {
            finish();
        }
    };

    private ImageView close_screen;
    private ImageView show_recents;
    private ImageView set_wallpager;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window mWindow = getWindow();

        mWindow.setGravity(Gravity.END|Gravity.BOTTOM);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.jancar_menu);

        close_screen = findViewById(R.id.close_screen);
        show_recents = findViewById(R.id.show_recents);
        set_wallpager = findViewById(R.id.set_wallpager);
        view = findViewById(R.id.rootview);

        close_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FlyLog.d("btn_close_screen: onClick");
                    JancarManager jancarManager = (JancarManager)getSystemService("jancar_manager");
                    jancarManager.requestDisplay(false);
                } catch (Exception e) {
                    FlyLog.e(e.toString());
                }
            }
        });

        show_recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName toActivityJancarmenu = new ComponentName( "com.android.systemui",
                        "com.android.systemui.recents.RecentsActivity");
                Intent intentBrightness = new Intent();
                intentBrightness.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentBrightness.setComponent(toActivityJancarmenu);
                startActivity(intentBrightness);
                finish();
            }
        });

        set_wallpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName toActivityJancarmenu = new ComponentName( "com.android.launcher3",
                        "com.android.launcher3.WallpaperPickerActivity");
                Intent intentBrightness = new Intent();
                intentBrightness.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentBrightness.setComponent(toActivityJancarmenu);
                startActivity(intentBrightness);
                finish();
            }
        });

        mHander.postDelayed(hideUI, 3000);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mHander.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
