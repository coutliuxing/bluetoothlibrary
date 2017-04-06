package com.inavr.bluetoothlibrary.basic;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by win7 on 2016/12/29.
 */

public class BlueOptions implements Parcelable {

    public int connectMode = ConnectMode.AUTO_CONNECT;
    public int timeout = 5 * 1000;
    public int scanType = ScanType.Tradition;
    public int connectType = ConnectType.Tradition;
    public int isRomote = 0;//1-true 0 - false


    public static final Creator<BlueOptions> CREATOR = new Creator<BlueOptions>() {
        @Override
        public BlueOptions createFromParcel(Parcel in) {
            BlueOptions options = new BlueOptions();
            options.connectMode = in.readInt();
            options.timeout = in.readInt();
            options.scanType = in.readInt();
            options.connectType = in.readInt();
            options.isRomote = in.readInt();
            return options;
        }

        @Override
        public BlueOptions[] newArray(int size) {
            return new BlueOptions[size];
        }
    };

    public int getConnectMode() {
        return connectMode;
    }

    public void setConnectMode(int connectMode) {
        this.connectMode = connectMode;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getScanType() {
        return scanType;
    }

    public void setScanType(int scanType) {
        this.scanType = scanType;
    }

    public int getConnectType() {
        return connectType;
    }

    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }

    public int isRomote() {
        return isRomote;
    }

    public void setRomote(int romote) {
        isRomote = romote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(connectMode);
        dest.writeInt(timeout);
        dest.writeInt(scanType);
        dest.writeInt(connectType);
        dest.writeInt(isRomote);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectMode {
        /**
         * 自动连接
         */
        int AUTO_CONNECT = 1;
        /**
         * 手动连接
         */
        int MANUAL = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanType {
        /**
         * 自动连接
         */
        int BLE = 1;
        /**
         * 手动连接
         */
        int Tradition = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConnectType {
        /**
         * 根据设备类型连接
         */
        int Auto = -1;

        /**
         * Ble连接
         */
        int BLE = 1;
        /**
         * 传统方式连接
         */
        int Tradition = 2;
    }
}
