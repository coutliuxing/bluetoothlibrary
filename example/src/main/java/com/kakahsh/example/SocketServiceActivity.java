package com.kakahsh.example;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SocketServiceActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_service);
        LogCatHelper.getInstance(this, null).start();
        regist();

        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter.isEnabled()) {
            new LocalSocketService().start();
        } else {
            bluetoothAdapter.enable();
        }

//        if (bluetoothAdapter.isEnabled()) {
//            bluetoothAdapter.disable();
//
//        }

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!bluetoothAdapter.isEnabled())
//                    bluetoothAdapter.enable();
//            }
//        }, 20 * 1000);

    }

    private void regist() {

        BluetoothConnectActivityReceiver receiver = new BluetoothConnectActivityReceiver();
        IntentFilter btfilter = new IntentFilter();
        btfilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        btfilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        btfilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        btfilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        registerReceiver(receiver, btfilter);
    }
}
