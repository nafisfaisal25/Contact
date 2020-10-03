package com.example.contact;

import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.contact.DataModel.Contact;
import com.example.contact.utils.ImageLoader;

public class EditContactFragment extends Fragment {
    private static final String TAG = "EditContactFragment";

    //ui_component
    private View mView;
    private EditText mContactNameEditText;
    private EditText mContactNumberEditText;
    private EditText mContactEmailEditText;
    private ImageView mContactImage;
    //variables
    private Contact mContact;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_edit_contact_info, container, false);
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
        backArrow.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        checkImage.setOnClickListener(view -> {
            saveChanges();
            hideSoftKeyboard();
        });
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
        mContactNameEditText.setText(mContact.getName());
        mContactNumberEditText.setText(mContact.getNumber());
        mContactEmailEditText.setText(mContact.getMail());
        ImageLoader.loadImage(getContext(), mContactImage, mContact.getImageUrl());

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
                Log.d(TAG, "item deleted");
        }
        return super.onOptionsItemSelected(item);
    }

}
