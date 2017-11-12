package com.dmlab.bawoori.dmfencetest2;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.dmlab.bawoori.dmlib.data.DMGeofence;
import com.dmlab.bawoori.dmlib.provider.DMGeofenceProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.dmlab.bawoori.dmfencetest2.Constants.ANDROID_BUILDING_RADIUS_METERS;
import static com.dmlab.bawoori.dmfencetest2.Constants.GEOFENCE_EXPIRATION_TIME;


public class DMService extends Service  {
    private static final String TAG = DMService.class.getSimpleName();


/*
    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }*/
    private IBinder mBinder = new LocalBinder();;
  //  private GeofencingClient mGeofencingClient;
    private SimpleGeofenceStore mGeofenceStorage;
    private PendingIntent mGeofencePendingIntent;
  //  private List<Geofence> mGeofenceList;
  //  private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;

/*
    @Override
    public void onComplete(@NonNull Task<Void> task) {
  //      mPendingGeofenceTask = PendingGeofenceTask.NONE;
        if (task.isSuccessful()) {
            */
/*
            updateGeofencesAdded(!getGeofencesAdded());

            int messageId = getGeofencesAdded() ? R.string.geofences_added :
                    R.string.geofences_removed;*//*

          //int messageId = R.string.geofences_added;
           //oast.makeText(this, getString(messageId), Toast.LENGTH_SHORT).show();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            Log.w(TAG, errorMessage);
        }

    }
*/


    public class LocalBinder extends Binder {
        public DMService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DMService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();

       /* Settings.System.putInt(getContentResolver(),
                Settings.System.WIFI_SLEEP_POLICY,
                Settings.System.WIFI_SLEEP_POLICY_NEVER);


        WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiManager.WifiLock wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY , "MyWifiLock");
        if(!wifiLock.isHeld()){
            wifiLock.acquire();
        }
*/
        mGeofencePendingIntent = null;
        mGeofenceStorage = new SimpleGeofenceStore(this);
      //  mGeofenceList = new ArrayList<>();
      //  mGeofencingClient = LocationServices.getGeofencingClient(this);
      //  createGeofences();
   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
         return mBinder;
    }

     /*
    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Constants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }

    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                Constants.GEOFENCES_ADDED_KEY, false);
    }

  private void addGeofences() {
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }


    private void removeGeofences() {

        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(this);
    }*/

  /*  public void createGeofences() {
        removeGeofences();

       *//* try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*//*

        mGeofenceList.clear();
        for (String id : getAllFenceIDs()) {
            SimpleGeofence geofence = mGeofenceStorage.getGeofence(id);
            mGeofenceList.add(geofence.toGeofence());
        }

        if (mGeofenceList.size() > 0) {
            addGeofences();
        }

    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }*/



    public void initDMLib() {

        /*mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceStorage = new SimpleGeofenceStore(this);
        mGeofenceList = new ArrayList<>();*/

    }

    public String[] getAllFenceIDs() {
        List<String> fences = mGeofenceStorage.getAllFenceIDs();

        return fences.toArray(new String[fences.size()]);
    }


    public boolean isValidID(String id){
        return mGeofenceStorage.getGeofence(id)==null?true:false;
    }

    public boolean addFence(DMInfo info) {


        String id = info.getId();
        Double latitude = info.getLatitude();
        Double longitue = info.getLongitude();

        SimpleGeofence geofence = new SimpleGeofence(id, latitude, longitue,
                ANDROID_BUILDING_RADIUS_METERS,
                GEOFENCE_EXPIRATION_TIME,
                1 | 2);

        mGeofenceStorage.setGeofence(id, geofence);

    //    createGeofences();

        return true;
    }

    public DMInfo getFence(String id) {

        DMInfo result = new DMInfo();
        SimpleGeofence geofence = mGeofenceStorage.getGeofence(id);

        result.setId(geofence.getId());
        result.setLatitude(geofence.getLatitude());
        result.setLongitude(geofence.getLongitude());
        result.setRadius(geofence.getRadius());
        result.setType(geofence.getTransitionType());

        return result;
    }

    public int createDMGeofences(){
        List<ContentValues> geofenceList =  new ArrayList<ContentValues>();
        for (String id : getAllFenceIDs()) {
            SimpleGeofence geofence = mGeofenceStorage.getGeofence(id);

            geofenceList.add(simplgeGeofeceToDMGeofence(geofence));
        }

        ContentValues[] simpleArray = new ContentValues[ geofenceList.size() ];
        geofenceList.toArray( simpleArray );

        return getContentResolver().bulkInsert(DMGeofenceProvider.URI_DMGEOFENCE,simpleArray);

    }

    private ContentValues simplgeGeofeceToDMGeofence(SimpleGeofence geofence) {
        final ContentValues values = new ContentValues();
        values.put(DMGeofence.COLUMN_NAME, geofence.getId());
        values.put(DMGeofence.COLUMN_ADDRESS, "");
        values.put(DMGeofence.COLUMN_CELL_ID, 0);
        values.put(DMGeofence.COLUMN_EXPIRE_DURATION, 0);
        values.put(DMGeofence.COLUMN_LATITUDE, geofence.getLatitude());
        values.put(DMGeofence.COLUMN_LONGITUDE, geofence.getLongitude());
        values.put(DMGeofence.COLUMN_PRIORITY, 0);
        values.put(DMGeofence.COLUMN_RADIUS, 100);
        values.put(DMGeofence.COLUMN_REG_TIME, Calendar.getInstance().getTimeInMillis());
        values.put(DMGeofence.COLUMN_TRANSITION_TYPE, DMGeofence.TRANS_UNKNOWN);

        return values;
    }

}
