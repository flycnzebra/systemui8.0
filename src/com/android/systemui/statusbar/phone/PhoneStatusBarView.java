/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.systemui.statusbar.phone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import android.os.SystemProperties;

import com.android.systemui.Dependency;
import com.android.systemui.EventLogTags;
import com.android.systemui.R;
import com.android.systemui.jancar.FlyLog;
import com.android.systemui.jancar.JancarMenuDialog;
import com.android.systemui.statusbar.policy.DarkIconDispatcher;
import com.android.systemui.statusbar.policy.DarkIconDispatcher.DarkReceiver;
import com.jancar.JancarManager;

public class PhoneStatusBarView extends PanelBar {
    private static final String TAG = "PhoneStatusBarView";
    private static final boolean DEBUG = StatusBar.DEBUG;
    private static final boolean DEBUG_GESTURES = false;

    StatusBar mBar;

    boolean mIsFullyOpenedPanel = false;
    private final PhoneStatusBarTransitions mBarTransitions;
    private ScrimController mScrimController;
    private float mMinFraction;
    private float mPanelFraction;
    private Runnable mHideExpandedRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPanelFraction == 0.0f) {
                mBar.makeExpandedInvisible();
            }
        }
    };
    private DarkReceiver mBattery;
    private ImageView mHome;
    private ImageView mBack;
    private ImageView mRecent;
    private ImageView mClose;
    private ImageView mJacMenu;
    private ImageView mJacDvd;

    public PhoneStatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBarTransitions = new PhoneStatusBarTransitions(this);
    }

    public BarTransitions getBarTransitions() {
        return mBarTransitions;
    }

    public void setBar(StatusBar bar) {
        mBar = bar;
    }

    public void setScrimController(ScrimController scrimController) {
        mScrimController = scrimController;
    }

    @Override
    public void onFinishInflate() {
        mBarTransitions.init();
        mBattery = findViewById(R.id.battery);

        if (atcEnhancementSupport()) {
            mHome = findViewById(R.id.home);
            mBack = findViewById(R.id.back);
            mRecent = findViewById(R.id.recent_apps007);
            mClose = findViewById(R.id.close_screen);
            mClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    FlyLog.d();
                    try {
                        if (DEBUG) Log.v(TAG, "btn_close_screen: onClick");
                        JancarManager jancarManager = (JancarManager) getContext().getSystemService("jancar_manager");
                        jancarManager.requestDisplay(false);
                    } catch (Exception e) {
                        FlyLog.e(e.toString());
                    }
                }
            });
            mJacMenu = findViewById(R.id.jancar_menu);
            mJacMenu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ComponentName toActivityJancarmenu = new ComponentName("com.android.systemui",
//                            "com.android.systemui.jancar.JancarMenu");
//                    Intent intentBrightness = new Intent();
//                    intentBrightness.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intentBrightness.setComponent(toActivityJancarmenu);
//                    getContext().startActivity(intentBrightness);
                    JancarMenuDialog.getInstance(getContext()).show();
                }
            });

            mJacDvd = findViewById(R.id.jancar_dvd);
            int showDvD = SystemProperties.getInt("ro.product.support.dvd", 1);
            if (showDvD == 1) {
                mJacDvd.setVisibility(VISIBLE);
            } else {
                mJacDvd.setVisibility(GONE);
            }
            mJacDvd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((JancarManager) getContext().getSystemService(JancarManager.JAC_SERVICE)).simulateKey(0x08);
                        FlyLog.d("jancarmanage simulateKey 0x08");
                    } catch (Exception e) {
                        FlyLog.e(e.toString());
                    }
                }
            });
            mJacDvd.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        ((JancarManager) getContext().getSystemService(JancarManager.JAC_SERVICE)).simulateKey(0x48);
                        FlyLog.d("jancarmanage simulateKey 0x48");
                    } catch (Exception e) {
                        FlyLog.e(e.toString());
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Always have Battery meters in the status bar observe the dark/light modes.
        Dependency.get(DarkIconDispatcher.class).addDarkReceiver(mBattery);
        if (atcEnhancementSupport()) {
            Dependency.get(DarkIconDispatcher.class).addDarkReceiver(mHome);
            Dependency.get(DarkIconDispatcher.class).addDarkReceiver(mBack);
            Dependency.get(DarkIconDispatcher.class).addDarkReceiver(mRecent);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mClose != null) {
            mClose.setOnClickListener(null);
        }
        Dependency.get(DarkIconDispatcher.class).removeDarkReceiver(mBattery);
        if (atcEnhancementSupport()) {
            Dependency.get(DarkIconDispatcher.class).removeDarkReceiver(mHome);
            Dependency.get(DarkIconDispatcher.class).removeDarkReceiver(mBack);
            Dependency.get(DarkIconDispatcher.class).removeDarkReceiver(mRecent);
        }
    }

    @Override
    public boolean panelEnabled() {
        return mBar.panelsEnabled();
    }

    @Override
    public boolean onRequestSendAccessibilityEventInternal(View child, AccessibilityEvent event) {
        if (super.onRequestSendAccessibilityEventInternal(child, event)) {
            // The status bar is very small so augment the view that the user is touching
            // with the content of the status bar a whole. This way an accessibility service
            // may announce the current item as well as the entire content if appropriate.
            AccessibilityEvent record = AccessibilityEvent.obtain();
            onInitializeAccessibilityEvent(record);
            dispatchPopulateAccessibilityEvent(record);
            event.appendRecord(record);
            return true;
        }
        return false;
    }

    @Override
    public void onPanelPeeked() {
        super.onPanelPeeked();
        mBar.makeExpandedVisible(false);
    }

    @Override
    public void onPanelCollapsed() {
        super.onPanelCollapsed();
        // Close the status bar in the next frame so we can show the end of the animation.
        post(mHideExpandedRunnable);
        mIsFullyOpenedPanel = false;
    }

    public void removePendingHideExpandedRunnables() {
        removeCallbacks(mHideExpandedRunnable);
    }

    @Override
    public void onPanelFullyOpened() {
        super.onPanelFullyOpened();
        if (!mIsFullyOpenedPanel) {
            mPanel.sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
        }
        mIsFullyOpenedPanel = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean barConsumedEvent = mBar.interceptTouchEvent(event);

        if (DEBUG_GESTURES) {
            if (event.getActionMasked() != MotionEvent.ACTION_MOVE) {
                EventLog.writeEvent(EventLogTags.SYSUI_PANELBAR_TOUCH,
                        event.getActionMasked(), (int) event.getX(), (int) event.getY(),
                        barConsumedEvent ? 1 : 0);
            }
        }

        return barConsumedEvent || super.onTouchEvent(event);
    }

    @Override
    public void onTrackingStarted() {
        super.onTrackingStarted();
        mBar.onTrackingStarted();
        mScrimController.onTrackingStarted();
        removePendingHideExpandedRunnables();
    }

    @Override
    public void onClosingFinished() {
        super.onClosingFinished();
        mBar.onClosingFinished();
    }

    @Override
    public void onTrackingStopped(boolean expand) {
        super.onTrackingStopped(expand);
        mBar.onTrackingStopped(expand);
    }

    @Override
    public void onExpandingFinished() {
        super.onExpandingFinished();
        mScrimController.onExpandingFinished();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mBar.interceptTouchEvent(event) || super.onInterceptTouchEvent(event);
    }

    @Override
    public void panelScrimMinFractionChanged(float minFraction) {
        if (mMinFraction != minFraction) {
            mMinFraction = minFraction;
            updateScrimFraction();
        }
    }

    @Override
    public void panelExpansionChanged(float frac, boolean expanded) {
        super.panelExpansionChanged(frac, expanded);
        mPanelFraction = frac;
        updateScrimFraction();
    }

    private void updateScrimFraction() {
        float scrimFraction = mPanelFraction;
        if (mMinFraction < 1.0f) {
            scrimFraction = Math.max((mPanelFraction - mMinFraction) / (1.0f - mMinFraction),
                    0);
        }
        mScrimController.setPanelExpansion(scrimFraction);
    }

    public void onDensityOrFontScaleChanged() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelSize(
                R.dimen.status_bar_height);
        setLayoutParams(layoutParams);
    }

    private static boolean atcEnhancementSupport() {
        return SystemProperties.getBoolean("ro.atc.aosp_enhancement", false);
    }
}
