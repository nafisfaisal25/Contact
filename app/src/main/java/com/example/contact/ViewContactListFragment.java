package com.example.contact;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.Adapter.ContactRecyclerAdapter;
import com.example.contact.DataModel.Contact;
import com.example.contact.utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewContactListFragment extends Fragment {

    private static final String TAG = "ViewContactListFragment";

    private static final int STANDARD_MODE = 0;
    private static final int SEARCH_MODE = 1;


    //UiComponent
    private RecyclerView mRecyclerView;
    private View mView;
    private LinearLayout mSearchToolBar, mStandardToolBar;
    private ImageView mBackArrow;
    private ImageView mSearchIcon;
    private EditText mSearchEditText;
    private FloatingActionButton fab;

    //vars
    private ContactRecyclerAdapter mAdapter;
    private List<Contact> mContactList = new ArrayList<>();
    private int mToolBarState;
    private OnContactSelectedListener mOnContactSelectedListener;
    private OnContactAddedListener mOnContactAddedListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(R.layout.fragment_view_contact_list,container,false);
         loadContactFromDb();
         initRecyclerView();
         mSearchToolBar = mView.findViewById(R.id.search_toolbar);
         mStandardToolBar = mView.findViewById(R.id.standard_toolbar);
         mBackArrow = mView.findViewById(R.id.search_back_arrow);
         mSearchIcon = mView.findViewById(R.id.toolbar_search_icon);
         mSearchEditText = mView.findViewById(R.id.search_edit_text);
         fab = mView.findViewById(R.id.fab_add_contact);
         setListener();

        return mView;
    }

    private void setListener() {
        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToolbarState();
            }
        });

        mSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToolbarState();
            }
        });

        fab.setOnClickListener(v -> {
            mOnContactAddedListener.onContactAdded();
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAdapter.filter(s.toString());
            }
        });
    }

    private void toggleToolbarState() {
        if(mToolBarState == STANDARD_MODE) {
            changeToolbarState(SEARCH_MODE);
        } else {
            changeToolbarState(STANDARD_MODE);
            mAdapter.filter("");
        }
    }

    private void changeToolbarState(int state) {
        mToolBarState = state;
        if(mToolBarState == STANDARD_MODE) {
            mStandardToolBar.setVisibility(View.VISIBLE);
            mSearchToolBar.setVisibility(View.GONE);
            hideSoftKeyboard();
        } else {
            mSearchEditText.setText("");
            mStandardToolBar.setVisibility(View.GONE);
            mSearchToolBar.setVisibility(View.VISIBLE);
        }
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getView();
        try {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        } catch (NullPointerException e) {

        }
    }

    private void initRecyclerView() {
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mAdapter = new ContactRecyclerAdapter(getContext(), mContactList, position -> {
            mOnContactSelectedListener.onContactSelected(mContactList.get(position));
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadContactFromDb() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getContext());
        Cursor cursor = dataBaseHelper.getAllContacts();
        mContactList.clear();
        while (cursor.moveToNext()) {
            mContactList.add(Contact.create(c -> {
                c.setName(cursor.getString(1))
                        .setNumber(cursor.getString(2))
                        .setDevice(cursor.getString(3))
                        .setMail(cursor.getString(4))
                        .setImageUrl(cursor.getString(5));
            }));
        }
        sortContactsByName();
    }

    private void sortContactsByName() {
        Collections.sort(mContactList, (contact1, contact2) -> {
            return contact1.getName().compareToIgnoreCase(contact2.getName());
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnContactSelectedListener = (OnContactSelectedListener)getActivity();
            mOnContactAddedListener = (OnContactAddedListener)getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "ClassCastException: " + e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        changeToolbarState(STANDARD_MODE);
    }

    public interface OnContactSelectedListener {
        void onContactSelected(Contact contact);
    }

    public interface OnContactAddedListener {
        void onContactAdded();
    }




}
