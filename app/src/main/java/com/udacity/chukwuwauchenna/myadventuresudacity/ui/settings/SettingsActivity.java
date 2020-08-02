package com.udacity.chukwuwauchenna.myadventuresudacity.ui.settings;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;


import com.google.firebase.auth.FirebaseAuth;
import com.udacity.chukwuwauchenna.myadventuresudacity.R;
import com.udacity.chukwuwauchenna.myadventuresudacity.data.User;
import com.udacity.chukwuwauchenna.myadventuresudacity.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsActivityBinding binding
                = DataBindingUtil.setContentView(this, R.layout.settings_activity);
        if (savedInstanceState == null) {
            // Create the fragment only when the activity is created for the first time.
            // ie. not after orientation changes
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(MySettingsFragment.FRAGMENT_TAG);
            if (fragment == null) {
                fragment = new MySettingsFragment();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.preference_fragment, fragment, MySettingsFragment.FRAGMENT_TAG);
            ft.commit();
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            FirebaseAuth auth = FirebaseAuth.getInstance();
            User user = new User();
            user.setName(auth.getCurrentUser().getDisplayName());
            user.setProfilePhotoUrl(auth.getCurrentUser().getPhotoUrl().toString());
            user.setUserId(auth.getCurrentUser().getUid());
            user.setEmail(auth.getCurrentUser().getEmail());
            binding.setUser(user);
        }
    }

    @Override
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MySettingsFragment fragment = new MySettingsFragment();
        Bundle args = new Bundle();
        args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, pref.getKey());
        fragment.setArguments(args);
        ft.replace(R.id.preference_fragment, fragment, pref.getKey());
        ft.addToBackStack(pref.getKey());
        ft.commit();
        return false;
    }
}