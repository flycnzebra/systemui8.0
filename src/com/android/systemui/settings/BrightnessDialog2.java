/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.systemui.R;

/**
 * A dialog that provides controls for adjusting the screen brightness.
 */
public class BrightnessDialog2 extends Dialog {

    private static BrightnessDialog2 instance;


    public static BrightnessDialog2 getInstance(Context context) {    //对获取实例的方法进行同步
        if (instance == null) {
            synchronized (BrightnessDialog2.class) {
                if (instance == null)
                    instance = new BrightnessDialog2(context);
            }
        }
        return instance;
    }

    private BrightnessController mBrightnessController;
    private boolean isStop = true;

    private Handler mHander = new Handler() {
        public void handleMessage(Message m) {
            super.handleMessage(m);
            switch (m.what) {
                case 1:
                    if (!isStop) {
                        removeCallbacks(hideUI);
                        postDelayed(hideUI, 3000);
                    }
                    break;
            }
        }
    };
    private Runnable hideUI = new Runnable() {
        public void run() {
            dismiss();
        }
    };

    public BrightnessDialog2(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window mWindow = getWindow();

//        mWindow.setGravity(Gravity.CENTER);
//        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
//        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mWindow.setType(WindowManager.LayoutParams.TYPE_VOLUME_OVERLAY);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        // Use a dialog theme as the activity theme, but inflate the content as
        // the QS content.
//        ContextThemeWrapper themedContext = new ContextThemeWrapper(this,
//                com.android.internal.R.style.Theme_DeviceDefault_QuickSettings);
//        View v = LayoutInflater.from(themedContext).inflate(
//                R.layout.quick_settings_brightness_dialog, null);
        setContentView(R.layout.quick_settings_brightness_dialog);

        final ImageView icon = findViewById(R.id.brightness_icon);
        final ToggleSliderView slider = findViewById(R.id.brightness_slider);
        mBrightnessController = new BrightnessController(getContext(), icon, slider);
    }


    @Override
    public void show() {
        super.show();
        mBrightnessController.setUIHandler(mHander);
        mHander.postDelayed(hideUI, 3000);
        isStop = false;
        mBrightnessController.registerCallbacks();
        MetricsLogger.visible(getContext(), MetricsEvent.BRIGHTNESS_DIALOG);

    }

    @Override
    public void dismiss() {
        super.dismiss();
        isStop = true;
        MetricsLogger.hidden(getContext(), MetricsEvent.BRIGHTNESS_DIALOG);
        mBrightnessController.unregisterCallbacks();
        mBrightnessController.setUIHandler(null);
        mHander.removeCallbacksAndMessages(null);
    }


}
