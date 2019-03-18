package com.android.systemui.jancar;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.systemui.R;
import com.jancar.JancarManager;


public class JancarMenuDialog extends Dialog {

    private static JancarMenuDialog instance;


    public static JancarMenuDialog getInstance(Context context) {    //对获取实例的方法进行同步
        if (instance == null) {
            synchronized (JancarMenuDialog.class) {
                if (instance == null)
                    instance = new JancarMenuDialog(context);
            }
        }
        return instance;
    }

    private ImageView close_screen;
    private ImageView show_recents;
    private ImageView set_wallpager;

    private Handler mHander = new Handler(Looper.getMainLooper());
    private Runnable hideUI = new Runnable() {
        public void run() {
            dismiss();
        }
    };

    private JancarMenuDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();

        window.setGravity(Gravity.END | Gravity.BOTTOM);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        setContentView(R.layout.jancar_menu);

        mHander.postDelayed(hideUI, 3000);

        close_screen = findViewById(R.id.close_screen);
        show_recents = findViewById(R.id.show_recents);
        set_wallpager = findViewById(R.id.set_wallpager);

        close_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FlyLog.d("btn_close_screen: onClick");
                    JancarManager jancarManager = (JancarManager) getContext().getSystemService("jancar_manager");
                    jancarManager.requestDisplay(false);
                } catch (Exception e) {
                    FlyLog.e(e.toString());
                }
            }
        });

        show_recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName toActivityJancarmenu = new ComponentName("com.android.systemui",
                        "com.android.systemui.recents.RecentsActivity");
                Intent intentBrightness = new Intent();
                intentBrightness.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentBrightness.setComponent(toActivityJancarmenu);
                getContext().startActivity(intentBrightness);
                dismiss();
            }
        });

        set_wallpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName toActivityJancarmenu = new ComponentName("com.android.launcher3",
                        "com.android.launcher3.WallpaperPickerActivity");
                Intent intentBrightness = new Intent();
                intentBrightness.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentBrightness.setComponent(toActivityJancarmenu);
                getContext().startActivity(intentBrightness);
                dismiss();
            }
        });


    }

    @Override
    public void show() {
        super.show();
        if (isShowing()) {
            dismiss();
        } else {
            mHander.postDelayed(hideUI, 3000);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mHander.removeCallbacksAndMessages(null);
    }
}
