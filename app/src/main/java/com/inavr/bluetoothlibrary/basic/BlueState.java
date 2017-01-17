package com.inavr.bluetoothlibrary.basic;

/**
 * Created by win7 on 2017/1/12.
 */

public enum BlueState {
    BLUETOOTH_CONNECT_ENABLE(101, "蓝牙正常连接"),
    BLUETOOTH_DISCONNECTED(102, "蓝牙失去连接"),

    BLUETOOTH_DISABLE(103, "蓝牙未开启"),
    BLUETOOTH_STATE_ON(104, "蓝牙打开"),
    BLUETOOTH_STATE_OFF(105, "蓝牙关闭"),

    BLUETOOTH_BOND_BONDED(106, "蓝牙匹对成功"),
    BOND_NONE(107, "删除配对"),
    BLUETOOTH_BOND_BONDING(108, "蓝牙匹对中"),

    BLUETOOTH_CANCLE_DISCOVERY(109, "扫描取消"),
    BLUETOOTH_CANCLE_DISCOVERING(110, "扫描中"),

    BLUETOOTH_FOUND(111, "找到蓝牙"),
    BLUETOOTH_DISCONNECTE(112, "蓝牙断开"),
    BLUETOOTH_UNFOUND(113, "蓝牙未找到"),
    BLUETOOTH_CONNECT_SUCCESS(114, "蓝牙连接成功"),
    BLUETOOTH_CONNECT_FAIL(115, "蓝牙连接失败");


    private int code;
    private String msg;

    BlueState(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BlueState{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
