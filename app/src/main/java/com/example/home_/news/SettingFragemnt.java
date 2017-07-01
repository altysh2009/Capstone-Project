package com.example.home_.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


public class SettingFragemnt extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingFragemnt() {
        // Required empty public constructor
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
addPreferencesFromResource(R.xml.pref_genral);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

}
