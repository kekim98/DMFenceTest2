/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.dmlab.bawoori.dmfencetest2;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.dmlab.bawoori.dmfencetest2.Constants.INVALID_FLOAT_VALUE;
import static com.dmlab.bawoori.dmfencetest2.Constants.INVALID_INT_VALUE;
import static com.dmlab.bawoori.dmfencetest2.Constants.INVALID_LONG_VALUE;
import static com.dmlab.bawoori.dmfencetest2.Constants.KEY_EXPIRATION_DURATION;
import static com.dmlab.bawoori.dmfencetest2.Constants.KEY_LATITUDE;
import static com.dmlab.bawoori.dmfencetest2.Constants.KEY_LONGITUDE;
import static com.dmlab.bawoori.dmfencetest2.Constants.KEY_PREFIX;
import static com.dmlab.bawoori.dmfencetest2.Constants.KEY_RADIUS;
import static com.dmlab.bawoori.dmfencetest2.Constants.KEY_TRANSITION_TYPE;


/**
 * Storage for geofence values, implemented in SharedPreferences.
 */
public class SimpleGeofenceStore {

    // The SharedPreferences object in which geofences are stored.
    private final SharedPreferences mPrefs;
    // The name of the SharedPreferences.
    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private static final String TAG =SimpleGeofenceStore.class.getSimpleName() ;

    /**
     * Create the SharedPreferences storage with private access only.
     */
    public SimpleGeofenceStore(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Returns a stored geofence by its id, or returns null if it's not found.
     * @param id The ID of a stored geofence.
     * @return A SimpleGeofence defined by its center and radius, or null if the ID is invalid.
     */
    public SimpleGeofence getGeofence(String id) {
        // Get the latitude for the geofence identified by id, or INVALID_FLOAT_VALUE if it doesn't
        // exist (similarly for the other values that follow).
        double lat = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LATITUDE),
                INVALID_FLOAT_VALUE);
        double lng = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LONGITUDE),
                INVALID_FLOAT_VALUE);
        float radius = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_RADIUS),
                INVALID_FLOAT_VALUE);
        long expirationDuration =
                mPrefs.getLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                        INVALID_LONG_VALUE);
        int transitionType = mPrefs.getInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                INVALID_INT_VALUE);
        // If none of the values is incorrect, return the object.
        if (lat != INVALID_FLOAT_VALUE
                && lng != INVALID_FLOAT_VALUE
                && radius != INVALID_FLOAT_VALUE
                && expirationDuration != INVALID_LONG_VALUE
                && transitionType != INVALID_INT_VALUE) {
            return new SimpleGeofence(id, lat, lng, radius, expirationDuration, transitionType);
        }
        // Otherwise, return null.
        return null;
    }

    /**
     * Save a geofence.
     * @param geofence The SimpleGeofence with the values you want to save in SharedPreferences.
     */
    public void setGeofence(String id, SimpleGeofence geofence) {
        // Get a SharedPreferences editor instance. Among other things, SharedPreferences
        // ensures that updates are atomic and non-concurrent.
        SharedPreferences.Editor prefs = mPrefs.edit();
        // Write the SimpleGeofence values to SharedPreferences.
        prefs.putFloat(getGeofenceFieldKey(id, KEY_LATITUDE), (float) geofence.getLatitude());
        prefs.putFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), (float) geofence.getLongitude());
        prefs.putFloat(getGeofenceFieldKey(id, KEY_RADIUS), geofence.getRadius());
        prefs.putLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                geofence.getExpirationDuration());
        prefs.putInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                geofence.getTransitionType());
        // Commit the changes.
        prefs.commit();
    }

    public  List<String> getAllFenceIDs(){

        List<String> ret= new ArrayList<String>();
        Set<String> tempResult = new HashSet<String>();

        Pattern pattern = Pattern.compile(KEY_PREFIX + "_([^_]*)_*");
        SharedPreferences prefs = mPrefs;
        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
          //  Log.d(TAG, entry.getKey() + ": " + entry.getValue().toString());
            String fullKey = entry.getKey();
            Matcher matcher = pattern.matcher(fullKey);

            while (matcher.find()) {
             //   Log.d(TAG, "getAllFenceIDs: id=" + matcher.group(1));
                String id = matcher.group(1);
                if (!id.equals("TRANSITION") && !id.equals("EXPIRATION") && !id.equals("LONGITUDE") && !id.equals("LATITUDE") && !id.equals("RADIUS")) {
               //     Log.d(TAG, "strip: id=" + id);
                    tempResult.add(id);
                }
                //ret.setCID(matcher.group(1));
            }


            //ret.add(entry.getKey());
        }
        ret.addAll(tempResult);
        return ret;
    }

    /**
     * Remove a flattened geofence object from storage by removing all of its keys.
     */
    public void clearGeofence(String id) {
        SharedPreferences.Editor prefs = mPrefs.edit();
        prefs.remove(getGeofenceFieldKey(id, KEY_LATITUDE));
        prefs.remove(getGeofenceFieldKey(id, KEY_LONGITUDE));
        prefs.remove(getGeofenceFieldKey(id, KEY_RADIUS));
        prefs.remove(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION));
        prefs.remove(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE));
        prefs.commit();
    }

    /**
     * Given a SimpleGeofence object's ID and the name of a field (for example, KEY_LATITUDE), return
     * the key name of the object's values in SharedPreferences.
     * @param id The ID of a SimpleGeofence object.
     * @param fieldName The field represented by the key.
     * @return The full key name of a value in SharedPreferences.
     */
    private String getGeofenceFieldKey(String id, String fieldName) {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }

}
