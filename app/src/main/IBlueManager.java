package com.detech.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.detech.bluetooth.callback.BluetoothConnectListener;


/**
 * Created by win7 on 2016/12/22.
 */

public interface IBlueManager {


    void write(Object o);

    void read();

    void connect(BluetoothDevice bluetoothDevice, BluetoothConnectListener connectListener);

    void close();

    boolean isConnected();

    void setBlueCallBack(IRequireUuidCipherCallback requireUuidCipherCallback);

    void disonnect();

}
