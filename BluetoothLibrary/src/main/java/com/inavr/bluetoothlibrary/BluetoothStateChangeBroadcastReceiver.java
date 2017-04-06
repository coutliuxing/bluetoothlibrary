package com.inavr.bluetoothlibrary;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inavr.bluetoothlibrary.basic.BlueState;


/**
 * Created by win7 on 2017/1/11.
 */

public class BluetoothStateChangeBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int state;
        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            if (state == BluetoothAdapter.STATE_ON) {
                BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_STATE_ON);
            } else if (state == BluetoothAdapter.STATE_OFF) {
                BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_STATE_OFF);
            }
        }
    }
}
