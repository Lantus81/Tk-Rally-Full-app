package com.akristic.www.tkrally;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class MatchPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference numberOfSets = findPreference(getString(R.string.settings_number_of_sets_key));
            bindPreferenceSummaryToValue(numberOfSets);

            Preference numberOfGames = findPreference(getString(R.string.settings_number_of_games_key));
            bindPreferenceSummaryToValue(numberOfGames);

            Preference tiebreakValue = findPreference(getString(R.string.settings_tiebreak_key));
            bindPreferenceSummaryToValue(tiebreakValue);

            Preference advantageValue = findPreference(getString(R.string.settings_advantage_key));
            bindPreferenceSummaryToValue(advantageValue);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
