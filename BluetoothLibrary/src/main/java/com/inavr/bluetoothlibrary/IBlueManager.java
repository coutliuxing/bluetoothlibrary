package com.inavr.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;


/**
 * Created by win7 on 2016/12/22.
 */

public interface IBlueManager {


    void write(Object o);

    void read();

    void connect(BluetoothDevice bluetoothDevice);

    void close();

    boolean isConnected();

    void setBlueCallBack(IRequireUuidCipherCallback requireUuidCipherCallback);

    void disonnect();

}
