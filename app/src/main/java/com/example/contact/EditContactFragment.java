package com.example.contact;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.contact.DataModel.Contact;
import com.example.contact.utils.ChangeImageDialog;
import com.example.contact.utils.DataBaseHelper;
import com.example.contact.utils.ImageLoader;
import com.example.contact.utils.Permissions;

public class EditContactFragment extends Fragment implements ChangeImageDialog.OnPhotoReceivedListener {
    private static final String TAG = "EditContactFragment";

    //ui_component
    private View mView;
    private EditText mContactNameEditText;
    private EditText mContactNumberEditText;
    private EditText mContactEmailEditText;
    private ImageView mContactImage;
    //variables
    private Contact mContact;
    private String mSelectedImagePath;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_contact_info, container, false);
        ((TextView)mView.findViewById(R.id.edit_contact_toolbar_text_view)).setText("Edit Contact");
        mContact = getContactFromBundle();
        if (mContact != null) {
            setContactInfo();
            setListener();
        }
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
            saveChangesToDatabase();
            hideSoftKeyboard();
            getActivity().getSupportFragmentManager().popBackStack();
        });

        cameraIcon.setOnClickListener(view -> {
            if (isAllPermissionGranted()) {
                openDialogFragment();
            }
        });
    }

    private void openDialogFragment() {
        ChangeImageDialog changeImageDialog = new ChangeImageDialog();
        changeImageDialog.show(getFragmentManager(), getString(R.string.dialog_change_photo));
        changeImageDialog.setTargetFragment(EditContactFragment.this, 0);
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

    private void saveChangesToDatabase() {
        DataBaseHelper helper = new DataBaseHelper(getContext());
        Cursor cursor = helper.getContactId(mContact);
        int id = -1;
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        if (id > -1) {
            mContact.setName(mContactNameEditText.getText().toString());
            mContact.setNumber(mContactNumberEditText.getText().toString());
            mContact.setMail(mContactEmailEditText.getText().toString());
            if (mSelectedImagePath != null) {
                mContact.setImageUrl(mSelectedImagePath);
            }
            if (helper.updateContact(mContact, id)) {
                Toast.makeText(getContext(), "contact updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "failed to update contact", Toast.LENGTH_SHORT).show();
            }
        }

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
        mContactNameEditText.setText(mContact.getName());
        mContactNumberEditText.setText(mContact.getNumber());
        mContactEmailEditText.setText(mContact.getMail());
        ImageLoader.loadImage(getContext(), mContactImage, mContact.getImageUrl());
        mContactNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BD"));
    }

    private Contact getContactFromBundle() {
        Log.d(TAG, "getContactFromBundle: " + this.getArguments());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.contact));
        } else {
            return null;
        }
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
                deleteContact();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteContact() {
        DataBaseHelper helper = new DataBaseHelper(getContext());
        Cursor cursor = helper.getContactId(mContact);
        int id = -1;
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        if (id != -1) {
            if (helper.deleteContact(id)) {
                this.getArguments().clear();
                getFragmentManager().popBackStack();
                Toast.makeText(getContext(), "contact deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "failed to delete contact", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void getBitMapImage(Bitmap bitmap, Uri imagePathUri) {
        if (bitmap != null) {
            ((MainActivity)getActivity()).compressBitmap(bitmap, 100);
            mContactImage.setImageBitmap(bitmap);
            mSelectedImagePath = imagePathUri.toString();
        }
    }

    @Override
    public void getImagePath(Uri imagePathUri) {
        ImageLoader.loadImageFromSdCard(getContext(), mContactImage, imagePathUri);
        mSelectedImagePath = imagePathUri.toString();
    }
}
