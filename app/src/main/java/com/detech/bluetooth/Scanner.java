package com.detech.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by win7 on 2017/1/12.
 * 扫描、返回设备
 */

public abstract class Scanner {
    public Handler handler = new Handler(Looper.getMainLooper());
    public BluetoothAdapter bluetoothAdapter;
    public Context context;
    public int timeout;

    public Scanner(Context context, int timeout) {
        this.context = context;
        this.timeout = timeout;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public abstract void scan();

    public abstract void cancelScan();

}
