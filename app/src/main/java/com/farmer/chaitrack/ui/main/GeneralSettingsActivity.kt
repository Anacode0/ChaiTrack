package com.farmer.chaitrack.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.farmer.chaitrack.R

class GeneralSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Settings"
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            val notificationsPref = findPreference<SwitchPreferenceCompat>("notifications")
            notificationsPref?.setOnPreferenceChangeListener { _, newValue ->
                val enabled = newValue as Boolean
                true
            }

            val themePref = findPreference<ListPreference>("theme")
            themePref?.setOnPreferenceChangeListener { _, newValue ->
                val theme = newValue as String
                applyTheme(theme)
                true
            }

            themePref?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

            val usernamePref = findPreference<EditTextPreference>("username")
            usernamePref?.setOnPreferenceChangeListener { _, newValue ->
                val username = newValue as String
                if (username.isNotBlank()) {
                    usernamePref.summary = username
                    true
                } else {
                    false
                }
            }

            usernamePref?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()

            val privacyPref = findPreference<Preference>("privacy_policy")
            privacyPref?.setOnPreferenceClickListener {
                true
            }
        }

        private fun applyTheme(theme: String) {
            when (theme) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
}