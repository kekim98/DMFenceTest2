package com.dmlab.bawoori.dmfencetest2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class AddFenceActivity extends AppCompatActivity {

    private static final String TAG = AddFenceActivity.class.getSimpleName();
    private static final String ADD_SUCCESS_MSG = "정상 저장 되었습니다.";
    private static final String ADD_FAIL_MSG = "이미 저장된 위치정가 있습니다.";

    DMService mService;
    boolean mBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        setTitle("위치정보 저장 결과 화면");

        // Bind to DMService
        mBound=false;
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

    /** Defines callbacks for service binding, passed to bindService() */
    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to DMService, cast the IBinder and get DMService instance
            if(!mBound) {
                DMService.LocalBinder binder = (DMService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;

                Bundle extras = getIntent().getExtras();
                boolean result = false;
                DMInfo dmInfo = new DMInfo();

                String geofence_id = "";
                if (extras != null) {
                    geofence_id = extras.getString("FENCE_ID");
                    DMTestApplication application = (DMTestApplication) getApplicationContext();
                    Double latitude = application.getLatitude();
                    Double longitue = application.getLongitude();


                    if (latitude == 0.0f || longitue == 0.0f) {
                        Toast.makeText(AddFenceActivity.this, "GPS 값이 유효하지 않습니다. GPS 상태 확인 바랍니다", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    dmInfo.setId(geofence_id);
                    dmInfo.setLatitude(latitude);
                    dmInfo.setLongitude(longitue);

                    Log.d(TAG, "onServiceConnected: latitude=" + String.valueOf(latitude));
                    result = mService.addFence(dmInfo);


                    if (result) {
                        TextView textView1 = (TextView) findViewById(R.id.addResultMsg);
                        textView1.setText(ADD_SUCCESS_MSG);

                        TextView id = (TextView) findViewById(R.id.VID);
                        id.setText(geofence_id);

                   /* TextView CELLID = (TextView) findViewById(R.id.VCELLID);
                    CELLID.setText(lo.getCID());

                    TextView ADDR1 = (TextView) findViewById(R.id.VADDR1);
                    ADDR1.setText(lo.getADDR1());

                    TextView ADDR2 = (TextView) findViewById(R.id.VADDR2);
                    ADDR2.setText(lo.getADDR2());*/

                        TextView LAT = (TextView) findViewById(R.id.VLAT);
                        LAT.setText(String.format(Locale.ENGLISH, "%f", dmInfo.getLatitude()));

                        TextView LNG = (TextView) findViewById(R.id.VLNG);
                        LNG.setText(String.format(Locale.ENGLISH, "%f", dmInfo.getLongitude()));


                    } else {
                        TextView textView1 = (TextView) findViewById(R.id.addResultMsg);
                        textView1.setText(ADD_FAIL_MSG);
                    }
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
