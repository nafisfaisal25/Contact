package com.example.contact.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class ImageLoader {
    private static final String TAG = "ImageLoader";

    public static void loadImage(Context context, ImageView imageView, String ImageUrl) {

        Log.d(TAG, "loadImage: Path: " + ImageUrl);
        Glide.with(context)
                .asBitmap()
                .load(ImageUrl)
                .into(imageView);
    }

    public static void loadImageFromSdCard(Context context, ImageView imageView, Uri imageUri) {

        Log.d(TAG, "loadImage: Path: " + imageUri);
        Glide.with(context)
                .asBitmap()
                .load(imageUri)
                .into(imageView);
    }
}
