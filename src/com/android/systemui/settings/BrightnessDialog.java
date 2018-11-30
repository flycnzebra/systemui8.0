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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
public class BrightnessDialog extends Activity {

    private BrightnessController mBrightnessController;

    private boolean dismiss = true;
    private Handler mHander = new Handler() {
        public void handleMessage(Message m) {
            super.handleMessage(m);
            switch (m.what) {
                case 1:
                    dismiss = false;
                    break;
            }
        }
    };
    private Runnable r = new Runnable() {
        public void run() {
            if (dismiss) {
                finish();
            }
            dismiss = true;
            mHander.postDelayed(r, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = getWindow();

//        window.setGravity(Gravity.CENTER);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.requestFeature(Window.FEATURE_NO_TITLE | Window.FEATURE_CONTENT_TRANSITIONS | Window.FEATURE_ACTIVITY_TRANSITIONS);

        // Use a dialog theme as the activity theme, but inflate the content as
        // the QS content.
        ContextThemeWrapper themedContext = new ContextThemeWrapper(this,
                com.android.internal.R.style.Theme_DeviceDefault_QuickSettings);
        View v = LayoutInflater.from(themedContext).inflate(
                R.layout.quick_settings_brightness_dialog, null);
        setContentView(R.layout.quick_settings_brightness_dialog);

        final ImageView icon = findViewById(R.id.brightness_icon);
        final ToggleSliderView slider = findViewById(R.id.brightness_slider);
        mBrightnessController = new BrightnessController(this, icon, slider, mHander);
        mHander.postDelayed(r, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBrightnessController.registerCallbacks();
        MetricsLogger.visible(this, MetricsEvent.BRIGHTNESS_DIALOG);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MetricsLogger.hidden(this, MetricsEvent.BRIGHTNESS_DIALOG);
        mBrightnessController.unregisterCallbacks();
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
}
