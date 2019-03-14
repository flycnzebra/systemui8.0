package com.android.systemui.jancar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();

        window.setGravity(Gravity.END|Gravity.TOP);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.jancar_menu);

        close_screen = findViewById(R.id.close_screen);
        show_recents = findViewById(R.id.show_recents);
        set_wallpager = findViewById(R.id.set_wallpager);

        close_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FlyLog.d("btn_close_screen: onClick");
                    JancarManager jancarManager = (JancarManager)getSystemService("jancar_manager");
                    jancarManager.requestDisplay(false);
                    mHander.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1000);
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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        WindowManager manager = getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        lp.y = width-lp.width;
        lp.x = 56;
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
