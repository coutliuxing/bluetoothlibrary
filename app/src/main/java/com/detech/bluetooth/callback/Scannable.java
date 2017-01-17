package com.detech.bluetooth.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Created by win7 on 2016/12/29.
 */

public interface Scannable {
    void onFound(BluetoothDevice bluetoothDevice);
}
