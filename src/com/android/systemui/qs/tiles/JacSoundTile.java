package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;

import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.phone.StatusBar;

public class JacSoundTile extends QSTileImpl<QSTile.BooleanState> {
    public JacSoundTile(QSHost host) {
        super(host);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        Dependency.get(ActivityStarter.class).postQSRunnableDismissingKeyguard(this::showDialog);
//        makeExpandedInvisible();
    }

    private void showDialog(){
        Intent intent = new Intent();
        intent.setAction(VolumeDialogController.BROADCAST_SHOW_VOLUME_BAR);
        mContext.sendBroadcast(intent);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.jac_qs_sound_01);
        state.label = mContext.getResources().getString(R.string.qs_volume_value);
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
