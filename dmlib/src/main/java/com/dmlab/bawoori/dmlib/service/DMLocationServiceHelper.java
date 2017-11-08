package com.dmlab.bawoori.dmlib.service;

import android.content.Context;
import android.content.Intent;


/**
 * Created by bawoori on 17. 11. 8.
 */

public class DMLocationServiceHelper {
    private final Context mCTX;




    public DMLocationServiceHelper(Context mCTX) {
        this.mCTX = mCTX;
    }

    public void init() {

    }

    public void startService() {
        Intent intent = new Intent(mCTX, DMLocationService.class);
        mCTX.startService(intent);
    }



}
