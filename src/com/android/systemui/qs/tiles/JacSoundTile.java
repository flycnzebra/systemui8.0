package com.android.systemui.qs.tiles;

import android.content.ComponentName;
import android.content.Intent;

import com.android.systemui.R;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;

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
        ComponentName toActivityCarsetting = new ComponentName("com.jancar.player.music", "com.jancar.player.music.MusicActivity");
        Intent intentCarsetting = new Intent();
        intentCarsetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentCarsetting.setComponent(toActivityCarsetting);
        mContext.startActivity(intentCarsetting);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.jac_qs_sound_01);
        state.label = "jancar1";
        state.contentDescription = "jancar2";
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
        return "jancar3";
    }
}