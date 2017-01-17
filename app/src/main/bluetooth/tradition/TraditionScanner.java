package com.detech.bluetooth.tradition;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.detech.bluetooth.BlueStateObserver;
import com.detech.bluetooth.Scanner;
import com.detech.bluetooth.basic.BlueState;

import java.util.Set;

import static android.R.attr.action;

/**
 * Created by win7 on 2017/1/12.
 */

public class TraditionScanner extends Scanner {
    private BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice btDevice = null;  //创建一个蓝牙device对象
            // 从Intent中获取设备对象
            btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (btDevice == null) return;
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {  //发现设备
                BlueStateObserver.getInstance().onDeviceFound(btDevice);
//                try {
//                    scannable.onFound(btDevice);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancelScan();
            }
        }, timeout);
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void cancelScan() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
            BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CANCLE_DISCOVERY);
        }
        unRegisterReceiver();

    }

    //蓝牙连接状态
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //蓝牙配对
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

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
