package com.inavr.bluetoothlibrary;

import android.content.Context;

import com.inavr.bluetoothlibrary.basic.BlueOptions;
import com.inavr.bluetoothlibrary.callback.BlueStateListener;

/**
 * Created by kakahsh on 2017/1/17.
 */

public class Builder {

    private BlueOptions options;
    private Context context;

    private BlueStateListener blueStateListener;


    public Builder setOptions(BlueOptions options) {
        this.options = options;
        return this;
    }

    public Builder setBlueStateListener(BlueStateListener blueStateListener) {
        this.blueStateListener = blueStateListener;
        return this;
    }

    public Builder setContext(Context context) {
        this.context = context;
        return this;
    }

    public BluetoothSDKImpl create() {
        BluetoothSDKImpl b = new BluetoothSDKImpl(this.context, this.options, blueStateListener);
        return b;
    }
}
