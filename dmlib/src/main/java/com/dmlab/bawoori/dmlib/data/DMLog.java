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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;


/**
 * Represents one record of the DMLog table.
 */
@Entity(tableName = DMLog.TABLE_NAME)
public class DMLog {

    /** The name of the DMLog table. */
    public static final String TABLE_NAME = "t_dm_log";

    /** The name of the ID column. */
    public static final String COLUMN_ID = BaseColumns._ID;

    public static final String COLUMN_DM_ID = "fence_id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_REG_TIME = "reg_time";
    /** The name of the name column. */

    /** The unique ID of the cheese. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    /** The name of the cheese. */
    @NonNull
    public String fence_id;

    @NonNull
    public Double latitude;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getFence_id() {
        return fence_id;
    }

    public void setFence_id(@NonNull String fence_id) {
        this.fence_id = fence_id;
    }

    @NonNull
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull Double latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull Double longitude) {
        this.longitude = longitude;
    }

    public long getReg_time() {
        return reg_time;
    }

    public void setReg_time(long reg_time) {
        this.reg_time = reg_time;
    }

    @NonNull

    public Double longitude;

    public long reg_time;

    /**
     * Create a new {@link DMLog} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues} that at least contain {@link #COLUMN_DM_ID}.
     * @return A newly created {@link DMLog} instance.
     */
    public static DMLog fromContentValues(ContentValues values) {
        final DMLog dmLog = new DMLog();
        if (values.containsKey(COLUMN_ID)) {
            dmLog.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_DM_ID)) {
            dmLog.fence_id = values.getAsString(COLUMN_DM_ID);
        }
        if (values.containsKey(COLUMN_LATITUDE)){
            dmLog.latitude = values.getAsDouble(COLUMN_LATITUDE);
        }
        if (values.containsKey(COLUMN_LONGITUDE)){
            dmLog.longitude = values.getAsDouble(COLUMN_LONGITUDE);
        }
        if (values.containsKey(COLUMN_REG_TIME)){
            dmLog.reg_time = values.getAsLong(COLUMN_REG_TIME);
        }
        return dmLog;
    }



}
