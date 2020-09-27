package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.contact.DataModel.Contact;

import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity implements Consumer<Contact>{
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);
        findViewById(R.id.recycler_view);
    }

    private void init() {
        ViewContactListFragment fragment = new ViewContactListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void accept(Contact contact) {
        moveToViewContactFragment(contact);
    }

    private void moveToViewContactFragment(Contact contact) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.contact), contact);
        ViewContactFragment fragment = new ViewContactFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(getString(R.string.view_contact_list_fragment));
        transaction.commit();
    }
}
