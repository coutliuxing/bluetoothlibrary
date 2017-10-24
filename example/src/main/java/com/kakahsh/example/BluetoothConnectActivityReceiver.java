package com.kakahsh.example;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

/*
 * 蓝牙模块自动配对
 *
 * */
public class BluetoothConnectActivityReceiver extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
//        ArboxUtils.Log("接收到系统蓝牙广播");
        String action = intent.getAction();
        // TODO Auto-generated method stub
        if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            abortBroadcast();

            BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            ArboxUtils.Log("recv pair " + btDevice.PAIRING_VARIANT_PASSKEY_CONFIRMATION);
            try {

                btDevice.setPairingConfirmation(true);
                ArboxUtils.Log("蓝牙开始配对");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = device.getName();

            int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
            switch (state) {
                case BluetoothDevice.BOND_NONE:
                    ArboxUtils.Log("删除配对");
                    break;
                case BluetoothDevice.BOND_BONDING:
                    ArboxUtils.Log("配对中");
                    break;
                case BluetoothDevice.BOND_BONDED:
                    ArboxUtils.Log("配对完成 ");
                    break;
            }
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            int state = (Integer) intent.getExtras().get(
                    BluetoothAdapter.EXTRA_STATE);
            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    ArboxUtils.Log("蓝牙已打开");
                    new LocalSocketService().start();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    ArboxUtils.Log("正在打开蓝牙...");
                    break;
                case BluetoothAdapter.STATE_OFF:
                    ArboxUtils.Log("蓝牙已关闭");
//                    BluetoothAdapter.getDefaultAdapter().enable();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    ArboxUtils.Log("正在关闭蓝牙...");
                    break;

                default:
                    break;
            }
        } else if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
            int state = (Integer) intent.getExtras().get(
                    BluetoothAdapter.EXTRA_CONNECTION_STATE);
            switch (state) {
                case BluetoothAdapter.STATE_CONNECTED:
                    ArboxUtils.Log("已连接~");
                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    ArboxUtils.Log("正在连接~");
                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    ArboxUtils.Log("已断开~");
                    break;
                case BluetoothAdapter.STATE_DISCONNECTING:
                    ArboxUtils.Log("正在断开...");
                    break;

                default:
                    break;
            }
        }
    }
}