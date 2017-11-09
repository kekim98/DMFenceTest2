package com.dmlab.bawoori.dmlib.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

/**
 * Created by bawoori on 17. 11. 9.
 */

@Dao
public interface DMGeofenceDao {

    /**
     * Counts the number of dmgeofences in the table.
     *
     * @return The number of dmgeofences.
     */
    @Query("SELECT COUNT(*) FROM " + DMGeofence.TABLE_NAME)
    int count();

    /**
     * Inserts a dmgeofence into the table.
     *
     * @param dmgeofence A new dmgeofence.
     * @return The row ID of the newly inserted dmgeofence.
     */
    @Insert
    long insert(DMGeofence dmgeofence);

    /**
     * Inserts multiple dmgeofences into the database
     *
     * @param dmgeofences An array of new dmgeofences.
     * @return The row IDs of the newly inserted dmgeofences.
     */
    @Insert
    long[] insertAll(DMGeofence[] dmgeofences);

    /**
     * Select all dmgeofences.
     *
     * @return A {@link Cursor} of all the dmgeofences in the table.
     */
    @Query("SELECT * FROM " + DMGeofence.TABLE_NAME)
    Cursor selectAll();

    /**
     * Select a dmgeofence by the ID.
     *
     * @param id The row ID.
     * @return A {@link Cursor} of the selected dmgeofence.
     */
    @Query("SELECT * FROM " + DMGeofence.TABLE_NAME + " WHERE " + DMGeofence.COLUMN_ID + " = :id")
    Cursor selectById(long id);

    /**
     * Delete a dmgeofence by the ID.
     *
     * @param id The row ID.
     * @return A number of dmgeofences deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + DMGeofence.TABLE_NAME + " WHERE " + DMGeofence.COLUMN_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the dmgeofence. The dmgeofence is identified by the row ID.
     *
     * @param dmgeofence The dmgeofence to update.
     * @return A number of dmgeofences updated. This should always be {@code 1}.
     */
    @Update
    int update(DMGeofence dmgeofence);
}
