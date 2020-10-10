package com.example.contact.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.function.Consumer;

public class Contact implements Parcelable {
    private String mName;
    private String mProfileImageUrl;
    private String mContactNumber;
    private String mEmail;
    private String mDevice;

    private Contact() {
    }

    protected Contact(Parcel in) {
        mName = in.readString();
        mProfileImageUrl = in.readString();
        mContactNumber = in.readString();
        mEmail = in.readString();
        mDevice = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public Contact setName(String mName) {
        this.mName = mName;
        return this;
    }

    public Contact setImageUrl(String mImageUrl) {
        this.mProfileImageUrl = mImageUrl;
        return this;
    }

    public Contact setNumber(String mNumber) {
        this.mContactNumber = mNumber;
        return this;
    }

    public Contact setMail(String mMail) {
        this.mEmail = mMail;
        return this;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mProfileImageUrl;
    }

    public String getNumber() {
        return mContactNumber;
    }

    public String getMail() {
        return mEmail;
    }

    public String getDevice() {
        return mDevice;
    }

    public Contact setDevice(String mDevice) {
        this.mDevice = mDevice;
        return this;
    }

    public static Contact create(Consumer<Contact> contactConsumer) {
        Contact contact = new Contact();
        contactConsumer.accept(contact);
        return contact;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mProfileImageUrl);
        dest.writeString(mContactNumber);
        dest.writeString(mEmail);
        dest.writeString(mDevice);
    }
}
