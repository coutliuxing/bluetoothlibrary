package com.inavr.bluetoothlibrary.callback;

import com.inavr.bluetoothlibrary.basic.ConnectCode;

/**
 * Created by win7 on 2016/12/23.
 */

public interface Connecter {

    void connectState(@ConnectCode int state);

}
