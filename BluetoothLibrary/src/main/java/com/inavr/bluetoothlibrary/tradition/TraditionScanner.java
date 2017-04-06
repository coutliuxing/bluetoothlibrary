package com.inavr.bluetoothlibrary.tradition;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.inavr.bluetoothlibrary.BlueStateObserver;
import com.inavr.bluetoothlibrary.Scanner;
import com.inavr.bluetoothlibrary.basic.BlueState;

import java.util.Set;

/**
 * Created by win7 on 2017/1/12.
 */

public class TraditionScanner extends Scanner {
    private BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CANCLE_DISCOVERY);
                    break;
            }


            BluetoothDevice btDevice = null;  //创建一个蓝牙device对象
            // 从Intent中获取设备对象
            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (btDevice == null) return;
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {  //发现设备
                BlueStateObserver.getInstance().onDeviceFound(btDevice);
            }
        }
    };

    private Runnable removeCallbacks = new Runnable() {
        @Override
        public void run() {
            cancelScan();

        }
    };

    public TraditionScanner(Context context, int timeout) {
        super(context, timeout);
    }

    @Override
    public void scan() {
        registerReceiver();
        for (BluetoothDevice bluetoothDevice : getBondedDevicesList()) {
            BlueStateObserver.getInstance().onDeviceFound(bluetoothDevice);
        }
        handler.postDelayed(removeCallbacks, timeout);
        bluetoothAdapter.startDiscovery();
        BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_DISCOVERING);
    }

    @Override
    public void cancelScan() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CANCLE_DISCOVERY);
            handler.removeCallbacks(removeCallbacks);
            unRegisterReceiver();
        }

    }

    //蓝牙连接状态
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //蓝牙配对
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(bluetoothBroadcastReceiver, intentFilter);
    }

    //关闭蓝牙广播接收器
    private void unRegisterReceiver() {
        if (bluetoothBroadcastReceiver == null)
            return;
        try {
            context.unregisterReceiver(bluetoothBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得已经配对过的设备
     *
     * @return
     */
    private Set<BluetoothDevice> getBondedDevicesList() {
        return bluetoothAdapter.getBondedDevices();
    }
}
