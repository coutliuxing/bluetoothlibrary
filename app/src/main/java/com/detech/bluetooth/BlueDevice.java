package com.detech.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kakahsh on 2017/1/13.
 */

public class BlueDevice implements Parcelable {
    private String name;
    private String mac;
    public static final Creator<BlueDevice> CREATOR = new Creator() {
        public BlueDevice createFromParcel(Parcel in) {
            BlueDevice blueInfo = new BlueDevice();
            blueInfo.name = in.readString();
            blueInfo.mac = in.readString();
            return blueInfo;
        }

        public BlueDevice[] newArray(int size) {
            return new BlueDevice[size];
        }
    };

    public BlueDevice() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return this.mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.mac);
    }

    public String toString() {
        return "{name=\'" + this.name + '\'' + ", mac=\'" + this.mac + '\'' + '}';
    }
}
