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

    @Query("SELECT * FROM " + DMGeofence.TABLE_NAME + " WHERE " + DMGeofence.COLUMN_TRANSITION_TYPE +
            "=" +DMGeofence.TRANS_EXIT + " AND "
            + "("+DMGeofence.COLUMN_IS_START_JOB + "=" + DMGeofence.JOB_NOT_DONE +" OR "
            + DMGeofence.COLUMN_IS_STOP_JOB + "=" + DMGeofence.JOB_NOT_DONE + ")" + " LIMIT 1")
    Cursor selectOneKnownTrans();

    @Query("SELECT * FROM " + DMGeofence.TABLE_NAME + " WHERE " + DMGeofence.COLUMN_TRANSITION_TYPE +
            "=" + DMGeofence.TRANS_UNKNOWN)
    Cursor selectAllUnknownTrans();

    @Query("SELECT * FROM " + DMGeofence.TABLE_NAME + " WHERE " + DMGeofence.COLUMN_TRANSITION_TYPE +
            "=" + DMGeofence.TRANS_ENTER)
    Cursor selectAllEnterTrans();

    /**
     * Delete a dmgeofence by the ID.
     *
     * @param id The row ID.
     * @return A number of dmgeofences deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + DMGeofence.TABLE_NAME + " WHERE " + DMGeofence.COLUMN_ID + " = :id")
    int deleteById(long id);

    @Query("DELETE FROM " + DMGeofence.TABLE_NAME)
    int deleteAll();

    /**
     * Update the dmgeofence. The dmgeofence is identified by the row ID.
     *
     * @param dmgeofence The dmgeofence to update.
     * @return A number of dmgeofences updated. This should always be {@code 1}.
     */
    @Update
    int update(DMGeofence dmgeofence);

    @Query("UPDATE " + DMGeofence.TABLE_NAME + " SET " + DMGeofence.COLUMN_TRANSITION_TYPE + "= :type"
            + " WHERE " + DMGeofence.COLUMN_ID + " = :id" )
    int updateTransType(long id, int type);


    @Query("UPDATE " + DMGeofence.TABLE_NAME + " SET " + DMGeofence.COLUMN_IS_START_JOB + "= :v"
            + " WHERE " + DMGeofence.COLUMN_NAME + " = :d" )
    int updateIsJobStart(String d, int v);

    @Query("UPDATE " + DMGeofence.TABLE_NAME + " SET " + DMGeofence.COLUMN_IS_STOP_JOB + "= :vv"
            + " WHERE " + DMGeofence.COLUMN_NAME + " = :dd" )
    int updateIsJobStop(String dd, int vv);
}
