package com.dmlab.bawoori.dmfencetest2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    DMService mService;
    boolean mBound = false;

    private ArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind to DMService
        Intent intent = new Intent(this, DMService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        setContentView(R.layout.activity_main);
        setTitle("Geofences 리스트");

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;

        ListView listview = (ListView) findViewById(R.id.deviceList) ;
        listview.setAdapter(adapter) ;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;
                Log.d(TAG, "selected item:" + strText);

                Intent intent=new Intent(MainActivity.this, FenceInfoActivity.class);
                intent.putExtra("FENCE_ID", strText);
                startActivity(intent);
            }
        }) ;


    }

    @Override
    protected void onStart() {
        super.onStart();

        // Bind to DMService
//        Intent intent = new Intent(this, DMService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private static final int REQUEST_SCAN_ALWAYS_AVAILABLE = 1;
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
        Log.d(TAG, "onResume().mBound:" + mBound);



        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (Build.VERSION.SDK_INT >= 18 && !wifiManager.isScanAlwaysAvailable()) {
            Intent intent = new Intent();
            intent.setAction(WifiManager.ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE);
            startActivityForResult(intent, REQUEST_SCAN_ALWAYS_AVAILABLE);
        }
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

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute) */
    public void onAddFence(View v) {
        Intent intent=new Intent(this, RegFenceActivity.class);
        startActivity(intent);
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

               mService.initDMLib();
               refresh();
               Log.d(TAG, "ServiceConnected-mBound:" + mBound);
           }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.d(TAG, "onServiceDisconnected().................................");
        }
    };

    private void refresh(){
        if(mBound){
            String[] fences = mService.getAllFences();
            adapter.clear();
            for(int i=0; i<fences.length; i++){
                adapter.add(fences[i]);
            }
            adapter.notifyDataSetChanged();
            Log.d(TAG, "refresh().................................");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dmgeofence_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_fences:
                addDMGeofences();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDMGeofences() {
        Log.d(TAG, "addDMGeofences: ................");
    }

}
