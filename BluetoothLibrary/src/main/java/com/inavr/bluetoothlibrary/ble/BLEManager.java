package com.inavr.bluetoothlibrary.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.inavr.bluetoothlibrary.BlueStateObserver;
import com.inavr.bluetoothlibrary.IBlueManager;
import com.inavr.bluetoothlibrary.IRequireUuidCipherCallback;
import com.inavr.bluetoothlibrary.basic.BlueState;

import java.util.UUID;

/**
 * Created by win7 on 2016/12/22.
 * 蓝牙4.0管理
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEManager implements IBlueManager {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private boolean isConnected;
    private static BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private BLEWriter bleWriter;

    private static final String DATA_SERVICE_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String TXD_CHARACT_UUID = "0000fff6-0000-1000-8000-00805f9b34fb";

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable connectRunnable;

    public BLEManager(Context context, BluetoothAdapter bluetoothAdapter) {
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
    }

    @Override
    public synchronized void write(Object o) {
        byte[] data = (byte[]) o;
        if (bluetoothGattCharacteristic == null) return;

        if (bleWriter != null) bleWriter.addDate(data);
    }

    @Override
    public void read() {

    }

    @Override
    public void connect(final BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        mBluetoothGatt = BLEManager.this.bluetoothDevice.connectGatt(context, true, mGattCallback);
        connectRunnable = new Runnable() {
            @Override
            public void run() {
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.close();
                    BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_FAIL);
                }
            }
        };
        handler.postDelayed(connectRunnable, 10 * 1000);
    }

    @Override
    public void close() {
        if (mBluetoothGatt != null) {
            if (bleWriter != null) {
                bleWriter.destroy();
                bleWriter = null;
            }
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
        }
        BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_DISCONNECTED);
    }

    @Override
    public boolean isConnected() {

        return isConnected;
    }

    @Override
    public void setBlueCallBack(IRequireUuidCipherCallback requireUuidCipherCallback) {

    }

    @Override
    public void disonnect() {
        close();

    }


    /**
     * 连接上后的事件处理回调
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        /**
         * 连接状态变化
         * @param gatt
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i(TAG, "gatt_state" + status);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                isConnected = true;
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isConnected = false;
                close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService data_service = mBluetoothGatt.getService(UUID.fromString(DATA_SERVICE_UUID));  // 先获取BluetoothGattService
                bluetoothGattCharacteristic = data_service.getCharacteristic(UUID.fromString(TXD_CHARACT_UUID)); // 再通过BluetoothGattService获取BluetoothGattCharacteristic特征值
                bleWriter = new BLEWriter(bluetoothGattCharacteristic, mBluetoothGatt);
                bleWriter.start();
                if (bluetoothGattCharacteristic == null) {
                    BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_FAIL);
                    close();
                } else {
                    BlueStateObserver.getInstance().onStateChange(BlueState.BLUETOOTH_CONNECT_SUCCESS);

                }
                if (connectRunnable != null) {
                    handler.removeCallbacks(connectRunnable);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i(TAG, new String(characteristic.getValue()));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            bleWriter.onWriteChange(false);
        }
    };


}
