package com.example.contact.DataModel;

public class Contact {
    private String mName;
    private String mProfileImageUrl;
    private String mContactNumber;
    private String mEmail;
    private String mDevice;

    public Contact(String mName, String mImageUrl, String mNumber, String mail, String mDevice) {
        this.mName = mName;
        this.mProfileImageUrl = mImageUrl;
        this.mContactNumber = mNumber;
        this.mEmail = mail;
        this.mDevice = mDevice;
    }

    public Contact() {
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setImageUrl(String mImageUrl) {
        this.mProfileImageUrl = mImageUrl;
    }

    public void setNumber(String mNumber) {
        this.mContactNumber = mNumber;
    }

    public void setMail(String mMail) {
        this.mEmail = mMail;
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

    public void setDevice(String mDevice) {
        this.mDevice = mDevice;
    }
}
