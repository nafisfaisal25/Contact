package com.example.contact;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.contact.DataModel.Contact;


public class ViewContactFragment extends Fragment {

    private static final String TAG = "ViewContactFragment";

    //vars
    private View mBackArrow;
    private View mEditButton;
    private Contact mContact;

    //ui_component
    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragement_view_contact_info, container, false);
        mContact = getContactFromBundle();
        Log.d(TAG, "onCreateView: Name: " + mContact.getName());
        mBackArrow = mView.findViewById(R.id.view_contact_toolbar_back_icon);
        mEditButton = mView.findViewById(R.id.view_contact_toolbar_edit_icon);
        setListener();
        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) mView.findViewById(R.id.view_contact_toolbar));
        setHasOptionsMenu(true);
        return mView;
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
