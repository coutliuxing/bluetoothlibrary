package simple.test;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.detech.inavrvest.bluetooth.R;
import com.inavr.bluetoothlibrary.BluetoothSDK;
import com.inavr.bluetoothlibrary.basic.BlueState;
import com.inavr.bluetoothlibrary.callback.BlueStateListener;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = new TextView(this);

        BluetoothSDK.initSDK(this, null, new BlueStateListener() {
            @Override
            public void onStateChange(BlueState blueState) {
                Log.i("blueState", blueState.toString());
            }

            @Override
            public void onDeviceFound(BluetoothDevice bluetoothDevice) {
                Log.i("bluetoothDevice", bluetoothDevice.getName() + ">>>" + bluetoothDevice.getAddress());
                if (bluetoothDevice.getName().contains("CJ"))
                    BluetoothSDK.connect(bluetoothDevice);
            }
        });

        BluetoothSDK.scan();
    }
}
