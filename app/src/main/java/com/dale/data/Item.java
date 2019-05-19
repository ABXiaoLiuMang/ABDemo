package com.dale.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文件描述:
 * 作者Dale:2019/4/26
 */
public class Item implements Parcelable {

    public Item() {
    }

    private String head;

    protected Item(Parcel in) {
        head = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Item{" +
                "head='" + head + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(head);
    }
}
