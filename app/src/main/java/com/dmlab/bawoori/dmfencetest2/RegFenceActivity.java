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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dmlab.bawoori.dmlib.dmINF.DMService;


public class RegFenceActivity extends AppCompatActivity {
    private static final String TAG = RegFenceActivity.class.getSimpleName();
    DMService mService;
    boolean mBound = false;
    private static final String ADD_SUCCESS_MSG = "정상 등록 되었습니다.";
    private static final String ADD_FAIL_MSG = "이미 등록된 Geofences가 있습니다.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to DMService
        Intent intent = new Intent(this, DMService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_reg_device);
        setTitle("등록 화면");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
    protected void onPause() {
        super.onPause();
        InputMethodManager immhide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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

    /**
     * Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute)
     */
    public void onRegDevice(View v) {
        EditText userInput = findViewById(R.id.userDevice);
        String fenceId = userInput.getText().toString();
        if (fenceId.isEmpty()) {
            Toast.makeText(this,
                    "입력값이 없습니다. 다시 입력해 주세요"
                    , Toast.LENGTH_LONG).show();
            return;
        }
        if (mBound) {
            boolean ret = mService.isValidID(fenceId);
            if (!ret) {
                Toast.makeText(this,
                        "이미 사용된 ID입니다. 다시 입력해 주세요"
                        , Toast.LENGTH_LONG).show();
                return;
            }
        }
        Intent intent = new Intent(this, AddFenceActivity.class);
        intent.putExtra("FENCE_ID", fenceId);
        startActivity(intent);
        finish();
    }

    public void onCancelRegDevice(View v) {
        finish();
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to DMService, cast the IBinder and get DMService instance
            Log.d(TAG, "onServiceConnected().................................");
            if (!mBound) {
                DMService.LocalBinder binder = (DMService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.d(TAG, "onServiceDisconnected().................................");
        }

    };
}

