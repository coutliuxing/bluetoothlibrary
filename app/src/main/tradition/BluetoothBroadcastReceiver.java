package com.detech.bluetooth.tradition;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.detech.bluetooth.BlueStateObserver;
import com.detech.bluetooth.basic.BlueState;

/**
 * Created by kakahsh on 2016/12/1.
 */

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    public static final String BLUETOOTH_CONNECTED = "android.bluetooth.device.action.ACL_CONNECTED";
    public static final String BLUETOOTH_DISCONNECT_REQUESTED = "android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED";
    public static final String BLUETOOTH_DISCONNECTED = "android.bluetooth.device.action.ACL_DISCONNECTED";

    private IBluetoothBroadcastCallback bCallback;

    public interface IBluetoothBroadcastCallback {
        void onFonud(BluetoothDevice bluetoothDevice);
    }

    public BluetoothBroadcastReceiver(IBluetoothBroadcastCallback bCallback) {
        this.bCallback = bCallback;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case BLUETOOTH_CONNECTED:
                BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_ENABLE);
                break;
            case BLUETOOTH_DISCONNECTED:
                BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_DISCONNECTED);
                break;
            case BLUETOOTH_DISCONNECT_REQUESTED:
                break;
        }

        BluetoothDevice btDevice = null;  //创建一个蓝牙device对象
        // 从Intent中获取设备对象
        btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (btDevice == null) return;

//        if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//            state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
//            switch (state) {
//                case BluetoothDevice.BOND_NONE:
//                    Log.d("aaa", "BOND_NONE 删除配对");
////                    ClientCallBack.getInstance().returnBluetoothDevice(InavrvestErrorCode.BOND_NONE, "匹对失败");
//                    break;
//                case BluetoothDevice.BOND_BONDING:
//                    Log.d("aaa", "BOND_BONDING 正在配对");
//                    ClientCallBack.getInstance().returnBluetoothDevice(InavrvestErrorCode.BLUETOOTH_BOND_BONDING, "蓝牙匹对中", btDevice);
//                    break;
//                case BluetoothDevice.BOND_BONDED:
//                    Log.d("aaa", "BOND_BONDED 配对成功");
//                    ClientCallBack.getInstance().returnBluetoothDevice(InavrvestErrorCode.BLUETOOTH_BOND_BONDED, "蓝牙匹对成功", btDevice);
//                    break;
//            }
//        }

        if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) //再次得到的action，会等于PAIRING_REQUEST
        {
            try {
                AutoPair.setPairingConfirmation(btDevice.getClass(), btDevice, true);
                //2.终止有序广播
                abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                //3.调用setPin方法进行配对...
                boolean ret = AutoPair.setPin(btDevice.getClass(), btDevice, "0000");
                if (!ret) {
                    if (bCallback != null) {
                        BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_FAIL);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
