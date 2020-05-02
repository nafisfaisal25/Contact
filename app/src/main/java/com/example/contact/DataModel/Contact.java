package com.example.contact.DataModel;

import java.util.function.Consumer;

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

    private Contact() {
    }

    public Contact setName(String mName) {
        this.mName = mName;
        return this;
    }

    public void setImageUrl(String mImageUrl) {
        this.mProfileImageUrl = mImageUrl;
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
}
