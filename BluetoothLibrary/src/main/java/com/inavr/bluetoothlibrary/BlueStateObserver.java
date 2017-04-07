package com.inavr.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;

import com.inavr.bluetoothlibrary.basic.BlueState;
import com.inavr.bluetoothlibrary.callback.BlueStateListener;


/**
 * Created by win7 on 2017/1/12.
 */

public class BlueStateObserver {
    private static BlueStateObserver blueStateObserver;
    private BlueStateListener blueStateListener;

    private Handler handler;

    private BlueStateObserver() {
        handler = new Handler(Looper.getMainLooper());
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

    public void onDeviceFound(final BluetoothDevice bluetoothDevice) {
        if (blueStateListener != null && handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    blueStateListener.onDeviceFound(bluetoothDevice);
                }
            });
        }
    }

    public void onStateChange(final BlueState blueState) {
        if (blueStateListener != null && handler != null) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    blueStateListener.onStateChange(blueState);
                }
            });
        }
    }

    public void destroy() {
        blueStateListener = null;
        handler = null;
        blueStateObserver = null;
    }
}
