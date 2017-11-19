package com.dmlab.bawoori.dmfencetest2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;


public class FenceInfoActivity extends AppCompatActivity {
    private static final String TAG = FenceInfoActivity.class.getSimpleName();
    private static final String CHK_SUCCESS_MSG = "GeoFence 정보.";
   // private static final String CHK_FAIL_MSG = "서비스가 정상 완료되지 못하였습니다.";
    DMService mService;
    boolean mBound = false;
    static String FENCE_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("GeoFence Info");

        // Bind to DMService
        Intent intent = new Intent(this, DMService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume().mBound:" + mBound);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }

    }
 /*   *//** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) *//*
    public void onReAddDevice(View v) {
        Log.d(TAG, "FENCE_ID:" + FENCE_ID);
        Intent intent=new Intent(this, AddFenceActivity.class);
        intent.putExtra("FENCE_ID", FENCE_ID);
        startActivity(intent);
        finish();
    }
*/
    public void onCancle(View v) {
        finish();
    }
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to DMService, cast the IBinder and get DMService instance
            if(!mBound) {
                DMService.LocalBinder binder = (DMService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;

                Bundle extras = getIntent().getExtras();
                DMInfo dmInfo = null;
                int logCount=0;

                if (extras != null) {
                    FENCE_ID = extras.getString("FENCE_ID");
                    dmInfo = mService.getFence(FENCE_ID);
                    logCount= mService.getLogCount(FENCE_ID);
                    //The key argument here must match that used in the other activity
                }

                if (dmInfo != null) {
                    TextView fence_id = (TextView) findViewById(R.id.fence_id);
                    TextView latitude = (TextView) findViewById(R.id.latitude);
                    TextView longitude = (TextView) findViewById(R.id.longitude);
                    TextView radius = (TextView) findViewById(R.id.radius);
                    TextView transition_type = (TextView) findViewById(R.id.transition_type);
                    TextView log_count = findViewById(R.id.logCount);

                    fence_id.setText(String.format(Locale.ENGLISH, "%s: %s",
                            "ID",
                            dmInfo.getId()));
                    latitude.setText(String.format(Locale.ENGLISH, "%s: %f",
                            "LATITUDE",
                            dmInfo.getLatitude()));
                    longitude.setText(String.format(Locale.ENGLISH, "%s: %f",
                            "LONGITUDE",
                            dmInfo.getLongitude()));
                    radius.setText(String.format(Locale.ENGLISH, "%s: %f",
                            "RADIUS",
                            dmInfo.getRadius()));
                    transition_type.setText(String.format(Locale.ENGLISH, "%s: %d",
                            "TRANSITION_TYPE",
                            dmInfo.getType()));
                    log_count.setText(String.format(Locale.ENGLISH, "%s: %d",
                            "LOG_COUNT",
                            logCount));;

                }


                Log.d(TAG, "ServiceConnected.................................");
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.d(TAG, "onServiceDisconnected().................................");
        }
    };
}
