package com.example.contact.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.contact.R;

public class ChangeImageDialog extends DialogFragment {
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
    }
}
