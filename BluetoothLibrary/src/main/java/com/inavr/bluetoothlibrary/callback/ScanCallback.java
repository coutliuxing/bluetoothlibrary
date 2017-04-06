package com.inavr.bluetoothlibrary.callback;

import android.bluetooth.BluetoothDevice;

import com.inavr.bluetoothlibrary.basic.ConnectCode;

/**
 * Created by win7 on 2016/12/29.
 */

public interface ScanCallback {
    void onFound(BluetoothDevice bluetoothDevice);

    void onStateChange(@ConnectCode int state);

}
