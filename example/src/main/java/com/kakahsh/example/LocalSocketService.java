package com.kakahsh.example;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by kakahsh on 2017/9/13.
 */

public class LocalSocketService extends Thread {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothServerSocket hostSocket;
    BluetoothSocket socket;

    String TAG = "socketService";

    //00805F9B34FB-00001101-0000-1000-8000
    public final static String uuid = "00805F9B34FB-00001101-0000-1000-8000";//00001101-0000-1000-8000-00805F9B34FB

    public LocalSocketService() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public void run() {
        super.run();
        try {
            createService(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createService(final String uuid) throws IOException {
        try {

//            Method listenMethod = BluetoothAdapter.class.getMethod("listenUsingRfcommOn", new Class[]{int.class});
//            hostSocket = (BluetoothServerSocket) listenMethod.invoke(mBluetoothAdapter, new Object[]{29});
            hostSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("tzyHost",
                    UUID.fromString(uuid));

                                /* 接受客户端的连接请求 */
            Log.i(TAG, "成功建立服务器，等待从机连接...");
            while (true) {
                Log.i(TAG, ">>>>>>>1");
                BluetoothSocket temp = hostSocket.accept();
                if (socket != null && socket.isConnected()) {
                    socket.close();
                }
                Thread.sleep(500);//socket可能还没完全准备好，阻塞线程等待关闭
                socket = temp;
                Log.i(TAG, "有主机登陆...");
                sendData(socket);
            }
        } catch (Exception e) {
            Log.i(TAG, "服务建立出错");
            e.printStackTrace();
        }
    }


    public void sendData(final BluetoothSocket bluetoothSocket) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OutputStream outputStream = bluetoothSocket.getOutputStream();

                    while (true) {
                        String time = format.format(Calendar.getInstance().getTime());
                        outputStream.write(time.getBytes());
                        sleep(60 * 1000);
                        Log.i(TAG, "发送了数据:" + time);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    Log.i(TAG, "OutputStream 出错");
                }
            }
        }).start();

    }
}
