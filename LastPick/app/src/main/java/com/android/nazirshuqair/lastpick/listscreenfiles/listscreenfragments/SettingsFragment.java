package com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments;

import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.R;

/**
 * Created by nazirshuqair on 10/8/14.
 */
public class SettingsFragment extends PreferenceFragment {

    static final String LOGTAG = "Project Log:";

    public static final String TAG = "SettingsFragment.TAG";


    @Override
    public void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    public interface PrefClickListener{

        public void secondsDelay(int seconds);

    }

    private PrefClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof PrefClickListener) {
            mListener = (PrefClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }
    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);
        // Find preference by key


        final EditTextPreference delayPref = (EditTextPreference) findPreference("delay_seconds");
        delayPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {


                int i = Integer.parseInt(newValue.toString()) * 1000;
                mListener.secondsDelay(i);

                    //Toast.makeText(getActivity().getBaseContext(), delayPref.getEntries()[index], Toast.LENGTH_LONG).show();

                return true;
            }
        });

    }
}
