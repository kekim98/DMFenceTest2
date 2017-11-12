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
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_CELL_ID = "cell_id";
    public static final String COLUMN_WIFI_MAC_ADDRS = "wifi_mac_adds";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_RADIUS = "radius";
    public static final String COLUMN_EXPIRE_DURATION = "expire_duration";
    public static final String COLUMN_TRANSITION_TYPE = "transition_type";
    public static final String COLUMN_REG_TIME = "reg_time";

    public static final int TRANS_UNKNOWN = 0;
    public static final int TRANS_ENTER = 1;
    public static final int TRANS_START = 2;
    public static final int TRANS_STOP = 3;
    public static final int TRANS_EXIT = 4;





    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @NonNull
    public String fence_id;

    @NonNull
    public Double latitude;

    @NonNull
    public Double longitude;

    public String address;

    public long  cell_id;


    public String wifi_mac_adds;
    public int  priority;
    public long radius;
    public int expire_duration;

    public int transition_type;
    public long reg_time;


    public static DMGeofence fromContentValues(ContentValues values) {
        final DMGeofence dmGeofence = new DMGeofence();
        if (values.containsKey(COLUMN_ID)) {
            dmGeofence.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_NAME)) {
            dmGeofence.fence_id = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_TRANSITION_TYPE)){
            dmGeofence.transition_type = values.getAsInteger(COLUMN_TRANSITION_TYPE);
        }
        if (values.containsKey(COLUMN_REG_TIME)){
            dmGeofence.reg_time = values.getAsLong(COLUMN_REG_TIME);
        }
        if (values.containsKey(COLUMN_RADIUS)){
            dmGeofence.radius = values.getAsInteger(COLUMN_RADIUS);
        }

        if (values.containsKey(COLUMN_LATITUDE)){
            dmGeofence.latitude = values.getAsDouble(COLUMN_LATITUDE);
        }
        if (values.containsKey(COLUMN_LONGITUDE)){
            dmGeofence.longitude = values.getAsDouble(COLUMN_LONGITUDE);
        }
        if (values.containsKey(COLUMN_CELL_ID)){
            dmGeofence.cell_id = values.getAsLong(COLUMN_CELL_ID);
        }

        if (values.containsKey(COLUMN_ADDRESS)){
            dmGeofence.address = values.getAsString(COLUMN_ADDRESS);
        }

        if (values.containsKey(COLUMN_EXPIRE_DURATION)){
            dmGeofence.expire_duration = values.getAsInteger(COLUMN_EXPIRE_DURATION);
        }

        if (values.containsKey(COLUMN_WIFI_MAC_ADDRS)){
            dmGeofence.wifi_mac_adds = values.getAsString(COLUMN_WIFI_MAC_ADDRS);
        }
        if (values.containsKey(COLUMN_PRIORITY)){
            dmGeofence.priority = values.getAsInteger(COLUMN_PRIORITY);
        }


        return dmGeofence;
    }

}
