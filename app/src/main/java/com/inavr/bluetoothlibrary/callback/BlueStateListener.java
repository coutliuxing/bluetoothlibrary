package com.inavr.bluetoothlibrary.callback;

import android.bluetooth.BluetoothDevice;

import com.inavr.bluetoothlibrary.basic.BlueState;


/**
 * Created by win7 on 2017/1/12.
 */

public interface BlueStateListener {
    void onStateChange(BlueState blueState);

    void onDeviceFound(BluetoothDevice bluetoothDevice);
}
