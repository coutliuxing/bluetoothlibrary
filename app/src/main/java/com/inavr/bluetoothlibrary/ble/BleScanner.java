package com.inavr.bluetoothlibrary.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;

import com.inavr.bluetoothlibrary.BlueStateObserver;
import com.inavr.bluetoothlibrary.Scanner;


/**
 * Created by win7 on 2017/1/12.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleScanner extends Scanner {
    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            BlueStateObserver.getInstance().onDeviceFound(device);
        }
    };

    public BleScanner(Context context, int timeout) {
        super(context, timeout);
    }


    @Override
    public void scan() {
        bluetoothAdapter.startLeScan(leScanCallback);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelScan();
            }
        }, timeout);
    }

    @Override
    public void cancelScan() {
        if (leScanCallback != null)
            bluetoothAdapter.stopLeScan(leScanCallback);
    }
}
