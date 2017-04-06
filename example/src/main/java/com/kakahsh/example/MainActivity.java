package com.kakahsh.example;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.inavr.bluetoothlibrary.BluetoothSDK;
import com.inavr.bluetoothlibrary.basic.ConnectCode;
import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.callback.Connecter;
import com.inavr.bluetoothlibrary.callback.ScanCallback;

public class MainActivity extends AppCompatActivity {
    BluetoothDevice bluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView main_tv_devices = (TextView) findViewById(R.id.main_tv_devices);
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {

        }
        BluetoothSDK.initSDK(this, new BlueOptions());
        BluetoothSDK.setConnectCallback(new Connecter() {
            @Override
            public void connectState(@ConnectCode int state) {
                Log.i("Connecter", state + "");
                main_tv_devices.setText(state + "");
            }
        });

        BluetoothSDK.setSCanner(new ScanCallback() {
            @Override
            public void onFound(BluetoothDevice bluetoothDevice) {
                if (bluetoothDevice.getName().equals("Vest JW")) {
                    Log.i("Connecter", bluetoothDevice.getName() + ">>>>>");
                    MainActivity.this.bluetoothDevice = bluetoothDevice;
                }
            }

            @Override
            public void onStateChange(int state) {
                Log.i("scanner", state + "");
                main_tv_devices.setText(state + "");
            }

        });

    }

    public void scan(View view) {
        BluetoothSDK.scan();
    }

    public void stopScan(View view) {
        BluetoothSDK.stopScan();
    }

    public void connect(View view) {
        BluetoothSDK.connect(bluetoothDevice);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothSDK.release();
    }
}
