package com.example.contact.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.contact.R;

public class ChangeImageDialog extends DialogFragment {
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
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Permissions.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            getDialog().dismiss();
            mOnPhotoReceivedListener.onPhotoReceived(bitmap);
        }
    }

    public interface OnPhotoReceivedListener {
        void onPhotoReceived(Bitmap bitmap);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mOnPhotoReceivedListener = (OnPhotoReceivedListener)getTargetFragment();
    }
}
