package com.kakahsh.example;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.inavr.bluetoothlibrary.BluetoothSDK;
import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.basic.ConnectCode;
import com.inavr.bluetoothlibrary.callback.Connecter;

/**
 * Created by kakahsh on 2017/9/12.
 */

public class Connector {
    private int times;


    public void connect(final BluetoothDevice bluetoothDevice) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothSDK.connect(bluetoothDevice);
            }
        }).start();
    }

    public void disconnect() {

    }



    public class Builder {
        private int times;


        public void times(int times) {
            this.times = times;
        }

        public void context(Context context){
            BluetoothSDK.initSDK(context,new BlueOptions());
            BluetoothSDK.setConnectCallback(new Connecter() {
                @Override
                public void connectState(@ConnectCode int state) {

                }
            });
        }

        public Connector build() {



            return new Connector();
        }
    }
}
