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

package com.dmlab.bawoori.dmlib.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dmlab.bawoori.dmlib.data.DMGeofence;
import com.dmlab.bawoori.dmlib.data.DMGeofenceDao;
import com.dmlab.bawoori.dmlib.data.DMGeofenceDatabase;
import com.dmlab.bawoori.dmlib.data.DMLog;
import com.dmlab.bawoori.dmlib.data.DMLogDao;
import com.dmlab.bawoori.dmlib.service.DMLocationServiceHelper;

import java.util.ArrayList;


/**
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class DMGeofenceProvider extends ContentProvider {

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.dmlab.bawoori.dmlib.contentprovidersample.provider";

    /** The URI for the DMLog table. */
    public static final Uri URI_DMGEOFENCE = Uri.parse(
            "content://" + AUTHORITY + "/" + DMGeofence.TABLE_NAME);


    public static final Uri URI_DMLOG = Uri.parse(
            "content://" + AUTHORITY + "/" + DMLog.TABLE_NAME);

    public static final String PATH_GET_KNOWN_TRANS = "get_known_trans";
    public static final String PATH_GET_UNKNOWN_TRANS = "get_unknown_trans";
    public static final String PATH_GET_ENTER_TRANS = "get_enter_trans";
   // public static final String PATH_GET_STARTORSTOP_TRANS = "get_startorstop_trans";
    public static final String PATH_UPDATE_TRANS = "update_trans_type";
    public static final String PATH_UPDATE_IS_JOB_START = "update_is_job_start";
    public static final String PATH_UPDATE_IS_JOB_STOP = "update_is_job_stop";
    public static final String PATH_GET_DMLOG="get_dmlog";


    /** The match code for some items in the DMLog table. */
    private static final int CODE_DMGEOFENCE_DIR = 1;
    private static final int CODE_DMGEOFENCE_ITEM = 2;
    private static final int CODE_DMGEOFENCE_GET_KNOWN_TRANS = 3;
    private static final int CODE_DMGEOFENCE_GET_UNKNOWN_TRANS = 4;
    private static final int CODE_DMGEOFENCE_GET_ENTER_TRANS = 5;
    private static final int CODE_DMGEOFENCE_UPDATE_TRANS_TYPE = 6;
    private static final int CODE_DMGEOFENCE_UPDATE_IS_JOB_START = 7;
    private static final int CODE_DMGEOFENCE_UPDATE_IS_JOB_STOP = 8;


    private static final int CODE_DMLOG_DIR = 9;

    /** The match code for an item in the DMLog table. */
    private static final int CODE_DMLOG_ITEM = 10;
    private static final int CODE_DMLOG_GET_LOG = 11;

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);



    static {
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME, CODE_DMGEOFENCE_DIR);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/" + PATH_GET_KNOWN_TRANS, CODE_DMGEOFENCE_GET_KNOWN_TRANS);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/" + PATH_GET_UNKNOWN_TRANS, CODE_DMGEOFENCE_GET_UNKNOWN_TRANS);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/" + PATH_GET_ENTER_TRANS, CODE_DMGEOFENCE_GET_ENTER_TRANS);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/" + PATH_UPDATE_TRANS , CODE_DMGEOFENCE_UPDATE_TRANS_TYPE);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/" + PATH_UPDATE_IS_JOB_START , CODE_DMGEOFENCE_UPDATE_IS_JOB_START);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/" + PATH_UPDATE_IS_JOB_STOP , CODE_DMGEOFENCE_UPDATE_IS_JOB_STOP);
        MATCHER.addURI(AUTHORITY, DMGeofence.TABLE_NAME + "/*", CODE_DMGEOFENCE_ITEM);

        MATCHER.addURI(AUTHORITY, DMLog.TABLE_NAME, CODE_DMLOG_DIR);
        MATCHER.addURI(AUTHORITY, DMLog.TABLE_NAME + "/" + PATH_GET_DMLOG, CODE_DMLOG_GET_LOG);
        MATCHER.addURI(AUTHORITY, DMLog.TABLE_NAME + "/*", CODE_DMLOG_ITEM);

    }

    private DMLocationServiceHelper mGeoFenceHelperService;


    @Override
    public boolean onCreate() {

        if (mGeoFenceHelperService == null) {
            Log.d("bawoori", "onCreate: ");
            mGeoFenceHelperService = new DMLocationServiceHelper(this.getContext());
            mGeoFenceHelperService.startService();

        }

        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
            @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_DMGEOFENCE_DIR
                || code == CODE_DMGEOFENCE_ITEM
                || code == CODE_DMGEOFENCE_GET_KNOWN_TRANS
                || code == CODE_DMGEOFENCE_GET_UNKNOWN_TRANS
                || code == CODE_DMGEOFENCE_GET_ENTER_TRANS) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            DMGeofenceDao dmGeofenceDao = DMGeofenceDatabase.getInstance(context).dmGeofenceDao();
            final Cursor cursor;
            if (code == CODE_DMGEOFENCE_DIR) {
                cursor = dmGeofenceDao.selectAll();
            } else if (code == CODE_DMGEOFENCE_GET_KNOWN_TRANS) {
                cursor = dmGeofenceDao.selectOneKnownTrans();
            } else if (code == CODE_DMGEOFENCE_GET_UNKNOWN_TRANS) {
                cursor = dmGeofenceDao.selectAllUnknownTrans();
            } else if (code == CODE_DMGEOFENCE_GET_ENTER_TRANS) {
                cursor = dmGeofenceDao.selectAllEnterTrans();
            }
            else {
                cursor = dmGeofenceDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;

        }

        else if (code == CODE_DMLOG_DIR || code == CODE_DMLOG_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            DMLogDao dmLogDao = DMGeofenceDatabase.getInstance(context).dmLogDao();
            final Cursor cursor;
            if (code == CODE_DMLOG_DIR) {
                cursor = dmLogDao.selectAll();
            } else {
                cursor = dmLogDao.selectById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        }
        else if (code == CODE_DMLOG_GET_LOG){
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            DMLogDao dmLogDao = DMGeofenceDatabase.getInstance(context).dmLogDao();
            final Cursor cursor;

                cursor = dmLogDao.selectByFenceId(selection);

            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        }
        else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_DMGEOFENCE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + DMGeofence.TABLE_NAME;
            case CODE_DMGEOFENCE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + DMGeofence.TABLE_NAME;


            case CODE_DMLOG_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + DMLog.TABLE_NAME;
            case CODE_DMLOG_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + DMLog.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final Context context = getContext();
        long id;
        switch (MATCHER.match(uri)) {
            case CODE_DMGEOFENCE_DIR:
                if (context == null) {
                    return null;
                }
                id = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .insert(DMGeofence.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_DMGEOFENCE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);


            case CODE_DMLOG_DIR:
               // final Context context = getContext();
                if (context == null) {
                    return null;
                }
                id = DMGeofenceDatabase.getInstance(context).dmLogDao()
                        .insert(DMLog.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_DMLOG_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        final Context context = getContext();
        int count;
        switch (MATCHER.match(uri)) {
            case CODE_DMGEOFENCE_DIR:
               // throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
                if (context == null) {
                    return 0;
                }
                count = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .deleteAll();
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_DMGEOFENCE_ITEM:
                if (context == null) {
                    return 0;
                }
                count = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;


            case CODE_DMLOG_DIR:
                if (context == null) {
                    return 0;
                }
                count = DMGeofenceDatabase.getInstance(context).dmLogDao()
                        .deleteAll();
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_DMLOG_ITEM:
            //    final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                count = DMGeofenceDatabase.getInstance(context).dmLogDao()
                        .deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;


            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
            @Nullable String[] selectionArgs) {
        final Context context = getContext();
        final DMGeofence dmGeofence = DMGeofence.fromContentValues(values);
        int count;
        switch (MATCHER.match(uri)) {
            case CODE_DMGEOFENCE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_DMGEOFENCE_ITEM:
               // final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                dmGeofence.id = ContentUris.parseId(uri);
                count = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .update(dmGeofence);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_DMGEOFENCE_UPDATE_TRANS_TYPE:
                final long id = dmGeofence.id;
                final int type = dmGeofence.transition_type;
                count = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .updateTransType(id, type);
                return count;

            case CODE_DMGEOFENCE_UPDATE_IS_JOB_START:
                final String d = dmGeofence.fence_id;
                final int v = dmGeofence.is_start_job;
                count = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .updateIsJobStart(d, v);
                return count;
            case CODE_DMGEOFENCE_UPDATE_IS_JOB_STOP:
                final String dd = dmGeofence.fence_id;
                final int vv = dmGeofence.is_stop_job;
                count = DMGeofenceDatabase.getInstance(context).dmGeofenceDao()
                        .updateIsJobStop(dd, vv);
                return count;


            case CODE_DMLOG_DIR:
                if (context == null) {
                    return 0;
                }
                final DMLog dl = DMLog.fromContentValues(values);
                count = DMGeofenceDatabase.getInstance(context).dmLogDao()
                        .updateUnknown(dl.getFence_id());
                context.getContentResolver().notifyChange(uri, null);
                return count;
            case CODE_DMLOG_ITEM:

                if (context == null) {
                    return 0;
                }
                final DMLog dmLog = DMLog.fromContentValues(values);
                dmLog.id = ContentUris.parseId(uri);
                count = DMGeofenceDatabase.getInstance(context).dmLogDao()
                        .update(dmLog);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final DMGeofenceDatabase database = DMGeofenceDatabase.getInstance(context);
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        final Context context = getContext();
        final DMGeofenceDatabase database = DMGeofenceDatabase.getInstance(context);
        switch (MATCHER.match(uri)) {
            case CODE_DMGEOFENCE_DIR:

                if (context == null) {
                    return 0;
                }
               // final DMGeofenceDatabase database = DMGeofenceDatabase.getInstance(context);
                final DMGeofence[] dmGeofences = new DMGeofence[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    dmGeofences[i] = DMGeofence.fromContentValues(valuesArray[i]);
                }
                return database.dmGeofenceDao().insertAll(dmGeofences).length;

            case CODE_DMGEOFENCE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);

            case CODE_DMLOG_DIR:
               // final Context context = getContext();
                if (context == null) {
                    return 0;
                }
              //  final DMGeofenceDatabase database = DMGeofenceDatabase.getInstance(context);
                final DMLog[] DMLogs = new DMLog[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    DMLogs[i] = DMLog.fromContentValues(valuesArray[i]);
                }
                return database.dmLogDao().insertAll(DMLogs).length;
            case CODE_DMLOG_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


}
