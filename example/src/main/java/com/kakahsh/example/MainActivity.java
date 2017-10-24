package com.kakahsh.example;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.inavr.bluetoothlibrary.BluetoothSDK;
import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.basic.ConnectCode;
import com.inavr.bluetoothlibrary.callback.Connecter;
import com.inavr.bluetoothlibrary.callback.ScanCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    BluetoothDevice bluetoothDevice;
    private String TAG = "tzy";
    TextView textView;
    EditText editText;
    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.obj == null)
                return;
            if (msg.obj.toString().equals("")) {
                textView.setText("");
            }
            String string = textView.getText().toString();
            if (string.length() > 4000)
                string = "";
            textView.setText(msg.obj.toString() + "\n" + string);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogCatHelper.getInstance(this, null).start();
        check();
        textView = (TextView) findViewById(R.id.main_tv_devices);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText = (EditText) findViewById(R.id.main_tv_device_name);
        editText.setText("AIdol000017");
//        final TextView main_tv_devices = (TextView) findViewById(R.id.main_tv_devices);
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) {

        }
        BluetoothSDK.initSDK(this, new BlueOptions());
        BluetoothSDK.setConnectCallback(new Connecter() {
            @Override
            public void connectState(@ConnectCode int state) {
                Log.i("Connecter", state + "");
            }
        });

        BluetoothSDK.setSCanner(new ScanCallback() {
            @Override
            public void onFound(BluetoothDevice bluetoothDevice) {
                Log.i("Connector", bluetoothDevice.getName() + ">>>>>" + bluetoothDevice.fetchUuidsWithSdp());
                sendMessage(bluetoothDevice.getName());
                if (bluetoothDevice.getName() != null && bluetoothDevice.getName().equals(editText.getText().toString())) {
                    MainActivity.this.bluetoothDevice = bluetoothDevice;
//                    MainActivity.this.bluetoothDevice.createBond();
//                    try {
//                        AutoPair.createBond(bluetoothDevice.getClass(),bluetoothDevice);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

            }

            @Override
            public void onStateChange(int state) {
                Log.i("scanner", state + "");
            }
        });
        regist();
    }

    private void check() {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Log.i(getClass().getSimpleName(), "不支持蓝牙");
        }

        Log.i(getClass().getSimpleName(), "地址：" + bluetoothAdapter.getAddress());
        Log.i(getClass().getSimpleName(), "名字：" + bluetoothAdapter.getName());


        if (!bluetoothAdapter.isEnabled()) {
            Log.i(getClass().getSimpleName(), "蓝牙不可用");
        }

        Log.i(getClass().getSimpleName(), "开启扫描：" + bluetoothAdapter.startDiscovery());


    }

    public void scan(View view) {
        BluetoothSDK.stopScan();
        if (editText.getText() == null) {
            return;
        }
        BluetoothSDK.scan();
    }

    public void stopScan(View view) {
//        BluetoothSDK.stopScan();
        if (bluetoothSocket != null)
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
                sendMessage("连接断开");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    BluetoothSocket bluetoothSocket;

    public void connect(View view) {
        if (bluetoothDevice == null)
            return;
        BluetoothSDK.stopScan();

        sendMessage("");
        sendMessage("开始连接" + editText.getText().toString());
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

//                BluetoothSDK.connect(bluetoothDevice);
//                sendMessage("连接成功,开始接收数据");
//                new ReadThread(bluetoothSocket).start();
                try {
                    if (bluetoothSocket != null) {
                        if (!bluetoothSocket.isConnected()) {
                            bluetoothSocket.close();
                        }
                        bluetoothSocket = null;
                    }
//                    Method m = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
//                    bluetoothSocket = (BluetoothSocket) m.invoke(bluetoothDevice, Integer.valueOf(29));
                    bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(
                            UUID.fromString("00805F9B34FB-00001101-0000-1000-8000"));
                    bluetoothSocket.connect();
                    sendMessage("连接成功,开始接收数据");
                    new ReadThread(bluetoothSocket).start();
                    new WriteThread().start();
                    Log.i(TAG, "连接成功");
                    timer();
                } catch (IOException e) {

                    Date date = new Date();
                    String tmp = "连接失败，点击CONNECT重连:" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                    Log.i(getClass().getSimpleName(), tmp);
                    sendMessage(tmp);
                    restart();
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void restart() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                stopScan(null);
//
//                try {
//                    Thread.sleep(10 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Log.i(TAG, "准备重启");
//                connect(null);
//            }
//        }).start();


    }


    public void send(View view) {
        if (bluetoothSocket == null) {
            ArboxUtils.Log("bluetooth socket null or noConnect");
            return;
        }
        try {
            OutputStream os = bluetoothSocket.getOutputStream();
            os.write(("{\"cmd\":\"volume_state\",\"state\":\"ok\",\"data\":null}").getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(String msg) {
        Message message = handler.obtainMessage();
        message.obj = msg;
        handler.sendMessage(message);
    }

    //读取数据现场
    private class ReadThread extends Thread {
        BluetoothSocket bluetoothSocket;

        public ReadThread(BluetoothSocket bluetoothSocket) {
            this.bluetoothSocket = bluetoothSocket;
        }

        public void run() {
            System.out.println("tzy:蓝牙开始接受数据..");
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream is = null;
            try {

                is = bluetoothSocket.getInputStream();
                while (true) {
                    if (!bluetoothSocket.isConnected()) {
                        continue;
                    }
                    if ((bytes = is.read(buffer)) != -1) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        Log.i(TAG, "收到数据" + s);
                        restart();
                        lastTime = Calendar.getInstance().getTimeInMillis();
                        sendMessage(s);
                    } else {
                        break;
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                Date date = new Date();
                String tmp = "socket连接中断，点击CONNECT重连" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                sendMessage(tmp);
//                connect(null);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    private class WriteThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                send(null);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private long lastTime;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (Calendar.getInstance().getTimeInMillis() - lastTime > 2 * 60 * 1000) {
//                MainActivity.this.stopScan(null);
                restart();
                Log.i(TAG, "接收不到新数据，进行socket重启");
            }
            handler.postDelayed(this, 2 * 60 * 1000);
        }
    };

    private void timer() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 10 * 1000);
    }

    public void checkConnect(View view) {
        textView.setText("");
//        Log.i("check", ">>>>>>" + isConnected());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothSDK.release();
    }

    private void regist() {

        ClientBroadcastReceiver receiver = new ClientBroadcastReceiver();
        IntentFilter btfilter = new IntentFilter();
        btfilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        btfilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        btfilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        btfilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        registerReceiver(receiver, btfilter);
    }
}
