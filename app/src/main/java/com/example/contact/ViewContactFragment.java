package com.example.contact;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.Adapter.ContactInfoAdapter;
import com.example.contact.DataModel.Contact;
import com.example.contact.utils.DataBaseHelper;
import com.example.contact.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.hdodenhof.circleimageview.CircleImageView;



public class ViewContactFragment extends Fragment {

    private static final String TAG = "ViewContactFragment";

    //vars
    private Contact mContact;

    //ui_component
    View mView;
    private RecyclerView mRecyclerView;
    private CircleImageView mImageView;
    private TextView mTextView;
    private View mBackArrow;
    private View mEditButton;
    private OnEditContactListener mOnEditContactSelected;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragement_view_contact_info, container, false);
        mContact = getContactFromBundle();
        Log.d(TAG, "onCreateView: Name: " + mContact.getName());
        mBackArrow = mView.findViewById(R.id.view_contact_toolbar_back_icon);
        mEditButton = mView.findViewById(R.id.view_contact_toolbar_edit_icon);
        mImageView = mView.findViewById(R.id.contact_image);
        mTextView = mView.findViewById(R.id.contact_name);
        mRecyclerView = mView.findViewById(R.id.contact_properties);
        initView();
        setListener();
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) mView.findViewById(R.id.view_contact_toolbar));
        setHasOptionsMenu(true);
        return mView;
    }

    private void initView() {
        setText();
        setImage();
        initContactInfoAdapter();
    }

    private void initContactInfoAdapter() {
        List<String> contactInfo = new ArrayList<>();
        contactInfo.add(mContact.getNumber());
        contactInfo.add(mContact.getMail());
        ContactInfoAdapter adapter = new ContactInfoAdapter(getContext(), contactInfo);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setImage() {
        ImageLoader.loadImage(getContext(), mImageView, mContact.getImageUrl());
    }

    private void setText() {
        mTextView.setText(mContact.getName());
    }

    private void setListener() {
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnEditContactSelected.onEditContactSelected(mContact);
            }
        });
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

    /**
     * check is contact is deleted or not
     */
    @Override
    public void onResume() {
        super.onResume();
        DataBaseHelper helper = new DataBaseHelper(getContext());
        Cursor cursor = helper.getContactId(mContact);
        int id = -1;
        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        if (id > -1) {
            //contact exist. Do nothing
        } else {
            getFragmentManager().popBackStack();
        }
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnEditContactSelected = (OnEditContactListener)getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "ClassCastException: " + e.getMessage());
        }
    }

    public interface OnEditContactListener {
        void onEditContactSelected(Contact contact);
    }
}
