package edu.ucsb.cs.capstone.letmypeoplecode.dowsingrod;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences form an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
