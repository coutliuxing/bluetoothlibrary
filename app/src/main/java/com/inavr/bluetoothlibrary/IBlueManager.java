package com.inavr.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;

import com.inavr.bluetoothlibrary.callback.BluetoothConnectListener;


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
