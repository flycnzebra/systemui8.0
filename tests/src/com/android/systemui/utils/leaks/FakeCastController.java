/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.systemui.utils.leaks;

import android.testing.LeakCheck;

import android.net.wifi.p2p.WifiP2pDevice;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.CastController.Callback;

import java.util.Set;

public class FakeCastController extends BaseLeakChecker<Callback> implements CastController {
    public FakeCastController(LeakCheck test) {
        super(test, "cast");
    }

    @Override
    public void setDiscovering(boolean request) {

    }

    @Override
    public void setCurrentUserId(int currentUserId) {

    }

    @Override
    public Set<CastDevice> getCastDevices() {
        return null;
    }

    @Override
    public void startCasting(CastDevice device) {

    }

    @Override
    public void stopCasting(CastDevice device) {

    }

    /// M: WFD sink support {@
    @Override
    public boolean isWfdSinkSupported() {
        return false;
    }

    @Override
    public boolean isNeedShowWfdSink(){
        return false;
    }

    @Override
    public void updateWfdFloatMenu(boolean start){

    }

    @Override
    public WifiP2pDevice getWifiP2pDev(){
        return null;
    }

    @Override
    public void setListening(boolean listening){

    }
    /// @}
}
