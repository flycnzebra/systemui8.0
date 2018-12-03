package com.android.systemui.qs.tiles;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;

import com.android.systemui.Dependency;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.R;
import com.jancar.JancarManager;

public class JacCarSettingTile extends QSTileImpl<QSTile.BooleanState> {
    public JacCarSettingTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        Dependency.get(ActivityStarter.class).postQSRunnableDismissingKeyguard(this::showDialog);
    }

    private void showDialog(){
        ComponentName toActivityCarsetting = new ComponentName("com.jancar.settingss", "com.jancar.settings.view.activity.MainActivity");
        Intent intentCarsetting = new Intent();
        intentCarsetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentCarsetting.putExtra("position", 6);
        intentCarsetting.setComponent(toActivityCarsetting);
        @SuppressLint("WrongConstant")
        JancarManager jancarManager = (JancarManager) mContext.getSystemService("jancar_manager");
        jancarManager.requestPage("eq", intentCarsetting);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.jac_qs_car_01);
        state.label = mContext.getResources().getString(R.string.qs_carsetting);
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
