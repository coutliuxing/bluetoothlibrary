package com.detech.bluetooth;

/**
 * Created by win7 on 2016/12/22.
 */

public interface IRequireUuidCipherCallback {
    /**
     * 返回32位密文
     *
     * @param cypher    密文
     * @param timestamp 时间戳
     */
    public void onResult(String cypher, int timestamp);
}