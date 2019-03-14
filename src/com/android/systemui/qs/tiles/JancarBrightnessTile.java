package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;

public class JancarBrightnessTile extends QSTileImpl<QSTile.BooleanState> {
    public JancarBrightnessTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        ComponentName toActivityBrightness = new ComponentName("com.android.systemui", "com.android.systemui.settings.BrightnessDialog");
        Intent intentBrightness = new Intent();
        intentBrightness.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentBrightness.setComponent(toActivityBrightness);
//        mContext.startActivity(intentCarsetting);
        Dependency.get(ActivityStarter.class)
                .postStartActivityDismissingKeyguard(intentBrightness, 0);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.jac_qs_brightness_01);
        state.label = mContext.getResources().getString(R.string.qs_brightness);
        state.contentDescription = state.label;
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }

    @Override
    public Intent getLongClickIntent() {
        return null;
    }

    @Override
    protected void handleSetListening(boolean listening) {

    }

    @Override
    public CharSequence getTileLabel() {
        return "";
    }
}
