package com.example.contact.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.contact.R;


public class ChangeImageDialog extends DialogFragment {
    private static final String TAG = "ChangeImageDialog";
    private OnPhotoReceivedListener mOnPhotoReceivedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_change_photo, container, false);
        setListener(view);
        return view;
    }

    private void setListener(View view) {
        TextView takePhoto = view.findViewById(R.id.dialog_take_photo);
        TextView choosePhoto = view.findViewById(R.id.dialog_choose_photo);
        TextView cancelDialog = view.findViewById(R.id.dialog_cancel);
        cancelDialog.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        takePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, Permissions.CAMERA_REQUEST_CODE);
        });

        choosePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("Image/*");
            startActivityForResult(intent, Permissions.PICFILE_REQUEST_CODE);
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Permissions.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mOnPhotoReceivedListener.getBitMapImage(bitmap);
            getDialog().dismiss();
        }

        if (requestCode == Permissions.PICFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imagePathUri = data.getData();
            Log.d(TAG, "onActivityResult: " + imagePathUri);
            mOnPhotoReceivedListener.getImagePath(imagePathUri);
            getDialog().dismiss();
        }
    }

    public interface OnPhotoReceivedListener {
        void getBitMapImage(Bitmap bitmap);
        void getImagePath(Uri imagePathUri);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOnPhotoReceivedListener = (OnPhotoReceivedListener)getTargetFragment();
    }
}
