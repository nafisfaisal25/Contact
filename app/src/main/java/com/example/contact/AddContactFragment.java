package com.example.contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.contact.DataModel.Contact;
import com.example.contact.utils.ChangeImageDialog;
import com.example.contact.utils.ImageLoader;
import com.example.contact.utils.Permissions;

import java.util.Locale;

import io.michaelrocks.libphonenumber.android.AsYouTypeFormatter;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;

public class AddContactFragment extends Fragment implements ChangeImageDialog.OnPhotoReceivedListener {
    private static final String TAG = "AddContactFragment";

    //ui_component
    private View mView;
    private EditText mContactNameEditText;
    private EditText mContactNumberEditText;
    private EditText mContactEmailEditText;
    private ImageView mContactImage;

    //variables
//    private Contact mContact;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_add_contact, container, false);
        ((TextView)mView.findViewById(R.id.edit_contact_toolbar_text_view)).setText("Add Contact");
        setContactInfo();
        setListener();

        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) mView.findViewById(R.id.edit_contact_toolbar));
        setHasOptionsMenu(true);

        return mView;
    }

    private void setListener() {
        ImageView backArrow = mView.findViewById(R.id.edit_contact_toolbar_back_icon);
        ImageView checkImage = mView.findViewById(R.id.edit_contact_toolbar_check_icon);
        ImageView cameraIcon = mView.findViewById(R.id.camera_icon);

        backArrow.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        checkImage.setOnClickListener(view -> {
            saveChanges();
            hideSoftKeyboard();
        });

        cameraIcon.setOnClickListener(view -> {
            if (isAllPermissionGranted()) {
                openDialogFragment();
            }
        });
        initOnTextChangeListener();
    }

    private void initOnTextChangeListener() {
        mContactNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BD"));
    }

    private void openDialogFragment() {
        ChangeImageDialog changeImageDialog = new ChangeImageDialog();
        changeImageDialog.show(getFragmentManager(), getString(R.string.dialog_change_photo));
        changeImageDialog.setTargetFragment(AddContactFragment.this, 0);
    }

    private boolean isAllPermissionGranted() {
        if (!checkWriteStoragePermission()) {
            return false;
        }

        if (!checkCameraPermission()) {
            return false;
        }
        return  true;
    }


    private boolean checkWriteStoragePermission() {
        if (!((MainActivity)getActivity()).checkPermission(Permissions.WRITE_STORAGE_PERMISSION)) {
            ((MainActivity)getActivity()).setOnPermissionGrantedListener(() -> {
                checkCameraPermission();
            });
            ((MainActivity)getActivity()).requestPermission(Permissions.WRITE_STORAGE_PERMISSION);
            return false;
        }
        return true;
    }

    private boolean checkCameraPermission() {
        if (!((MainActivity)getActivity()).checkPermission(Permissions.CAMERA_PERMISSION)) {
            ((MainActivity)getActivity()).setOnPermissionGrantedListener(() -> {
                openDialogFragment();
            });
            ((MainActivity)getActivity()).requestPermission(Permissions.CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    private void saveChanges() {
        mContactNameEditText.setText(mContactNameEditText.getText());
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getView();
        try {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        } catch (NullPointerException e) {

        }
    }

    private void setContactInfo() {
        mContactNameEditText = mView.findViewById(R.id.edit_text_contact_name);
        mContactNumberEditText = mView.findViewById(R.id.edit_text_contact_number);
        mContactEmailEditText = mView.findViewById(R.id.edit_text_contact_email);
        mContactImage = mView.findViewById(R.id.contact_image);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contact_delete_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete :
                Log.d(TAG, "item deleted");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getBitMapImage(Bitmap bitmap) {
        if (bitmap != null) {
            ((MainActivity)getActivity()).compressBitmap(bitmap, 100);
            mContactImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImagePath(Uri imagePathUri) {
        ImageLoader.loadImageFromSdCard(getContext(), mContactImage, imagePathUri);
    }
}
