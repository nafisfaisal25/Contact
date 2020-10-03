package com.example.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.contact.DataModel.Contact;


public class MainActivity extends AppCompatActivity implements
        ViewContactListFragment.OnContactSelectedListener,
        ViewContactFragment.OnEditContactListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;

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

    private void moveToEditContactFragment(Contact contact) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.contact), contact);
        Fragment editContactFragment = new EditContactFragment();
        editContactFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editContactFragment);
        transaction.addToBackStack(getString(R.string.view_contact_fragment));
        transaction.commit();
    }

    /**
     * Method for asking permission. Can ask any array of permission.
     * @param permissions
     */
    public void verifyPermission(String [] permissions) {
        Log.d(TAG, "verifyPermission: asking user for permission");
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);

    }

    public boolean checkPermission(String [] permissions) {
        Log.d(TAG, "checkPermission: checking permission for " + permissions[0]);
        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: permission is not granted for " + permissions[0]);
            return false;
        } else {
            Log.d(TAG, "checkPermission: permission is granted for " + permissions[0]);
            return true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);

        switch (requestCode) {
            case REQUEST_CODE:
                for (int i=0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: user has allowed permission to access " + permissions[i]);
                    } else {
                        Log.d(TAG, "onRequestPermissionsResult: user has denied permission to access" + permissions[i]);
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onContactSelected(Contact contact) {
        moveToViewContactFragment(contact);
    }

    @Override
    public void onEditContactSelected(Contact contact) {
        moveToEditContactFragment(contact);
    }
}
