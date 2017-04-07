package com.inavr.bluetoothlibrary;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.callback.Connecter;
import com.inavr.bluetoothlibrary.callback.Reader;
import com.inavr.bluetoothlibrary.callback.ScanCallback;

/**
 * Created by kakahsh on 2017/1/17.
 */

public class BluetoothSDK {

    private static BluetoothSDKImpl bluetoothSDK;


    public static void initSDK(Context context, BlueOptions options) {
        Builder builder = new Builder();
        builder.setContext(context);
        builder.setOptions(options);
        bluetoothSDK = builder.create();
    }

    public static void setSCanner(ScanCallback sCanner) {
        bluetoothSDK.setScanListener(sCanner);
    }

    public static void setConnectCallback(Connecter callback) {
        bluetoothSDK.setConnectListener(callback);
    }

    public static void setReader(Reader reader) {
        bluetoothSDK.setReaderCallback(reader);
    }

    public static void scan() {
        bluetoothSDK.scan();
    }

    public static void stopScan() {
        bluetoothSDK.stopScan();
    }

    public static void connect(BluetoothDevice bluetoothDevice) {
        bluetoothSDK.connect(bluetoothDevice);
    }

    public static void disconnect() {
        bluetoothSDK.disconnect();
    }

    public static void write(Object o) {
        bluetoothSDK.write(o);
    }

    public static void release() {
        bluetoothSDK.release();
    }

    public static void turnOn() {
        bluetoothSDK.turnOn();
    }

    public static void turnOff() {
        bluetoothSDK.turnOff();
    }

    public static void openBlueSetting() {
        bluetoothSDK.openBlueSetting();
    }

    public static boolean isConnected() {
        return bluetoothSDK.isConnected();
    }
}
