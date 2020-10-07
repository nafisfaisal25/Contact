package com.example.contact.utils;


import android.Manifest;

public class Permissions {

    public static final String [] PHONE_PERMISSION = {Manifest.permission.CALL_PHONE};
    public static final String [] WRITE_STORAGE_PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String [] CAMERA_PERMISSION = {Manifest.permission.CAMERA};
    public static final int CAMERA_REQUEST_CODE = 2;


    public Permissions() {
    }
}
