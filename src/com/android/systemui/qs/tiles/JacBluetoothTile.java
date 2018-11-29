package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;

public class JacBluetoothTile extends QSTileImpl<QSTile.BooleanState> {
    public JacBluetoothTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        ComponentName toActivityBt = new ComponentName("com.jancar.settingss",
                "com.jancar.settings.view.activity.MainActivity");
        Intent intentBt = new Intent();
        intentBt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentBt.setComponent(toActivityBt);
        intentBt.putExtra("position", 1);
//        mContext.startActivity(intentBt);
        Dependency.get(ActivityStarter.class)
                .postStartActivityDismissingKeyguard(intentBt, 0);
//        jancarManager.requestPage("btset", intentBt);
//        makeExpandedInvisible();
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.jac_qs_bt_01);
        state.label = mContext.getResources().getString(R.string.qs_bt);
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
