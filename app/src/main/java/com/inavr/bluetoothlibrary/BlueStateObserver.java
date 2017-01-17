package com.inavr.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;

import com.inavr.bluetoothlibrary.basic.BlueState;
import com.inavr.bluetoothlibrary.callback.BlueStateListener;


/**
 * Created by win7 on 2017/1/12.
 */

public class BlueStateObserver {
    private static BlueStateObserver blueStateObserver;
    private BlueStateListener blueStateListener;

    private BlueStateObserver() {
    }

    public static void init(BlueStateListener blueStateListener) {
        if (blueStateObserver == null) {
            blueStateObserver = new BlueStateObserver();
            blueStateObserver.blueStateListener = blueStateListener;
        }
    }

    public static BlueStateObserver getInstance() {
        return blueStateObserver;
    }

    public void onDeviceFound(BluetoothDevice bluetoothDevice) {
        if (blueStateListener != null) {
            blueStateListener.onDeviceFound(bluetoothDevice);
        }
    }

    public void onStateChange(BlueState blueState) {
        if (blueStateListener != null)
            blueStateListener.onStateChange(blueState);
    }

    public void destroy() {
        blueStateListener = null;
        blueStateObserver = null;
    }
}
