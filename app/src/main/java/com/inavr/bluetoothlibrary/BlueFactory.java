package com.inavr.bluetoothlibrary;

import android.content.Context;

import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.callback.BlueStateListener;

/**
 * Created by kakahsh on 2017/1/17.
 */

public class BlueFactory {

    public static BluetoothSDKImpl buildSDK(Context context, BlueOptions options, BlueStateListener blueStateListener) {
        Builder builder = new Builder();
        builder.setContext(context);
        builder.setOptions(options);
        builder.setBlueStateListener(blueStateListener);
        return builder.create();
    }

    public static BluetoothSDKImpl buildSDK(Context context, BlueOptions options) {
        Builder builder = new Builder();
        builder.setContext(context);
        builder.setOptions(options);
        return builder.create();
    }
}
