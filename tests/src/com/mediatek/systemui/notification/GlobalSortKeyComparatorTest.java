/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.mediatek.systemui.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.android.server.notification.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class GlobalSortKeyComparatorTest extends NotificationTestCase {

    private final String PKG = "PKG";
    private final int UID = 1111111;
    private static final String TEST_CHANNEL_ID = "test_channel_id";

    @Test
    public void testComparator() throws Exception {
        Notification n = new Notification.Builder(getContext(), TEST_CHANNEL_ID)
                .build();
        NotificationRecord left = new NotificationRecord(getContext(),
                new StatusBarNotification(PKG,
                        PKG, 1, "media", UID, UID, n,
                        new UserHandle(UserHandle.myUserId()),
                        "", 1499), getDefaultChannel());
        left.setGlobalSortKey("first");

        NotificationRecord right = new NotificationRecord(getContext(),
                new StatusBarNotification(PKG,
                        PKG, 1, "media", UID, UID, n,
                        new UserHandle(UserHandle.myUserId()),
                        "", 1499), getDefaultChannel());
        right.setGlobalSortKey("second");

        final List<NotificationRecord> expected = new ArrayList<>();
        expected.add(left);
        expected.add(right);

        List<NotificationRecord> actual = new ArrayList<>();
        actual.addAll(expected);
        Collections.shuffle(actual);

        Collections.sort(actual, new GlobalSortKeyComparator());

        assertEquals(expected, actual);
    }

    @Test
    public void testNoCrash_leftNull() throws Exception {
        Notification n = new Notification.Builder(getContext(), TEST_CHANNEL_ID)
                .build();
        NotificationRecord left = new NotificationRecord(getContext(),
                new StatusBarNotification(PKG,
                        PKG, 1, "media", UID, UID, n,
                        new UserHandle(UserHandle.myUserId()),
                        "", 1499), getDefaultChannel());
        left.setGlobalSortKey("left");

        NotificationRecord right = new NotificationRecord(getContext(),
                new StatusBarNotification(PKG,
                        PKG, 1, "media", UID, UID, n,
                        new UserHandle(UserHandle.myUserId()),
                        "", 1499), getDefaultChannel());
        right.setGlobalSortKey("not null");

        final List<NotificationRecord> expected = new ArrayList<>();
        expected.add(left);
        expected.add(right);

        List<NotificationRecord> actual = new ArrayList<>();
        actual.addAll(expected);
        Collections.shuffle(actual);

        Collections.sort(actual, new GlobalSortKeyComparator());

        assertEquals(expected, actual);
    }

    @Test
    public void testNoCrash_rightNull() throws Exception {
        Notification n = new Notification.Builder(getContext(), TEST_CHANNEL_ID)
                .build();
        NotificationRecord left = new NotificationRecord(getContext(),
                new StatusBarNotification(PKG,
                        PKG, 1, "media", UID, UID, n,
                        new UserHandle(UserHandle.myUserId()),
                        "", 1499), getDefaultChannel());
        left.setGlobalSortKey("not null");

        NotificationRecord right = new NotificationRecord(getContext(),
                new StatusBarNotification(PKG,
                        PKG, 1, "media", UID, UID, n,
                        new UserHandle(UserHandle.myUserId()),
                        "", 1499), getDefaultChannel());
        right.setGlobalSortKey("right");

        final List<NotificationRecord> expected = new ArrayList<>();
        expected.add(left);
        expected.add(right);

        List<NotificationRecord> actual = new ArrayList<>();
        actual.addAll(expected);
        Collections.shuffle(actual);

        Collections.sort(actual, new GlobalSortKeyComparator());

        assertEquals(expected, actual);
    }

    private NotificationChannel getDefaultChannel() {
        return new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, "name",
                NotificationManager.IMPORTANCE_LOW);
    }
}
