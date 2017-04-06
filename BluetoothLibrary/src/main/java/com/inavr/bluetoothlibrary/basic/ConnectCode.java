package com.inavr.bluetoothlibrary.basic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kakahsh on 2017/3/28.
 */

@Retention(RetentionPolicy.SOURCE)
public @interface ConnectCode {
    int BLUETOOTH_CONNECT_ENABLE = 101;

    int BLUETOOTH_DISCONNECTED = 102;

    int BLUETOOTH_DISABLE = 103;

    int BLUETOOTH_STATE_ON = 104;

    int BLUETOOTH_STATE_OFF = 105;

    int BLUETOOTH_BOND_BONDED = 106;

    int BOND_NONE = 107;

    int BLUETOOTH_BOND_BONDING = 108;

    int BLUETOOTH_CANCLE_DISCOVERY = 109;

    int BLUETOOTH_DISCOVERING = 110;

    int BLUETOOTH_FOUND = 111;

    int BLUETOOTH_UNFOUND = 113;

    int BLUETOOTH_CONNECT_SUCCESS = 114;

    int BLUETOOTH_CONNECT_FAIL = 115;

}