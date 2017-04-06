package com.inavr.bluetoothlibrary.ble;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;

import java.util.LinkedList;

/**
 * Created by win7 on 2016/12/27.
 */

class BLEWriter {
    private LinkedList<byte[]> linkedList;
    private BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;


    private boolean isBusy;

    private Thread workThread = new Thread(new Runnable() {
        @Override
        public void run() {
            byte[] data;
            while (true) {
                if (!linkedList.isEmpty() && (data = linkedList.getFirst()) != null && !isBusy) {
                    write(data);
                    linkedList.removeFirst();
                }
            }
        }
    });

    BLEWriter(BluetoothGattCharacteristic bluetoothGattCharacteristic, BluetoothGatt mBluetoothGatt) {
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        this.mBluetoothGatt = mBluetoothGatt;
        linkedList = new LinkedList<>();
    }

    void addDate(byte[] bytes) {
        linkedList.add(bytes);
    }

    void onWriteChange(boolean isBusy) {
        this.isBusy = isBusy;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void write(byte[] data) {
        isBusy = true;
        if (data == null || data.length <= 0 || !bluetoothGattCharacteristic.setValue(data)) {
            isBusy = false;
            return;
        }
        boolean writeSuccess = mBluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }


    public void start() {
        workThread.start();
    }


    public void destroy() {
        if (workThread.isInterrupted())
            workThread.stop();
        linkedList.clear();
    }

}
