package com.example.chedifier.previewofandroidn.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chedifier on 2016/3/10.
 */
public class IPCBean implements Parcelable {

    public int id;
    public String name;

    public int sheepNumber;

    public IPCBean(){

    }

    protected IPCBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
        sheepNumber = in.readInt();
    }

    public static final Creator<IPCBean> CREATOR = new Creator<IPCBean>() {
        @Override
        public IPCBean createFromParcel(Parcel in) {
            return new IPCBean(in);
        }

        @Override
        public IPCBean[] newArray(int size) {
            return new IPCBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(sheepNumber);
    }

    @Override
    public String toString(){
        return "id: "+id+"; " + "name: "+name+"; " + "sheepNumber: " + sheepNumber;
    }
}
