package com.detech.bluetooth.tradition;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.detech.bluetooth.BlueStateObserver;
import com.detech.bluetooth.IBlueManager;
import com.detech.bluetooth.IRequireUuidCipherCallback;
import com.detech.bluetooth.basic.BlueState;
import com.detech.bluetooth.callback.BluetoothConnectListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by win7 on 2016/12/22.\
 * 传统的蓝牙管理
 */

public class TraditionManager implements IBlueManager {
    private final String TAG = getClass().getSimpleName();
    public BluetoothSocket bSocket;// 蓝牙通信socket
    private InputStream inputStream;
    private OutputStream outputStream;
    private Runnable readRunable;
    private IRequireUuidCipherCallback uuidCallback;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothBroadcastReceiver bluetoothBroadcastReceiver;
    private Context context;

    Thread thread;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper());

    public TraditionManager(Context context, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;


    }

    public TraditionManager(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void setUuidCallback(IRequireUuidCipherCallback uuidCallback) {
        this.uuidCallback = uuidCallback;
    }


    @Override
    public void write(Object o) {
        byte[] data = (byte[]) o;
        if (!isConnected()) return;
        try {
            outputStream.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void read() {
        readRunable = new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[64];
                int bytes;
                byte[] tmpBuffer = new byte[]{};
                try {
                    if (inputStream == null)
                        return;
                    // Read from the InputStream
                    if ((bytes = inputStream.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                    }
                } catch (IOException e) {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                            inputStream = null;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                handler.postDelayed(readRunable, 1000);
            }

        };

        handler.post(readRunable);
    }

    @Override
    public void connect(final BluetoothDevice bluetoothDevice, final BluetoothConnectListener bluetoothConnectListener) {
        close();
        registerReceiver();
        bSocket = getBluetoothSocket(bluetoothDevice);
        if (bSocket == null) {
            return;
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    bSocket.connect();
                    outputStream = bSocket.getOutputStream();
                    inputStream = bSocket.getInputStream();
                    bluetoothConnectListener.onConnected();
                    read();
                } catch (IOException e) {
                    e.printStackTrace();
                    bluetoothConnectListener.onDisconnected();
                }
            }
        });
        thread.start();
    }

    @Override
    public void close() {
        try {
            if (readRunable != null)
                handler.removeCallbacks(readRunable);
            if (inputStream != null)
                inputStream.close();
            if (outputStream != null)
                outputStream.close();
            if (bSocket != null) {
                bSocket.close();
                bSocket = null;
            }
            unRegisterReceiver();
            BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_DISCONNECTE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return bSocket != null && bSocket.isConnected();
    }

    @Override
    public void setBlueCallBack(IRequireUuidCipherCallback requireUuidCipherCallback) {
        this.uuidCallback = requireUuidCipherCallback;
    }

    @Override
    public void disonnect() {
        close();
    }

    //建立蓝牙通信
    public BluetoothSocket getBluetoothSocket(BluetoothDevice device) {
        if (device == null)
            return null;
        BluetoothSocket temp = null;
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            temp = (BluetoothSocket) m.invoke(device, 1);// 这里端口为1
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return temp;
    }


    //蓝牙连接状态
    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothBroadcastReceiver.BLUETOOTH_DISCONNECTED);
        intentFilter.addAction(BluetoothBroadcastReceiver.BLUETOOTH_CONNECTED);
        intentFilter.addAction(BluetoothBroadcastReceiver.BLUETOOTH_DISCONNECT_REQUESTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        //蓝牙配对
        intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        context.registerReceiver(bluetoothBroadcastReceiver, intentFilter);
    }

    //关闭蓝牙广播接收器
    public void unRegisterReceiver() {
        if (bluetoothBroadcastReceiver == null)
            return;
        try {
            context.unregisterReceiver(bluetoothBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
