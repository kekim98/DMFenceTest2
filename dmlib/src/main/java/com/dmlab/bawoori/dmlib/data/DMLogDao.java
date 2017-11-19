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

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;


/**
 * Data access object for DMLog.
 */
@Dao
public interface DMLogDao {

    /**
     * Counts the number of cheeses in the table.
     *
     * @return The number of cheeses.
     */
    @Query("SELECT COUNT(*) FROM " + DMLog.TABLE_NAME)
    int count();

    /**
     * Inserts a DMLog into the table.
     *
     * @param dmLog A new DMLog.
     * @return The row ID of the newly inserted DMLog.
     */
    @Insert
    long insert(DMLog dmLog);

    /**
     * Inserts multiple DMLogs into the database
     *
     * @param dmLogs An array of new DMLogs.
     * @return The row IDs of the newly inserted DMLogs.
     */
    @Insert
    long[] insertAll(DMLog[] dmLogs);

    /**
     * Select all cheeses.
     *
     * @return A {@link Cursor} of all the cheeses in the table.
     */
    @Query("SELECT * FROM " + DMLog.TABLE_NAME)
    Cursor selectAll();

    /**
     * Select a cheese by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected cheese.
     */
    @Query("SELECT * FROM " + DMLog.TABLE_NAME + " WHERE " + DMLog.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    @Query("SELECT * FROM " + DMLog.TABLE_NAME + " WHERE " + DMLog.COLUMN_DM_ID + " = :id")
    Cursor selectByFenceId(String id);

    /**
     * Delete a cheese by the ID.
     *
     * @param id The row ID.
     * @return A number of cheeses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + DMLog.TABLE_NAME + " WHERE " + DMLog.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the DMLog. The DMLog is identified by the row ID.
     *
     * @param dmLog The DMLog to update.
     * @return A number of cheeses updated. This should always be {@code 1}.
     */
    @Update
    int update(DMLog dmLog);

    @Query("UPDATE " +DMLog.TABLE_NAME + " SET "+DMLog.COLUMN_DM_ID + "= :id" + " WHERE " + DMLog.COLUMN_DM_ID + "='UNKNOWN'")
    int updateUnknown(String id);

    @Query("DELETE FROM " + DMLog.TABLE_NAME)
    int deleteAll();
}
