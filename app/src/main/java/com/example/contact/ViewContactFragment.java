package com.example.contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact.Adapter.ContactRecyclerAdapter;
import com.example.contact.DataModel.Contact;

import java.util.ArrayList;
import java.util.List;

public class ViewContactFragment extends Fragment {

    private static final int STANDARD_MODE = 0;
    private static final int SEARCH_MODE = 1;


    //UiComponent
    private RecyclerView mRecyclerView;
    private View mView;
    private LinearLayout mSearchToolBar, mStandardToolBar;
    private ImageView mBackArrow;
    private ImageView mSearchIcon;
    private EditText mSearchEditText;

    //vars
    private ContactRecyclerAdapter mAdapter;
    private List<Contact> mContactList = new ArrayList<>();
    private int mToolBarState;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mView = inflater.inflate(R.layout.fragment_view_contacts,container,false);
         createDummyContactList();
         initRecyclerView();
         Toolbar toolbar = mView.findViewById(R.id.abc);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
         mSearchToolBar = mView.findViewById(R.id.search_toolbar);
         mStandardToolBar = mView.findViewById(R.id.standard_toolbar);
         mBackArrow = mView.findViewById(R.id.search_back_arrow);
         mSearchIcon = mView.findViewById(R.id.toolbar_search_icon);
         mSearchEditText = mView.findViewById(R.id.search_edit_text);
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
    }

    private void toggleToolbarState() {
        if(mToolBarState == STANDARD_MODE) {
            changeToolbarState(SEARCH_MODE);
        } else {
            changeToolbarState(STANDARD_MODE);
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
        mAdapter = new ContactRecyclerAdapter(getContext(), mContactList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void createDummyContactList() {
        for(int i=0;i<100;i++){
            Contact contact = Contact.create(contact1 -> {
                contact1.setName("NFS")
                        .setMail("fnafis0@gmail.com")
                        .setImageUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/1200px-Android_robot.svg.png");
            });
            mContactList.add(contact);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        changeToolbarState(STANDARD_MODE);
    }
}
