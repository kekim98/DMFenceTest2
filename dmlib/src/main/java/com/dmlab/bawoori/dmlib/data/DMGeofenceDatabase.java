/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.dmlab.bawoori.dmlib.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.VisibleForTesting;


/**
 * The Room database.
 */
@Database(entities = {DMLog.class, DMGeofence.class}, version = 1)
public abstract class DMGeofenceDatabase extends RoomDatabase {

    /**
     * @return The DAO for the DMLog table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract DMLogDao dmLogDao();
    public abstract DMGeofenceDao dmGeofenceDao();

    /** The only instance */
    private static DMGeofenceDatabase sInstance;

    /**
     * Gets the singleton instance of DMGeofenceDatabase.
     *
     * @param context The context.
     * @return The singleton instance of DMGeofenceDatabase.
     */
    public static synchronized DMGeofenceDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), DMGeofenceDatabase.class, "ex")
                    .build();
         //   sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                DMGeofenceDatabase.class).build();
    }



}
