package com.example.contact;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.Adapter.ContactInfoAdapter;
import com.example.contact.DataModel.Contact;
import com.example.contact.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

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
        ContactInfoAdapter adapter = new ContactInfoAdapter(contactInfo);
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
                moveToEditContactFragment();
            }
        });
    }

    private void moveToEditContactFragment() {
        Fragment editContactFragment = new EditContactFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editContactFragment);
        transaction.addToBackStack(getString(R.string.view_contact_fragment));
        transaction.commit();
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

    private Contact getContactFromBundle() {
        Log.d(TAG, "getContactFromBundle: " + this.getArguments());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.contact));
        } else {
            return null;
        }
    }
}
