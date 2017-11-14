/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmlab.bawoori.dmlib.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.dmlab.bawoori.dmlib.R;
import com.dmlab.bawoori.dmlib.data.DMGeofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Listener for geofence transition changes.
 *
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class DMGeofenceNotificationService extends IntentService {

    private static final String TAG = DMGeofenceNotificationService.class.getSimpleName();
    private static final String ACTION_ENTER_NOTIFICATION = "com.dmlab.bawoori.dmlib.ACTION_ENTER_NOTIFICATION";
    private static final String ACTION_EXIT_NOTIFICATION = "com.dmlab.bawoori.dmlib.ACTION_EXIT_NOTIFICATION";

    public static void startActionEnterNotification(Context context, String geofencName, long time) {
        Intent intent = new Intent(context, DMGeofenceNotificationService.class);
        intent.setAction(DMGeofenceNotificationService.ACTION_ENTER_NOTIFICATION);
        intent.putExtra("geofencName", geofencName);
        intent.putExtra("time", time);
        intent.putExtra("type", DMGeofence.TRANS_ENTER);
        context.startService(intent);
    }

    public static void startActionExitNotification(Context context, String geofencName, long time) {
        Intent intent = new Intent(context, DMGeofenceNotificationService.class);
        intent.setAction(DMGeofenceNotificationService.ACTION_EXIT_NOTIFICATION);
        intent.putExtra("geofencName", geofencName);
        intent.putExtra("time", time);
        intent.putExtra("type", DMGeofence.TRANS_EXIT);
        context.startService(intent);
    }

    public static void startActionStartOrStopNotification(Context context, String geofenceName,
                                                          int is_start_job, int is_stop_job) {
        Intent intent = new Intent(context, DMGeofenceNotificationService.class);
        intent.setAction(DMGeofenceNotificationService.ACTION_EXIT_NOTIFICATION);
        intent.putExtra("geofencName", geofenceName);
        if(is_start_job == DMGeofence.JOB_NOT_DONE){
            intent.putExtra("type", DMGeofence.TRANS_START);
        }
        else{
            intent.putExtra("type", DMGeofence.TRANS_STOP);
        }

        context.startService(intent);
    }

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public DMGeofenceNotificationService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    /**
     * Handles incoming intents.
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            // Get the transition type.
            int geofenceTransition = intent.getIntExtra("type", -1);

            // Test that the reported transition was of interest.
            if (geofenceTransition == DMGeofence.TRANS_ENTER ||
                    geofenceTransition == DMGeofence.TRANS_EXIT ||
                    geofenceTransition == DMGeofence.TRANS_START ||
                    geofenceTransition == DMGeofence.TRANS_STOP) {

                final String triggeringGeofence = intent.getStringExtra("geofencName");
                // Get the transition details as a String.
                String geofenceTransitionDetails = getGeofenceTransitionDetails(geofenceTransition,
                        triggeringGeofence);

                // Send notification and log the transition details.
                sendNotification(geofenceTransitionDetails);
                Log.i(TAG, geofenceTransitionDetails);
            } else {
                // Log the error.
                Log.e(TAG, "unknown geofence type");
            }
        }


    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition    The ID of the geofence transition.
     * @param geofencName   The geofence(s) triggered.
     * @return                      The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            String geofencName) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.


        return geofenceTransitionString + ": " + geofencName;
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(String notificationDetails) {

        // Creates an Intent for the Activity
        Intent notificationIntent = new Intent();

        notificationIntent.setAction("com.bawoori.dmlib.ACTION_NOTIFICATION_INTENT");
        notificationIntent.setClassName("com.dmlab.bawoori.dmfencetest2", "com.dmlab.bawoori.dmfencetest2.MainActivity");

// Sets the Activity to start in a new, empty task
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
// Creates the PendingIntent

        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setSmallIcon(R.drawable.dmgeofence)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.dmgeofence))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to app")
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType    A transition type constant defined in SimpleGeofence
     * @return                  A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case DMGeofence.TRANS_ENTER:
                return "Entered";
            case DMGeofence.TRANS_EXIT:
                return "Exited";
            case DMGeofence.TRANS_START:
                return "Not Started Job";
            case DMGeofence.TRANS_STOP:
                return "Not Stoped Job";
            default:
                return "Unknown Transition";
        }
    }



}
