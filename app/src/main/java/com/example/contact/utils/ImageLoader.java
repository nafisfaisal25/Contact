package com.example.contact.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageLoader {

    public static void loadImage(Context context, ImageView imageView, String ImageUrl) {
        Glide.with(context)
                .asBitmap()
                .load(ImageUrl)
                .into(imageView);
    }
}
