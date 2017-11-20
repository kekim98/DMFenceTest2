package com.dmlab.bawoori.dmlib.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.dmlab.bawoori.dmlib.data.DMGeofence;
import com.dmlab.bawoori.dmlib.data.DMLog;

import java.util.Calendar;
import java.util.Date;

import static com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider.PATH_GET_ENTER_TRANS;
import static com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider.PATH_GET_KNOWN_TRANS;
import static com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider.PATH_GET_UNKNOWN_TRANS;
import static com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider.PATH_UPDATE_TRANS;
import static com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider.URI_DMGEOFENCE;
import static com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider.URI_DMLOG;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DMGeofenceService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS

    public static final String ACTION_PROC_DMGEOFENCE = "com.dmlab.bawoori.dmlib.ACTION_PROC_DMGEOFENCE" ;
    private static final String ACTION_FOO = "com.dmlab.bawoori.dmlib.service.action.FOO";
    private static final String ACTION_BAZ = "com.dmlab.bawoori.dmlib.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.dmlab.bawoori.dmlib.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.dmlab.bawoori.dmlib.service.extra.PARAM2";
    private static final String TAG = DMGeofenceService.class.getSimpleName();
    private static final float ENTER_RADIUS_PERCENT = 0.4f;
    private static final float EXIT_RADIUS_PERCENT = 0.1f;


    public DMGeofenceService() {
        super("DMGeofenceService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionProcDMGeofence(Context context, Double latitude, Double longitude) {
        Intent intent = new Intent(context, DMGeofenceService.class);
        intent.setAction(DMGeofenceService.ACTION_PROC_DMGEOFENCE);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        context.startService(intent);
    }
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DMGeofenceService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }



    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DMGeofenceService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROC_DMGEOFENCE.equals(action)) {
                final Double latitude = intent.getDoubleExtra("latitude", 0);
                final Double longitude = intent.getDoubleExtra("longitude", 0);
                handleActionDMGeofence(latitude, longitude);
            } else if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    private void handleActionDMGeofence(Double latitude, Double longitude) {

        triggerStartOrStop();
        triggerExitEvent(latitude, longitude);
        triggerEnterEvent(latitude, longitude);
        saveLog(latitude,longitude);
    
    }

    private void saveLog(Double latitude, Double longitude) {
        final Cursor checkEnterCursor = getContentResolver().query(
                URI_DMGEOFENCE.buildUpon().appendPath(PATH_GET_ENTER_TRANS).build(),
                null,null,null,null);

        final Cursor checkUnknownCursor = getContentResolver().query(
                URI_DMGEOFENCE.buildUpon().appendPath(PATH_GET_UNKNOWN_TRANS).build(),
                null,null,null,null);


        if(checkEnterCursor.getCount() > 0 && checkUnknownCursor.getCount() > 0){
            final ContentValues values = new ContentValues();
            values.put(DMLog.COLUMN_DM_ID, "UNKNOWN");
            values.put(DMLog.COLUMN_LATITUDE, latitude);
            values.put(DMLog.COLUMN_LONGITUDE, longitude);
            values.put(DMLog.COLUMN_REG_TIME,new Date().getTime());


           getContentResolver().insert(URI_DMLOG, values);


        }

    }

    private void triggerStartOrStop() {
        // Log.d(TAG, "triggerExitEvent: ............");
        final Cursor cursor = getContentResolver().query(
                URI_DMGEOFENCE.buildUpon().appendPath(PATH_GET_KNOWN_TRANS).build(),
                null,null,null,null);

        try {
            while (cursor.moveToNext()) {
                final int is_start_job = cursor.getInt(cursor.getColumnIndex(DMGeofence.COLUMN_IS_START_JOB));
                final int is_stop_job = cursor.getInt(cursor.getColumnIndex(DMGeofence.COLUMN_IS_STOP_JOB));
                final String geofenceName = cursor.getString(cursor.getColumnIndex(DMGeofence.COLUMN_NAME));


                Log.d(TAG, "triggerStartOrStop: Exited.........");

                DMGeofenceNotificationService.startActionStartOrStopNotification(getApplicationContext(), geofenceName, is_start_job, is_stop_job);




               /* Log.d(TAG, "triggerEnterEvent: latitude=" + String.valueOf(dLatitude));
                Log.d(TAG, "triggerEnterEvent: longitude=" + String.valueOf(dLongitude));
                Log.d(TAG, "triggerEnterEvent: enter=" + String.valueOf(enter) );
                Log.d(TAG, "triggerEnterEvent: distance=" + String.valueOf(results[0]) );*/
            }
        } finally {
            cursor.close();
        }
    }

    private void triggerExitEvent(Double latitude, Double longitude) {
       // Log.d(TAG, "triggerExitEvent: ............");
        final Cursor cursor = getContentResolver().query(
                URI_DMGEOFENCE.buildUpon().appendPath(PATH_GET_ENTER_TRANS).build(),
                null,null,null,null);

        try {
            while (cursor.moveToNext()) {
                Double dLatitude = cursor.getDouble(cursor.getColumnIndex(DMGeofence.COLUMN_LATITUDE));
                Double dLongitude =cursor.getDouble(cursor.getColumnIndex(DMGeofence.COLUMN_LONGITUDE));
                int radisu =cursor.getInt(cursor.getColumnIndex(DMGeofence.COLUMN_RADIUS));

                float exit = radisu + (radisu * EXIT_RADIUS_PERCENT);
                float[] results = {0, 0,0};
                Location.distanceBetween(latitude, longitude, dLatitude, dLongitude,results);

                if (exit < results[0]) {
                    Log.d(TAG, "triggerExitEvent: Exited.........");
                    ContentValues contentValue = new ContentValues();
                    final long id = cursor.getLong(cursor.getColumnIndex(DMGeofence.COLUMN_ID));
                    final String fence_name = cursor.getString(cursor.getColumnIndex(DMGeofence.COLUMN_NAME));
                    contentValue.put(DMGeofence.COLUMN_ID, id);
                    contentValue.put(DMGeofence.COLUMN_TRANSITION_TYPE, DMGeofence.TRANS_EXIT);
                    final int count = getContentResolver().update(URI_DMGEOFENCE.buildUpon().appendPath(PATH_UPDATE_TRANS).build(),
                            contentValue, null, null);
                    Log.d(TAG, "triggerEnterEvent: updated count=" + String.valueOf(count));

                    final String geofenName = cursor.getString(cursor.getColumnIndex(DMGeofence.COLUMN_NAME));
                    final long time = Calendar.getInstance().getTimeInMillis();
                    DMGeofenceNotificationService.startActionExitNotification(getApplicationContext(), geofenName, time);

                    udpateDmLog(fence_name);
                    break;
                }

               /* Log.d(TAG, "triggerEnterEvent: latitude=" + String.valueOf(dLatitude));
                Log.d(TAG, "triggerEnterEvent: longitude=" + String.valueOf(dLongitude));
                Log.d(TAG, "triggerEnterEvent: enter=" + String.valueOf(enter) );
                Log.d(TAG, "triggerEnterEvent: distance=" + String.valueOf(results[0]) );*/
            }
        } finally {
            cursor.close();
        }
    }

    private void udpateDmLog(String fence_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DMLog.COLUMN_DM_ID, fence_name);
        final int count = getContentResolver().update(URI_DMLOG,
                contentValue, null, null);

    }

    private void triggerEnterEvent(Double latitude, Double longitude) {
        //Log.d(TAG, "triggerEnterEvent: ...................");
        final Cursor checkCursor = getContentResolver().query(
                URI_DMGEOFENCE.buildUpon().appendPath(PATH_GET_ENTER_TRANS).build(),
                null,null,null,null);

        try{
            if(checkCursor.getCount() > 0) {
                checkCursor.close();
                return;
            }

        }finally {
            checkCursor.close();
        }


        final Cursor cursor = getContentResolver().query(
                URI_DMGEOFENCE.buildUpon().appendPath(PATH_GET_UNKNOWN_TRANS).build(),
                null,null,null,null);

        try {
            while (cursor.moveToNext()) {
                Double dLatitude = cursor.getDouble(cursor.getColumnIndex(DMGeofence.COLUMN_LATITUDE));
                Double dLongitude =cursor.getDouble(cursor.getColumnIndex(DMGeofence.COLUMN_LONGITUDE));
                int radisu =cursor.getInt(cursor.getColumnIndex(DMGeofence.COLUMN_RADIUS));

                float enter = radisu - (radisu * ENTER_RADIUS_PERCENT);
                float[] results = {0, 0,0};
                Location.distanceBetween(latitude, longitude, dLatitude, dLongitude,results);

                if (enter > results[0]) {
                    Log.d(TAG, "triggerEnterEvent: Entered.........");
                    ContentValues contentValue = new ContentValues();
                    final long id = cursor.getLong(cursor.getColumnIndex(DMGeofence.COLUMN_ID));
                    contentValue.put(DMGeofence.COLUMN_ID, id);
                    contentValue.put(DMGeofence.COLUMN_TRANSITION_TYPE, DMGeofence.TRANS_ENTER);
                    final int count = getContentResolver().update(URI_DMGEOFENCE.buildUpon().appendPath(PATH_UPDATE_TRANS).build(),
                            contentValue, null, null);
                    Log.d(TAG, "triggerEnterEvent: updated count=" + String.valueOf(count));

                    final String geofenName = cursor.getString(cursor.getColumnIndex(DMGeofence.COLUMN_NAME));
                    final long time = Calendar.getInstance().getTimeInMillis();
                    DMGeofenceNotificationService.startActionEnterNotification(getApplicationContext(), geofenName, time);

                    break;
                }

               /* Log.d(TAG, "triggerEnterEvent: latitude=" + String.valueOf(dLatitude));
                Log.d(TAG, "triggerEnterEvent: longitude=" + String.valueOf(dLongitude));
                Log.d(TAG, "triggerEnterEvent: enter=" + String.valueOf(enter) );
                Log.d(TAG, "triggerEnterEvent: distance=" + String.valueOf(results[0]) );*/
            }
        } finally {
            cursor.close();
        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
