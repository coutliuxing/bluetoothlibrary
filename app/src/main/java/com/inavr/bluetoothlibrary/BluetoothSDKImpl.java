package com.inavr.bluetoothlibrary;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.basic.BlueState;
import com.inavr.bluetoothlibrary.ble.BLEManager;
import com.inavr.bluetoothlibrary.ble.BleScanner;
import com.inavr.bluetoothlibrary.callback.BlueStateListener;
import com.inavr.bluetoothlibrary.callback.BluetoothConnectListener;
import com.inavr.bluetoothlibrary.tradition.TraditionManager;
import com.inavr.bluetoothlibrary.tradition.TraditionScanner;

import java.util.Set;

/**
 * Created by kakahsh on 2016/12/1.
 */
class BluetoothSDKImpl {
    private final String TAG = getClass().getSimpleName();
    private BluetoothAdapter bAdapter;
    private BlueOptions options;
    private Context context;
    private Scanner scanner;

    private BluetoothStateChangeBroadcastReceiver bluetoothStateChangeBroadcastReceiver;

    private IBlueManager blueManager;


    protected BluetoothSDKImpl(Context context, BlueOptions options, BlueStateListener blueStateListener) {
        this.options = options == null ? new BlueOptions() : options;
        this.context = context;
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothStateChangeBroadcastReceiver = new BluetoothStateChangeBroadcastReceiver();
        BlueStateObserver.init(blueStateListener);
        registerReceiver();
    }


    /**
     * 默认扫描完后的处理
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void scan() {
        if (blueManager != null)
            blueManager.close();
        initScanner();
        scanner.scan();

    }

    private void initScanner() {
        switch (options.scanType) {
            case BlueOptions.ScanType.Tradition:
                scanner = new TraditionScanner(context, options.timeout);
                break;
            case BlueOptions.ScanType.BLE:
                scanner = new BleScanner(context, options.timeout);
        }
    }

    /**
     * 关闭扫描
     */
    public void stopScan() {
        if (scanner != null)
            scanner.cancelScan();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void connect(final BluetoothDevice bluetoothDevice) {

        if (bluetoothDevice == null) {
            throw new NullPointerException("device info is null");
        }

        if (blueManager != null) {
            blueManager.disonnect();
        }

        createBlueManager(bluetoothDevice);

        blueManager.connect(bluetoothDevice, new BluetoothConnectListener() {
            @Override
            public void onConnected() {
                BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_SUCCESS);
            }

            @Override
            public void onDisconnected() {
                BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_FAIL);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void createBlueManager(final BluetoothDevice bluetoothDevice) {
        if (isSupporBLE(bluetoothDevice) && options != null) {
            blueManager = new BLEManager(BluetoothSDKImpl.this.context, bAdapter);
        } else {
            blueManager = new TraditionManager(context, bAdapter);
        }

    }

    public void write(Object o) {
        blueManager.write(o);
    }

    public boolean isConnected() {
        return blueManager != null && blueManager.isConnected();
    }

    /**
     * 释放资源
     */
    public void release() {
        if (blueManager != null) {
            blueManager.close();
            blueManager = null;
        }
        if (bAdapter.isDiscovering()) {
            bAdapter.cancelDiscovery();
        }
        unregisterReceiver();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (blueManager != null) {
            blueManager.close();
            blueManager = null;
        }
    }


    /**
     * 获得已经配对过的设备
     *
     * @return
     */
    public Set<BluetoothDevice> getBondedDevicesList() {
        return bAdapter.getBondedDevices();
    }

    /**
     * 强制打开蓝牙
     */
    public void turnOn() {
        if (bAdapter.isEnabled()) {
            return;
        }
        bAdapter.enable();
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        turnOn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(turnOn);
    }

    /**
     * 关闭蓝牙
     */
    public void turnOff() {
        bAdapter.disable();
        Toast.makeText(context, "关闭蓝牙", Toast.LENGTH_LONG).show();
    }

    /**
     * 设置蓝牙可见
     */
    public void setVisible() {
        Intent visibleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        context.startActivity(visibleIntent);
    }

    /**
     * 打开蓝牙设置
     */
    public void openBlueSetting() {
        if (context != null) {
            context.startActivity(new
                    Intent(Settings.ACTION_BLUETOOTH_SETTINGS));//直接进入手机中的蓝牙设置界面
        }
    }

    public boolean isSupport() {
        return bAdapter != null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean isSupporBLE(BluetoothDevice bluetoothDevice) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (options != null && options.connectMode == BlueOptions.ConnectType.BLE) {
                Log.w(TAG, "当前设备不支持BLE,已改为传统方式");
            }
            return false;
        }
        switch (bluetoothDevice.getType()) {
            case 0:
                break;
            case 1:
                return false;
            case 2:
            case 3:
                return true;
        }
        return false;
    }

    //蓝牙连接状态
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        context.registerReceiver(bluetoothStateChangeBroadcastReceiver, intentFilter);
    }

    private void unregisterReceiver() {

        if (bluetoothStateChangeBroadcastReceiver == null)
            return;
        try {
            context.unregisterReceiver(bluetoothStateChangeBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
