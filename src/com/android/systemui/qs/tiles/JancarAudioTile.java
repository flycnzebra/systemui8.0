package com.android.systemui.qs.tiles;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;

import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.R;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.jancar.JancarManager;

import static com.android.internal.logging.nano.MetricsProto.MetricsEvent.ACTION_QS_MORE_SETTINGS;

public class JancarAudioTile extends QSTileImpl<QSTile.BooleanState> {
    public JancarAudioTile(QSHost host) {
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
        ComponentName toActivityAudio = new ComponentName("com.jancar.settingss", "com.jancar.settings.view.activity.MainActivity");
        Intent intentAduio = new Intent();
        intentAduio.setComponent(toActivityAudio);
        intentAduio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentAduio.putExtra("position", 3);
        @SuppressLint("WrongConstant")
        JancarManager jancarManager = (JancarManager) mContext.getSystemService("jancar_manager");
        jancarManager.requestPage("eq", intentAduio);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.icon = ResourceIcon.get(R.drawable.jac_qs_audio_01);
        state.label = mContext.getResources().getString(R.string.qs_volume_mode);
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
