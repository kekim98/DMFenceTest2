package com.dmlab.bawoori.dmlib.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;


/**
 * Created by bawoori on 17. 11. 9.
 */

@Entity(tableName = DMGeofence.TABLE_NAME, indices = {@Index(value = "fence_id", unique = true)})
public class DMGeofence {

    public static final String TABLE_NAME = "t_dm_geofence";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "fence_id";
    public  enum transition_type {
        TRANS_ENTER, TRANS_START, TRANS_STOP, TRANS_EXIT
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @NonNull
    public String fence_id;

    @NonNull
    public long latitude;

    @NonNull
    public long longitude;

    public String address;

    public int  cell_id;


    public String wifi_mac_adds;
    public int  priority;
    public long radius;
    public int expire_duration;

    public int transition_type;
    public String reg_time;


    public static DMGeofence fromContentValues(ContentValues values) {
        final DMGeofence dmGeofence = new DMGeofence();
        if (values.containsKey(COLUMN_ID)) {
            dmGeofence.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            dmGeofence.fence_id = values.getAsString(COLUMN_NAME);
        }
        return dmGeofence;
    }

}
