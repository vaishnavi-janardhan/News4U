package com.vaishjanardhan.news4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    static Intent starterIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        String theme = sharedPreferences.getString(
                getString(R.string.settings_theme_key),
                getString(R.string.settings_theme_default)
        );

        if (theme.equals(getString(R.string.settings_theme_dark_value)))
            setTheme(R.style.DarkTheme);
        else if (theme.equals(getString(R.string.settings_theme_light_value)))
            setTheme(R.style.AppTheme);

        starterIntent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(SettingsActivity.this);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference category = findPreference(getString(R.string.settings_category_key));
            bindPreferenceSummaryToValue(category);

            Preference noOfItems = findPreference(getString(R.string.settings_number_key));
            bindPreferenceSummaryToValue(noOfItems);

            Preference theme = findPreference(getString(R.string.settings_theme_key));
            bindPreferenceSummaryToValue(theme);

            theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().finish();
                    startActivity(starterIntent);
                    return true;
                }
            });
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.
                    getDefaultSharedPreferences(preference.getContext());
            String preferenceString = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    }
}
